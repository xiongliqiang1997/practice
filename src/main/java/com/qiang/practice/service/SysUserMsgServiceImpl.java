package com.qiang.practice.service;

import com.alibaba.fastjson.JSONObject;
import com.qiang.practice.base.Constants;
import com.qiang.practice.config.ServerConfig;
import com.qiang.practice.mapper.SysUserMsgListMapper;
import com.qiang.practice.mapper.SysUserMsgMapper;
import com.qiang.practice.model.FileInfo;
import com.qiang.practice.model.SysUserMsg;
import com.qiang.practice.model.vo.MsgInfo;
import com.qiang.practice.model.vo.SysUserMsgAndDateVO;
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
 * @Date: 2019/9/3
 * @Description: TODO
 */
@SuppressWarnings("all")
@Transactional
@Service
public class SysUserMsgServiceImpl implements SysUserMsgService {
    @Autowired
    private SysUserMsgListMapper sysUserMsgListMapper;
    @Autowired
    private SysUserMsgMapper sysUserMsgMapper;
    @Autowired
    private WebSocketServer webSocketServer;
    @Autowired
    private RedisService redisService;
    @Autowired
    private ServerConfig serverConfig;
    @Autowired
    private SysUserMsgListService sysUserMsgListService;
    @Autowired
    private MsgUtils msgUtils;

    @Override
    public WSResult getMsgPageList(Long sysUserId, JSONObject jsonObject) {
        //读取参数
        Date msgDateBottomLine = DateUtil.newDateIfNull(jsonObject.getJSONObject("data").getDate("msgDateBottomLine"));
        Long anotherUserId = jsonObject.getJSONObject("data").getLong("anotherUserId");

        //根据好友id分页获取与好友的消息记录
        List<SysUserMsg> sysUserMsgList = sysUserMsgMapper.getMsgPageList(msgDateBottomLine,
                sysUserId,
                anotherUserId,
                10,
                180);

        //返回值准备
        SysUserMsgAndDateVO sysUserMsgAndDateVO = new SysUserMsgAndDateVO();

        if (!sysUserMsgList.isEmpty()) {
            //处理图片和文件消息的加载url
            for (SysUserMsg sysUserMsg : sysUserMsgList) {
                if (!StringUtils.equals("text", sysUserMsg.getMsgType())) {
                    String msg = sysUserMsg.getMsg();
                    if (!Objects.isNull(msg)) {
                        String[] msgArray = msg.split("\\|");
                        if (StringUtils.equals("image", sysUserMsg.getMsgType())) {
                            sysUserMsg.setOriginUrl(serverConfig.getImageUrl(msgArray[0]));
                            sysUserMsg.setFileUrl(serverConfig.getImageUrl(msgArray[1]));
                        } else {
                            sysUserMsg.setFileName(msgArray[0]);
                            sysUserMsg.setFileUrl(serverConfig.getFileDownloadUrl(msgArray[1]));
                        }
                        sysUserMsg.setContentMD5(msgArray[2]);
                    }
                }
            }

            sysUserMsgAndDateVO.setSysUserMsgList(sysUserMsgList);
            sysUserMsgAndDateVO.setLastMsgDate(sysUserMsgList.get(0).getCreateDate());

            //将最后一条消息的时间进行格式化并返回
            sysUserMsgAndDateVO.setLastMsgDateStr(DateUtil.getAllTime(sysUserMsgAndDateVO.getLastMsgDate()));

            //将对该好友的未读消息全部置为已读
            sysUserMsgMapper.setHasReadedByTwoUserId(sysUserId, anotherUserId);
        }

        //更新缓存，保存该用户现在是待在谁的聊天页面上
        redisService.hset(Constants.USER_CHAT_WITH_WHO, sysUserId.toString(), anotherUserId.toString());
        return new WSResult(2, true, sysUserMsgAndDateVO);
    }

    @Override
    public SysUserMsg sendMsg(Long sysUserId, Long toUserId, MsgInfo msgInfo, String sign, Map<String, Map<String, FileInfo>> uploadFileInfoMap) {
        long start = System.currentTimeMillis();
        Date createDate = new Date();

        //判断消息类型
        if (MsgUtils.isTextMsg(msgInfo.getMsgType())) { //文字消息

            //准备消息实体
            SysUserMsg sysUserMsg = new SysUserMsg();
            sysUserMsg.setIsSysUser(true);
            sysUserMsg.setFromUserId(sysUserId);
            sysUserMsg.setToUserId(toUserId);
            sysUserMsg.setCreateDate(createDate);
            sysUserMsg.setIsRecall((byte) 0);
            sysUserMsg.setIsRead(msgUtils.getMsgIsRead(sysUserId.toString(), toUserId.toString()));
            sysUserMsg.setMsgType(msgInfo.getMsgType());
            sysUserMsg.setMsg(msgInfo.getMsg());
            sysUserMsgMapper.insert(sysUserMsg); //将消息存进数据库
            sysUserMsgListService.insertAfterSendMsg(sysUserId, toUserId, createDate); //更新最近聊天列表
            return sysUserMsg;

        } else { //文件消息

            //将文件的属性信息放进缓存，以便接收文件时使用
            FileInfo fileInfo = new FileInfo();

            if (MsgUtils.isImageMsg(msgInfo.getMsgType())) { //图片消息
                fileInfo.setFileType("image");
            } else { //其他文件
                fileInfo.setFileType("file");
            }

            fileInfo.setSeparateSize(Math.min(msgInfo.getSeparateSize() * 1024, msgInfo.getFileSize()));
            fileInfo.setIndexStart(0L);
            fileInfo.setToUserId(toUserId.toString());
            fileInfo.setIsSysUser(true);
            fileInfo.setSign(sign);
            fileInfo.setFileName(msgInfo.getFileName());
            fileInfo.setFileSize(msgInfo.getFileSize());
            fileInfo.setCreateDate(createDate);
            webSocketServer.addToUploadFileInfoMap(sysUserId.toString(), sign, fileInfo);
            return null;
        }
    }

    @Override
    public void updateAllMsgToHasReaded(Long clientId, Long anotherUserId) {
        sysUserMsgMapper.setHasReadedByTwoUserId(clientId, anotherUserId);
    }

    @Override
    public void insert(SysUserMsg sysUserMsg) {
        sysUserMsgMapper.insert(sysUserMsg);
    }

    @Override
    public void recallMsg(Long id) {
        sysUserMsgMapper.recallMsg(id); //撤回消息
    }

    @Override
    public List<SysUserMsg> removeMsgList(List<SysUserMsg> msgList, Long sysUserId) {
        List<Boolean> isBothDeleteList = sysUserMsgMapper.isBothDeleteList(msgList);
        sysUserMsgMapper.removeMsgList(msgList, sysUserId);
        int index = 0;
        for (SysUserMsg sysUserMsg : msgList) {
            sysUserMsg.setIsBothDelete(isBothDeleteList.get(index));
        }
        return msgList;
    }

}
