package com.qiang.practice.service;

import com.alibaba.fastjson.JSONObject;
import com.qiang.practice.base.Constants;
import com.qiang.practice.config.ServerConfig;
import com.qiang.practice.mapper.SysTouristMsgListMapper;
import com.qiang.practice.mapper.SysTouristMsgMapper;
import com.qiang.practice.model.FileInfo;
import com.qiang.practice.model.SysTouristMsg;
import com.qiang.practice.model.SysUserMsg;
import com.qiang.practice.model.vo.MsgInfo;
import com.qiang.practice.model.vo.SysTouristMsgAndDateVO;
import com.qiang.practice.utils.DateUtil;
import com.qiang.practice.utils.MsgUtils;
import com.qiang.practice.utils.response.WSResult;
import com.qiang.practice.websocket.WebSocketServer;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @Author: CLQ
 * @Date: 2019/9/6
 * @Description: TODO
 */
@SuppressWarnings("all")
@Transactional
@Service
public class SysTouristMsgServiceImpl implements SysTouristMsgService {
    @Autowired
    private SysTouristMsgMapper sysTouristMsgMapper;
    @Autowired
    private SysTouristMsgListMapper sysTouristMsgListMapper;
    @Autowired
    private WebSocketServer webSocketServer;
    @Autowired
    private RedisService redisService;
    @Autowired
    private ServerConfig serverConfig;
    @Autowired
    private SysTouristMsgListService sysTouristMsgListService;
    @Autowired
    private MsgUtils msgUtils;

    @Override
    public WSResult getMsgPageList(String clientId, JSONObject jsonObject) {
        //读取参数
        Date msgDateBottomLine = DateUtil.newDateIfNull(jsonObject.getJSONObject("data").getDate("msgDateBottomLine"));
        String anotherUserId = jsonObject.getJSONObject("data").getString("anotherUserId");
        //根据客服id分页获取与该客服的消息记录
        List<SysTouristMsg> sysTouristMsgList = sysTouristMsgMapper.getMsgPageList(
                msgDateBottomLine,
                clientId,
                anotherUserId,
                10,
                180);
        //返回值准备
        SysTouristMsgAndDateVO sysTouristMsgAndDateVO = new SysTouristMsgAndDateVO();

        if (!sysTouristMsgList.isEmpty()) {
            //处理图片和文件消息的加载url
            for (SysTouristMsg sysTouristMsg : sysTouristMsgList) {
                if (!StringUtils.equals("text", sysTouristMsg.getMsgType())) {
                    String msg = sysTouristMsg.getMsg();
                    if (!Objects.isNull(msg)) {
                        String[] msgArray = msg.split("\\|");
                        if (StringUtils.equals("image", sysTouristMsg.getMsgType())) {
                            sysTouristMsg.setOriginUrl(serverConfig.getImageUrl(msgArray[0]));
                            sysTouristMsg.setFileUrl(serverConfig.getImageUrl(msgArray[1]));
                        } else {
                            sysTouristMsg.setFileName(msgArray[0]);
                            sysTouristMsg.setFileUrl(serverConfig.getFileDownloadUrl(msgArray[1]));
                        }
                        sysTouristMsg.setContentMD5(msgArray[2]);
                    }
                }
            }

            sysTouristMsgAndDateVO.setSysUserMsgList(sysTouristMsgList);
            sysTouristMsgAndDateVO.setLastMsgDate(sysTouristMsgList.get(0).getCreateDate());
            //将最后一条消息的时间进行格式化并返回
            sysTouristMsgAndDateVO.setLastMsgDateStr(DateUtil.getAllTime(sysTouristMsgAndDateVO.getLastMsgDate()));
            //将与该客服的未读消息全部置为已读
            sysTouristMsgMapper.setHasReadedByTwoUserId(clientId, anotherUserId);
        }

        //更新缓存，保存该游客现在是待在哪个客服的聊天页面上
        redisService.hset(Constants.USER_CHAT_WITH_WHO, clientId, anotherUserId);
        return new WSResult(2, false, sysTouristMsgAndDateVO);
    }

    @Override
    public SysTouristMsg sendMsg(String clientId, String toUserId, MsgInfo msgInfo, String sign, Map<String, Map<String, FileInfo>> uploadFileInfoMap) {
        //参数准备
        long start = System.currentTimeMillis();
        Date createDate = new Date();

        //判断消息类型
        if (MsgUtils.isTextMsg(msgInfo.getMsgType())) { //文字消息

            //准备消息实体
            SysTouristMsg sysTouristMsg = new SysTouristMsg();
            sysTouristMsg.setFromUserId(clientId);
            sysTouristMsg.setToUserId(toUserId);
            sysTouristMsg.setCreateDate(createDate);
            sysTouristMsg.setIsRecall((byte) 0);
            sysTouristMsg.setIsRead(msgUtils.getMsgIsRead(clientId, toUserId));
            sysTouristMsg.setMsgType(msgInfo.getMsgType());
            sysTouristMsg.setMsg(msgInfo.getMsg());
            sysTouristMsgMapper.insert(sysTouristMsg); //将消息存进数据库
            sysTouristMsgListService.insertAfterSendMsg(clientId, toUserId, createDate);

            System.out.println("聊天消息处理时间：" + (System.currentTimeMillis() - start) + "ms");
            return sysTouristMsg;

        } else { //文件

            //将文件的属性信息放进缓存，以便接收文件时使用
            FileInfo fileInfo = new FileInfo();

            if (MsgUtils.isImageMsg(msgInfo.getMsgType())) { //图片
                fileInfo.setFileType("image");
            } else { //其他文件
                fileInfo.setFileType("file");
            }

            fileInfo.setSeparateSize(Math.min(msgInfo.getSeparateSize() * 1024, msgInfo.getFileSize()));
            fileInfo.setIndexStart(0L);
            fileInfo.setToUserId(toUserId.toString());
            fileInfo.setSign(sign);
            fileInfo.setFileName(msgInfo.getFileName());
            fileInfo.setFileSize(msgInfo.getFileSize());
            fileInfo.setCreateDate(createDate);
            webSocketServer.addToUploadFileInfoMap(clientId, sign, fileInfo);

            System.out.println("聊天消息处理时间：" + (System.currentTimeMillis() - start) + "ms");
            return null;
        }
    }

    @Override
    public void updateAllMsgToHasReaded(String clientId, String anotherUserId) {
        sysTouristMsgMapper.setHasReadedByTwoUserId(clientId, anotherUserId);
    }

    @Override
    public void insert(SysTouristMsg sysTouristMsg) {
        sysTouristMsgMapper.insert(sysTouristMsg);
    }

    @Override
    public void recallMsg(Long id) {
        sysTouristMsgMapper.recallMsg(id); //撤回消息
    }

    @Override
    public List<SysUserMsg> removeMsgList(List<SysUserMsg> msgList, String sysUserId) {
        List<Boolean> isBothDeleteList = sysTouristMsgMapper.isBothDeleteList(msgList);
        sysTouristMsgMapper.removeById(msgList, sysUserId);
        int index = 0;
        for (SysUserMsg sysUserMsg : msgList) {
            sysUserMsg.setIsBothDelete(isBothDeleteList.get(index));
        }
        return msgList;
    }
}
