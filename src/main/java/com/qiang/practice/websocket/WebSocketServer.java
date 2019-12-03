package com.qiang.practice.websocket;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.qiang.practice.base.Constants;
import com.qiang.practice.config.ServerConfig;
import com.qiang.practice.model.FileInfo;
import com.qiang.practice.model.SysTouristMsg;
import com.qiang.practice.model.SysUser;
import com.qiang.practice.model.SysUserMsg;
import com.qiang.practice.model.vo.MsgInfo;
import com.qiang.practice.service.*;
import com.qiang.practice.utils.*;
import com.qiang.practice.utils.response.WSResult;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.BinaryMessage;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author: CLQ
 * @Date: 2019/9/2
 * @Description: WebSocket服务
 */
@Component
public class WebSocketServer extends TextWebSocketHandler {

    @Autowired
    private SysUserMsgService sysUserMsgService;
    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private SysUserMsgListService sysUserMsgListService;
    @Autowired
    private SysTouristMsgListService sysTouristMsgListService;
    @Autowired
    private SysTouristMsgService sysTouristMsgService;
    @Autowired
    private RedisService redisService;
    @Autowired
    private ServerConfig serverConfig;
    @Autowired
    private MsgUtils msgUtils;

    private Map<String, WebSocketSession> socketCache = new ConcurrentHashMap<>();
    private static Logger log = LoggerFactory.getLogger(WebSocketServer.class);
    private Map<String, Map<String, FileInfo>> uploadFileInfoMap = new ConcurrentHashMap<>();
    private final Object lock = new Object();
    private boolean isServletStop = false;

    public WebSocketServer() {
    }

    /**
     * 将客户端的连接保存到缓存
     *
     * @param session
     * @throws Exception
     */
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        Map<String, Object> attributes = session.getAttributes();

        //判断本次连接是否是为了获取游客uuid
        if (attributes.get("needCreateUUID") != null) { //需要生成uuid并返回
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("type", 301);
            jsonObject.put("data", UUIDUtil.getUUID());
            session.sendMessage(new TextMessage(jsonObject.toJSONString()));
            return;
        }

        //获取客户端标识
        String clientId = getClientId(attributes);

        //如果有人正在使用该账号，将其顶下线
        if (socketCache.containsKey(clientId)) {
            socketCache.get(clientId).close(new CloseStatus(4001, "你的账号已在别处登录,若不是您本人操作,请及时修改密码或联系客服"));
        }
        socketCache.put(clientId, session); //将本次连接放入缓存

        //判断本次连接是游客还是用户
        if (NumUtil.isInteger(clientId)) { //用户
            sysUserService.setUserStatus(Long.valueOf(clientId), (byte) 1); //设置用户的登陆状态为在线
            noticeFriendOnline(clientId, (byte) 1); //通知他的好友他已经上线
        } else { //游客, 则为游客在聊天列表中添加客服
            sysTouristMsgListService.createChatListWithCustomerService(clientId);
        }

        //记录现在客户端正在与谁聊天
        Object anotherUserIdObject = attributes.get("anotherUserId");
        if (anotherUserIdObject != null) {
            String anotherUserId = anotherUserIdObject.toString(); //正在聊天的人的id
            if (!NumUtil.isInteger(anotherUserId) || Long.valueOf(anotherUserId) != 0) {
                //在缓存中记录正在聊天的人的id
                redisService.hset(Constants.USER_CHAT_WITH_WHO, clientId, anotherUserId);

                //将两人的未读消息全部置为已读
                if (MsgUtils.isSysUser(clientId, anotherUserId)) { //用户相关模块
                    sysUserMsgService.updateAllMsgToHasReaded(Long.valueOf(clientId), Long.valueOf(anotherUserId));
                } else { //游客相关模块
                    sysTouristMsgService.updateAllMsgToHasReaded(clientId, anotherUserId);
                }
            }
        }

        //判断客户端是否有未发送完的文件
        Map<String, FileInfo> fileInfoMap = uploadFileInfoMap.get(clientId);
        if (!Objects.isNull(fileInfoMap) && !fileInfoMap.isEmpty()) { //有, 将其发完

            for (Map.Entry<String, FileInfo> fileInfoEntry : fileInfoMap.entrySet()) {
                session.sendMessage(new TextMessage(JSONObject.toJSONString(new WSResult(202, false, fileInfoEntry.getValue(), fileInfoEntry.getKey()))));
            }

        }

        log.info("客户端 {} 已连接，当前在线数为: {}", clientId, socketCache.size());
        session.sendMessage(new TextMessage("连接成功"));
    }

    /**
     * 有用户上线则通知他的好友该用户已上线
     *
     * @param clientId
     */
    private void noticeFriendOnline(String clientId, byte status) {

        //通知时的数据准备
        Map<String, Object> msgMap = new HashMap<>();
        msgMap.put("type", 9);
        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("id", Long.valueOf(clientId));
        dataMap.put("status", status);
        msgMap.put("data", dataMap);
        TextMessage message = new TextMessage(JSONObject.toJSONString(msgMap));

        //获取客服id列表
        List<Long> idList = sysUserService.getCustomerServiceIdList();
        boolean isCustomerService = false;
        if (idList.contains(Long.valueOf(clientId))) {  //上线的这个人是客服，游客也一起通知
            isCustomerService = true;
        }

        //通知
        for (Map.Entry<String, WebSocketSession> entry : socketCache.entrySet()) {

            //不是本人才通知
            if (!entry.getKey().equals(clientId)) {

                if (entry.getValue().isOpen()) { //会话正常

                    if (NumUtil.isInteger(entry.getKey())) { //是用户，无论上线的是不是客服，都通知
                        send(entry.getValue(), message);
                    } else { //是游客，若上线的是客服，就通知他
                        if (isCustomerService) { //上线的是客服，需要通知
                            send(entry.getValue(), message);
                        }
                    }

                }

            }
        }
    }

    /**
     * 连接断开
     *
     * @param session
     * @param closeStatus
     * @throws Exception
     */
    public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
        doAfterWebSocketClose(session.getAttributes(), true); //连接关闭后的处理
    }

    /**
     * 连接发生错误
     *
     * @param session
     * @param exception
     * @throws Exception
     */
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        if (session.isOpen()) { //若连接还在开启中，则关闭
            session.close();
        }
        doAfterWebSocketClose(session.getAttributes(), true); //连接关闭后的处理
    }


    /**
     * 将收到的二进制流追加写进本地文件(并行接收)
     *
     * @param session
     * @param message
     */
    @Override
    protected void handleBinaryMessage(WebSocketSession session, BinaryMessage message) {
        String fileServerSonPath = null;
        String originFileServerSonPath = null;

        //拿到客户端标识
        String clientId = getClientId(session.getAttributes());

        //取出二进制流中头部的18位标识
        byte[] signBytes = new byte[18];
        message.getPayload().get(signBytes, 0, 18);
        String sign = new String(signBytes);

        //取出该客户端要上传的文件的信息
        FileInfo fileInfo = null;
        synchronized (this) {
            fileInfo = uploadFileInfoMap.get(clientId).get(sign);
        }

        //拿到文件流转为字节数组
        byte[] fileBytes = new byte[message.getPayloadLength()];
        message.getPayload().get(fileBytes, 0, message.getPayloadLength());

        FileOutputStream fos = null;
        FileChannel fc = null;
        try {

            //判断本次二进制流在网络传输过程中是否有损坏
            if (fileInfo.getSeparateSize() > fileBytes.length) { //损坏，重新索要本段数据
                session.sendMessage(new TextMessage(JSONObject.toJSONString(new WSResult(202, false, fileInfo, sign))));
                return;
            }

            //未损坏，开始处理
            //获取文件后缀名
            String fileNameExt = fileInfo.getFileName().substring(fileInfo.getFileName().lastIndexOf("."));
            //生成唯一文件名
            if (fileInfo.getFileServerName() == null) {
                fileInfo.setFileServerName(UUIDUtil.getUUID());
                fileInfo.setFileServerPath(serverConfig.getSaveFilePath() + fileInfo.getFileServerName() + fileNameExt);
            }

            //创建该文件
            File file = new File(fileInfo.getFileServerPath());
            if (!file.exists()) {
                file.createNewFile();
            }

            //创建指向该文件的流输出管道
            fos = new FileOutputStream(file, true);
            fc = fos.getChannel();
            //将收到的文件转成字节流

            ByteBuffer bbf = ByteBuffer.wrap(fileBytes);
            bbf.put(fileBytes);
            bbf.flip();
            //将字节流从上次的位置开始追加进文件(后期做断点续传时就是这种模式)
            fc.write(bbf, fileInfo.getIndexStart());
            //清空内存
            fos.flush();

            //将位置向前推进
            fileInfo.setIndexStart(fileInfo.getIndexStart() + fileBytes.length);

            if (fileInfo.getIndexStart() >= (fileInfo.getFileSize())) { //上传完成

                //获取该文件的md5值
                String fileMD5 = DigestUtils.md5Hex(new FileInputStream(file));
                fileInfo.setContentMD5(fileMD5);

                //判断该文件在服务器是否已经存在
                fileServerSonPath = redisService.hget(Constants.SAVE_FILE_MD5_MAP, fileMD5);
                if (!Objects.isNull(fileServerSonPath)) { //已存在,直接使用已存在的文件

                    if (ImageUtil.isImage(fileInfo.getFileType())) { //图片
                        fileInfo.setFileUrl(serverConfig.getImageUrl(fileServerSonPath));
                        originFileServerSonPath = redisService.hget(Constants.SAVE_FILE_MD5_MAP, fileMD5 + "-origin");
                        fileInfo.setOriginUrl(serverConfig.getImageUrl(originFileServerSonPath));
                    } else { //文件
                        fileInfo.setFileUrl(serverConfig.getFileDownloadUrl(fileServerSonPath));
                    }

                    file.delete(); //删除本次接收的文件
                    log.info("本次直接用的服务器上的文件");

                } else {

                    if (ImageUtil.isImage(fileInfo.getFileType())) {

                        //图片文件，进行压缩
                        String desFileName = ImageUtil.compressPicForScale(
                                file.getPath(),
                                serverConfig.getSaveFilePath() + fileInfo.getFileServerName() + "-yasuo",
                                file.getName(),
                                fileInfo.getFileServerName().concat("-yasuo"),
                                50L
                        );

                        //返回加载图片的url
                        originFileServerSonPath = serverConfig.getSaveFileSonFullPath() + file.getName();
                        fileInfo.setOriginUrl(serverConfig.getImageUrl(originFileServerSonPath));
                        //返回加载缩略图的url
                        fileServerSonPath = serverConfig.getSaveFileSonFullPath() + desFileName;
                        fileInfo.setFileUrl(serverConfig.getImageUrl(fileServerSonPath));

                        //缓存图片原图路径
                        redisService.hset(Constants.SAVE_FILE_MD5_MAP, fileMD5 + "-origin", originFileServerSonPath);

                    } else {

                        //非图片文件，返回下载文件的url
                        fileServerSonPath = serverConfig.getSaveFileSonFullPath() + file.getName();
                        fileInfo.setFileUrl(serverConfig.getFileDownloadUrl(fileServerSonPath));

                    }
                    //缓存缩略图或文件路径
                    redisService.hset(Constants.SAVE_FILE_MD5_MAP, fileMD5, fileServerSonPath);
                    log.info("服务器上没有该文件,将它保存了下来");
                }

                //将数据库中的信息进行完善
                WSResult wsResult = null;
                if (fileInfo.getIsSysUser()) { //用户之间的消息

                    SysUserMsg sysUserMsg = new SysUserMsg();
                    sysUserMsg.setIsSysUser(true);
                    sysUserMsg.setMsg(FileUtil.getMsg(fileInfo, fileServerSonPath, originFileServerSonPath));
                    sysUserMsg.setFromUserId(Long.valueOf(clientId));
                    sysUserMsg.setToUserId(Long.valueOf(fileInfo.getToUserId()));
                    sysUserMsg.setMsgType(FileUtil.getFileType(fileInfo.getFileType()));
                    sysUserMsg.setFileUrl(fileInfo.getFileUrl());
                    sysUserMsg.setCreateDate(new Date());
                    sysUserMsg.setIsRecall((byte) 0);
                    sysUserMsg.setIsRead(msgUtils.getMsgIsRead(clientId, fileInfo.getToUserId()));
                    sysUserMsg.setContentMD5(fileMD5);

                    if (ImageUtil.isImage(fileInfo.getFileType())) {
                        sysUserMsg.setOriginUrl(fileInfo.getOriginUrl());
                    } else {
                        sysUserMsg.setFileName(fileInfo.getFileName());
                    }
                    //将消息存进数据库
                    sysUserMsgService.insert(sysUserMsg);
                    sysUserMsgListService.insertAfterSendMsg(Long.valueOf(clientId), Long.valueOf(fileInfo.getToUserId()), sysUserMsg.getCreateDate());
                    fileInfo.setId(sysUserMsg.getId());
                    wsResult = new WSResult(4, sysUserMsg);
                } else { //与游客有关的消息

                    SysTouristMsg sysTouristMsg = new SysTouristMsg();
                    sysTouristMsg.setFromUserId(clientId);
                    sysTouristMsg.setToUserId(fileInfo.getToUserId());
                    sysTouristMsg.setMsg(FileUtil.getMsg(fileInfo, fileServerSonPath, originFileServerSonPath));
                    sysTouristMsg.setMsgType(FileUtil.getFileType(fileInfo.getFileType()));
                    sysTouristMsg.setFileUrl(fileInfo.getFileUrl());
                    sysTouristMsg.setCreateDate(new Date());
                    sysTouristMsg.setIsRecall((byte) 0);

                    if (ImageUtil.isImage(fileInfo.getFileType())) {
                        sysTouristMsg.setOriginUrl(fileInfo.getOriginUrl());
                    } else {
                        sysTouristMsg.setFileName(fileInfo.getFileName());
                    }

                    sysTouristMsg.setContentMD5(fileMD5);
                    sysTouristMsg.setIsRead(msgUtils.getMsgIsRead(clientId, fileInfo.getToUserId()));

                    //将消息存进数据库
                    sysTouristMsgService.insert(sysTouristMsg);
                    sysTouristMsgListService.insertAfterSendMsg(clientId, fileInfo.getToUserId(), sysTouristMsg.getCreateDate());
                    fileInfo.setId(sysTouristMsg.getId());
                    wsResult = new WSResult(4, sysTouristMsg);
                }
                send(fileInfo.getToUserId(), new TextMessage(JSONObject.toJSONString(wsResult)));
                synchronized (this) {
                    uploadFileInfoMap.get(clientId).remove(sign);
                }

                //该文件的引用数+1
                synchronized (lock) {
                    String useCount = redisService.hget(Constants.SAVE_FILE_MD5_USE_COUNT, fileMD5);
                    redisService.hset(Constants.SAVE_FILE_MD5_USE_COUNT, fileMD5,
                            Objects.isNull(useCount) ? "1" : String.valueOf(Integer.valueOf(useCount) + 1));
                }
                System.gc(); //清理缓存,防止jvm持续锁定该文件
            }

            fileInfo.setPercent((int) (Math.floor(fileInfo.getIndexStart().doubleValue() / fileInfo.getFileSize().doubleValue() * 100)));
            if ((fileInfo.getIndexStart() + fileInfo.getSeparateSize()) > fileInfo.getFileSize()) {
                fileInfo.setIndexEnd(fileInfo.getFileSize());
                fileInfo.setSeparateSize(fileInfo.getIndexEnd() - fileInfo.getIndexStart());
            } else {
                fileInfo.setIndexEnd(fileInfo.getIndexStart() + fileInfo.getSeparateSize());
            }

            //通知客户端上传完成
            session.sendMessage(new TextMessage(JSONObject.toJSONString(new WSResult(202, false, fileInfo, sign))));
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            FileUtil.closeFileChannel(fc);
            FileUtil.closeOutputStream(fos);
        }


    }

    /**
     * 收到消息
     *
     * @param session
     * @param message
     * @throws Exception
     */
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        long start = System.currentTimeMillis();
        //拿到客户端发来的信息
        JSONObject jsonObject = JSONObject.parseObject(message.getPayload());
        //操作类型
        int type = jsonObject.getInteger("type");
        //返回值
        WSResult wsResult;
        //客户端id
        String clientId = getClientId(session.getAttributes());
        log.info(jsonObject.toJSONString());

        switch (type) {

            case 1: //加载最近聊天列表

                boolean isSysUser = jsonObject.getBooleanValue("isSysUser");

                if (isSysUser) { //用户
                    wsResult = sysUserMsgListService.frendList(Long.valueOf(clientId));
                } else { //游客相关
                    wsResult = sysTouristMsgListService.friendList(clientId);
                }

                //将信息返回客户端
                session.sendMessage(new TextMessage(JSONObject.toJSONString(wsResult)));
                break;

            case 2: //加载聊天记录

                if (jsonObject.getBooleanValue("isSysUser")) { //用户
                    wsResult = sysUserMsgService.getMsgPageList(Long.valueOf(clientId), jsonObject);
                } else { //游客相关
                    wsResult = sysTouristMsgService.getMsgPageList(clientId, jsonObject);
                }

                //将信息返回客户端
                session.sendMessage(new TextMessage(JSONObject.toJSONString(wsResult)));
                break;

            case 3: //发消息

                String sign = jsonObject.getString("sign");
                String toUserId = jsonObject.getString("toUserId");
                MsgInfo msgInfo = JSON.parseObject(JSONObject.toJSONString(jsonObject), MsgInfo.class);

                if (MsgUtils.isSysUser(clientId, toUserId)) { //用户相关模块
                    SysUserMsg sysUserMsg = sysUserMsgService.sendMsg(Long.valueOf(clientId), Long.valueOf(toUserId), msgInfo, sign, uploadFileInfoMap);
                    if (Objects.isNull(sysUserMsg)) { //文件信息处理完毕，通知客户端可以开始发送文件
                        session.sendMessage(new TextMessage(JSONObject.toJSONString(new WSResult(201, true, sign))));
                    } else { //文字消息
                        //将消息发送给对方
                        send(toUserId, new TextMessage(JSONObject.toJSONString(new WSResult(4, true, sysUserMsg))));
                        //告知发送方发送成功
                        session.sendMessage(new TextMessage(JSONObject.toJSONString(new WSResult(3, true, sign, sysUserMsg.getId(), sysUserMsg.getCreateDate()))));
                    }
                } else { //游客相关模块

                    SysTouristMsg sysTouristMsg = sysTouristMsgService.sendMsg(clientId, toUserId, msgInfo, sign, uploadFileInfoMap);
                    if (Objects.isNull(sysTouristMsg)) { //文件信息处理完毕，通知客户端可以开始发送文件
                        session.sendMessage(new TextMessage(JSONObject.toJSONString(new WSResult(201, false, sign))));
                    } else { //文字消息, 将消息发送给对方, 然后告知发送方发送成功
                        send(toUserId, new TextMessage(JSONObject.toJSONString(new WSResult(4, sysTouristMsg))));
                        session.sendMessage(new TextMessage(JSONObject.toJSONString(new WSResult(3, false, sign, sysTouristMsg.getId(), sysTouristMsg.getCreateDate()))));
                    }
                }

                break;

            case 5: //获取通讯录

                wsResult = sysUserService.getMailList(Long.valueOf(clientId));
                session.sendMessage(new TextMessage(JSONObject.toJSONString(wsResult)));
                break;

            case 6: //根据好友id获取该好友详情

                wsResult = sysUserService.getMailFriendDetail(jsonObject.getLong("data"));
                session.sendMessage(new TextMessage(JSONObject.toJSONString(wsResult)));
                break;

            case 7: //在某个好友的详细信息页，点击"发消息"按钮

                //为用户在左侧添加与该好友的聊天列表
                wsResult = sysUserMsgListService.clickSendBtn(Long.valueOf(clientId), jsonObject);
                session.sendMessage(new TextMessage(JSONObject.toJSONString(wsResult)));
                break;

            case 10: //将当前客户端设置为:没有跟任何人聊天

                redisService.hset(Constants.USER_CHAT_WITH_WHO, clientId, "");
                break;

            case 11: //撤回消息

                toUserId = jsonObject.getString("toUserId"); //对方id
                isSysUser = MsgUtils.isSysUser(clientId, toUserId);

                //判断该消息是用户相关还是游客相关
                if (isSysUser) {
                    sysUserMsgService.recallMsg(jsonObject.getLong("id"));
                } else {
                    sysTouristMsgService.recallMsg(jsonObject.getLong("id"));
                }

                //判断对方是否正在呆在本客户端的聊天页面上
                if (msgUtils.isChatWithClient(clientId, toUserId)) {
                    send(toUserId, new TextMessage(jsonObject.toJSONString())); //通知对方这条消息已经撤回
                }

                session.sendMessage(new TextMessage(jsonObject.toJSONString())); //通知客户端撤回成功

                //判断撤回的是否是最后一条消息
                if (jsonObject.getBooleanValue("isLast")) { //是，则刷新双方左侧的最近消息列表

                    if (isSysUser) {
                        //向对方推送最新列表
                        wsResult = sysUserMsgListService.frendList(Long.valueOf(toUserId));
                        send(toUserId, new TextMessage(JSONObject.toJSONString(wsResult)));
                        //向本客户端推送最新列表
                        wsResult = sysUserMsgListService.frendList(Long.valueOf(clientId));
                        session.sendMessage(new TextMessage(JSONObject.toJSONString(wsResult)));
                    } else {
                        //向对方推送最新列表
                        wsResult = sysTouristMsgListService.friendList(toUserId);
                        send(toUserId, new TextMessage(JSONObject.toJSONString(wsResult)));
                        //向本客户端推送最新列表
                        wsResult = sysTouristMsgListService.friendList(clientId);
                        session.sendMessage(new TextMessage(JSONObject.toJSONString(wsResult)));
                    }

                }

                //判断撤回的消息是否是文件或图片,判断文件是否还有人引用，没有则删除
                String msgType = jsonObject.getString("msgType");
                if (msgType != null && !StringUtils.equals(msgType, "text")) { //是

                    String contentMD5 = jsonObject.getString("contentMD5");
                    synchronized (lock) {
                        String fileUseCountStr = redisService.hget(Constants.SAVE_FILE_MD5_USE_COUNT, contentMD5);
                        int fileUseCount = fileUseCountStr == null ? 0 : Integer.valueOf(fileUseCountStr);
                        if (fileUseCount - 1 <= 0) { //文件或缩略图已经没人使用,删除
                            if (new File(serverConfig.getSaveFileParentPath()
                                    .concat(redisService.hget(Constants.SAVE_FILE_MD5_MAP, contentMD5))).delete()) {
                                redisService.hashDeleteByKey(Constants.SAVE_FILE_MD5_USE_COUNT, contentMD5);
                                redisService.hashDeleteByKey(Constants.SAVE_FILE_MD5_MAP, contentMD5);
                                log.info("服务器上的原图或者文件已经删除");

                                if (ImageUtil.isImage(msgType)) { //图片, 删除原图
                                    if (new File(serverConfig.getSaveFileParentPath()
                                            .concat(redisService.hget(Constants.SAVE_FILE_MD5_MAP, contentMD5.concat("-origin")))).delete()){
                                        redisService.hashDeleteByKey(Constants.SAVE_FILE_MD5_MAP, contentMD5.concat("-origin"));
                                        log.info("服务器上的缩略图已经删除");
                                    }
                                }
                            }
                        }

                    }

                }
                break;

            case 12: //删除最近聊天列表记录, 然后向客户端推送新的最近聊天列表

                String anotherUserId = jsonObject.getString("anotherUserId");
                isSysUser = MsgUtils.isSysUser(clientId, anotherUserId);

                if (isSysUser) { //用户相关
                    sysUserMsgListService.remove(jsonObject.getLong("id"));
                    wsResult = sysUserMsgListService.frendList(Long.valueOf(clientId));
                } else { //游客相关
                    sysTouristMsgListService.remove(jsonObject.getLong("id"));
                    wsResult = sysTouristMsgListService.friendList(clientId);
                }

                String userChatWithWho = redisService.hget(Constants.USER_CHAT_WITH_WHO, clientId);
                //判断客户端当前是否在删除的那个人的页面
                if (StringUtils.equals(userChatWithWho, anotherUserId)) { //是,则置为当前没有在任何人的聊天页面上
                    redisService.hset(Constants.USER_CHAT_WITH_WHO, clientId, "");
                }

                session.sendMessage(new TextMessage(JSONObject.toJSONString(wsResult))); //向客户端发送最新的左侧列表
                session.sendMessage(new TextMessage(jsonObject.toJSONString())); //通知客户端删除成功
                break;

            case 13: //删除聊天记录

                isSysUser = MsgUtils.isSysUser(clientId, jsonObject.getString("anotherUserId"));
                List<SysUserMsg> msgList = JSONArray.parseArray(JSONObject.toJSONString(jsonObject.getJSONArray("data")), SysUserMsg.class);

                if (isSysUser) { //用户相关
                    msgList = sysUserMsgService.removeMsgList(msgList, Long.valueOf(clientId));
                } else { //游客相关
                    msgList = sysTouristMsgService.removeMsgList(msgList, clientId);
                }

                //如果删除的是俩人最后一条消息，刷新左侧列表
                if (jsonObject.getBooleanValue("isLast")) {
                    if (isSysUser) {
                        wsResult = sysUserMsgListService.frendList(Long.valueOf(clientId));
                    } else {
                        wsResult = sysTouristMsgListService.friendList(clientId);
                    }
                    session.sendMessage(new TextMessage(JSONObject.toJSONString(wsResult)));
                }
                session.sendMessage(new TextMessage(jsonObject.toJSONString()));

                //删除文件
                for (SysUserMsg sysUserMsg : msgList) {
                    //判断删除的消息是否双方都删除了,是否是文件或图片,文件是否还有人引用，以上都满足则删除服务器上的文件
                    if (sysUserMsg.getIsBothDelete() && sysUserMsg.getMsgType() != null && !StringUtils.equals(sysUserMsg.getMsgType(), "text")) {

                        synchronized (lock) {
                            String fileUseCountStr = redisService.hget(Constants.SAVE_FILE_MD5_USE_COUNT, sysUserMsg.getContentMD5());
                            int fileUseCount = fileUseCountStr == null ? 0 : Integer.valueOf(fileUseCountStr);
                            if (fileUseCount - 1 <= 0) { //文件或缩略图已经没人使用,删除
                                if (new File(serverConfig.getSaveFileParentPath()
                                        .concat(redisService.hget(Constants.SAVE_FILE_MD5_MAP, sysUserMsg.getContentMD5()))).delete()) {
                                    redisService.hashDeleteByKey(Constants.SAVE_FILE_MD5_USE_COUNT, sysUserMsg.getContentMD5());
                                    redisService.hashDeleteByKey(Constants.SAVE_FILE_MD5_MAP, sysUserMsg.getContentMD5());
                                    log.info("服务器上的缩略图或者文件已经删除");

                                    if (ImageUtil.isImage(sysUserMsg.getMsgType())) { //图片, 删除原图
                                        if (new File(serverConfig.getSaveFileParentPath()
                                                .concat(redisService.hget(Constants.SAVE_FILE_MD5_MAP, sysUserMsg.getContentMD5().concat("-origin")))).delete()) {
                                            redisService.hashDeleteByKey(Constants.SAVE_FILE_MD5_MAP, sysUserMsg.getContentMD5().concat("-origin"));
                                            log.info("服务器上的原图已经删除");
                                        }
                                    }
                                }
                            }

                        }

                    }
                }
                break;

            case 203: //继续上传文件

                sign = jsonObject.getString("sign");
                session.sendMessage(new TextMessage(JSONObject.toJSONString(
                        new WSResult(202, false, uploadFileInfoMap.get(clientId).get(sign), sign))));
                break;

            case 204: //取消上传文件

                sign = jsonObject.getString("sign");
                uploadFileInfoMap.get(clientId).get(sign).fileUploadComplete();
                uploadFileInfoMap.get(clientId).remove(sign);
                break;

            case 401: //客户端主动断开连接

                closeIfUploadingFile(clientId);  //若客户端有正在传输的文件，结束传输并删除硬盘上已存在的残缺文件
                if (session.isOpen()) {
                    session.close(); //关闭连接
                }
                socketCache.remove(clientId); //清理缓存

                //若是游客，将游客的聊天记录清空
                if (!NumUtil.isInteger(clientId)) {

                    //获取跟该游客聊过天的客服列表
                    List<String> idList = sysTouristMsgListService.getIdListWhoChatWithHim(clientId);

                    //清空与该游客有关的聊天信息
                    sysTouristMsgListService.clearMsgInfo(clientId);

                    //跟该游客聊过天的客服们  需要刷新最近聊天列表
                    for (String id : idList) {
                        WebSocketSession customerServiceSession = socketCache.get(id);
                        //判断该客服是否在线
                        if (!Objects.isNull(customerServiceSession) && customerServiceSession.isOpen()) {
                            wsResult = sysTouristMsgListService.friendList(id); //为该客服加载最近消息列表
                            send(id, new TextMessage(JSONObject.toJSONString(wsResult)));
                        }
                    }
                }
                break;

            case 402: //客户端点击左上角刷新按钮

                closeIfUploadingFile(clientId);  //若该客户端有正在传输的文件，结束传输并删除硬盘上已存在的残缺文件
                break;

        }

        log.info("整个请求结束,耗时 {}ms", System.currentTimeMillis()-start);
    }

    /**
     * 是否支持切片传输
     *
     * @return
     */
    public boolean supportsPartialMessages() {
        return false;
    }

    /**
     * 给某人发送消息
     *
     * @param token
     * @param message
     * @return
     */
    public boolean send(String token, TextMessage message) {
        if (socketCache.containsKey(token)) {
            //说明对方在线，直接发过去
            return send(socketCache.get(token), message);
        }
        //不在线则不发
        return false;
    }

    /**
     * 群发
     *
     * @param message
     */
    void sendAll(TextMessage message) {
        if (message == null) return;
        for (WebSocketSession webSocketSession : socketCache.values()) {
            send(webSocketSession, message);
        }
    }

    /**
     * 发消息
     *
     * @param webSocketSession
     * @param message
     * @return
     */
    private boolean send(WebSocketSession webSocketSession, TextMessage message) {
        //是用户
        String clientId = (String) webSocketSession.getAttributes().get("sysUserId");
        clientId = clientId == null ? (String) webSocketSession.getAttributes().get("touristId") : clientId;
        if (clientId == null) {
            return false;
        }
        try {
            synchronized (webSocketSession) {
                if (webSocketSession.isOpen()) {
                    webSocketSession.sendMessage(message);
                }
            }
            return true;
        } catch (IOException e) {
            log.error("WebSocket 数据发送异常 !", e);
            return false;
        }
    }

    /**
     * 如果该用户在线，将新的信息给用户发一下
     *
     * @param sysUser
     */
    public void sendNewPersonInfoToUser(SysUser sysUser) {
        if (socketCache.containsKey(sysUser.getId().toString())) {
            Map<String, String> resultMap = new HashMap<>();
            resultMap.put("userName", sysUser.getUserName());
            if (StringUtils.isNotEmpty(sysUser.getImgPath())) {
                resultMap.put("imgPath", serverConfig.getImageUrl(sysUser.getImgPath()));
            }
            send(sysUser.getId().toString(), new TextMessage(JSONObject.toJSONString(new WSResult(8, resultMap))));
        }
    }

    /**
     * 若用户要完全断开，将用户名下缓存的文件信息清除，并将已经保存到硬盘上的残缺文件删除
     *
     * @param clientId
     */
    private synchronized void closeIfUploadingFile(String clientId) {
        Map<String, FileInfo> fileInfoMap = uploadFileInfoMap.get(clientId);
        if (fileInfoMap != null && !fileInfoMap.isEmpty()) {
            for (Map.Entry<String, FileInfo> fileInfoEntry : fileInfoMap.entrySet()) {
                fileInfoEntry.getValue().fileUploadComplete();
            }
            uploadFileInfoMap.remove(clientId);
        }
    }

    /**
     * 将文件信息保存到该客户端名下
     *
     * @param clientId
     * @param sign
     * @param fileInfo
     */
    public void addToUploadFileInfoMap(String clientId, String sign, FileInfo fileInfo) {
        synchronized (this) {
            //存在则添加，不存在则创建
            Map<String, FileInfo> clientUploadFileInfo = uploadFileInfoMap.getOrDefault(clientId, new ConcurrentHashMap<>());
            clientUploadFileInfo.put(sign, fileInfo);
            uploadFileInfoMap.put(clientId, clientUploadFileInfo);
        }
    }

    /**
     * 获取客户端id
     *
     * @param sessionAttributes
     * @return
     */
    public String getClientId(Map<String, Object> sessionAttributes) {
        Object touristId = sessionAttributes.get("touristId");
        if (Objects.isNull(touristId)) { //用户
            return sessionAttributes.get("sysUserId").toString();
        } else { //游客
            return touristId.toString();
        }
    }

    /**
     * 连接关闭后的处理
     *
     * @param sessionAttributes
     * @param needNoticeFriend  是否需要通知好友客户端已下线
     */
    public void doAfterWebSocketClose(Map<String, Object> sessionAttributes, boolean needNoticeFriend) {

        if (isServletStop) { //若是在项目关闭阶段, 则不做任何操作
            return;
        }

        //若本次连接不是为了生成uuid来的，才做操作
        if (!sessionAttributes.containsKey("needCreateUUID")) {

            String clientId = getClientId(sessionAttributes);

            //更新缓存，该客户端现在没有跟任何人聊天
            redisService.hashDeleteByKey(Constants.USER_CHAT_WITH_WHO, clientId);

            //判断退出的客户端是否是用户
            if (NumUtil.isInteger(clientId)) { //是
                sysUserService.setUserStatus(Long.valueOf(clientId), (byte) 0); //设置用户的登陆状态为离线
                if (needNoticeFriend) {
                    noticeFriendOnline(clientId, (byte) 0); //通知他的好友他已经下线
                }
            }

            socketCache.remove(clientId); //将连接移除
            log.info("客户端 {} 已断开, 剩余在线数为: {}", clientId, socketCache.size());
        }
    }

    public Map<String, WebSocketSession> getSocketCache() {
        return socketCache;
    }

    public void setIsServletStop(boolean isServletStop) {
        this.isServletStop = isServletStop;
    }
}
