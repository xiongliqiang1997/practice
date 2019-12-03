package com.qiang.practice.service;

import com.alibaba.fastjson.JSONObject;
import com.qiang.practice.base.Constants;
import com.qiang.practice.config.ServerConfig;
import com.qiang.practice.mapper.SysUserMsgListMapper;
import com.qiang.practice.mapper.SysUserMsgMapper;
import com.qiang.practice.model.SysUserMsgList;
import com.qiang.practice.model.vo.SysUserMsgListUserNameMsgVO;
import com.qiang.practice.utils.DateUtil;
import com.qiang.practice.utils.response.WSResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * @Author: CLQ
 * @Date: 2019/9/5
 * @Description: TODO
 */
@Transactional
@Service
public class SysUserMsgListServiceImpl implements SysUserMsgListService {
    @Autowired
    private SysUserMsgListMapper sysUserMsgListMapper;
    @Autowired
    private SysUserMsgMapper sysUserMsgMapper;
    @Autowired
    private RedisService redisService;
    @Autowired
    private ServerConfig serverConfig;

    @Override
    public WSResult frendList(Long sysUserId) {
        //获取有过消息往来的朋友列表
        List<SysUserMsgListUserNameMsgVO> sysUserMsgListAndMsgVOList = sysUserMsgListMapper.frendList(sysUserId);

        for (SysUserMsgListUserNameMsgVO sysUserMsgListUserNameMsgVO : sysUserMsgListAndMsgVOList) {

            //为好友头像图片Url拼上前缀
            sysUserMsgListUserNameMsgVO.setImgPath(serverConfig.getImageUrl(sysUserMsgListUserNameMsgVO.getImgPath()));
            if (sysUserMsgListUserNameMsgVO.getLastMsgDate() != null) {
                //对最后一条消息的发送时间进行格式化
                sysUserMsgListUserNameMsgVO.setLastMsgDateStr(DateUtil.getPartTime(sysUserMsgListUserNameMsgVO.getLastMsgDate()));
            }

        }
        return new WSResult(1, true, sysUserMsgListAndMsgVOList);
    }

    @Override
    public WSResult clickSendBtn(Long sysUserId, JSONObject jsonObject) {
        Long anotherUserId = jsonObject.getLong("data");
        assert anotherUserId != null;
        //聊天列表中没有对方，添加
        SysUserMsgList sysUserMsgList = new SysUserMsgList();
        sysUserMsgList.setSysUser(sysUserId);
        sysUserMsgList.setAnotherUserId(anotherUserId);
        //如果两人从来没有聊过，则设置最后一次聊天时间为当前时间
        //如果两人聊过，则设置最后一次聊天时间为最后一次聊天的时间
        sysUserMsgList.setLastMsgDate(sysUserMsgMapper.getLastMsgDate(sysUserId, anotherUserId));
        sysUserMsgListMapper.insertIfAbsent(sysUserMsgList);
        //修改redis，用户现在正在跟这个人聊天
        redisService.hset(Constants.USER_CHAT_WITH_WHO, sysUserId.toString(), anotherUserId.toString());
        return new WSResult(7, null);
    }

    @Override
    public void remove(Long id) {
        sysUserMsgListMapper.deleteByPrimaryKey(id);
    }

    @Override
    public void insertAfterSendMsg(Long sysUserId, Long toUserId, Date lastMsgDate) {
        //如果现在双方的聊天列表中没有对方,则增加,并将双方的最后消息时间置为最新的
        SysUserMsgList sysUserMsgList = new SysUserMsgList();
        sysUserMsgList.setSysUser(sysUserId);
        sysUserMsgList.setAnotherUserId(toUserId);
        sysUserMsgList.setLastMsgDate(lastMsgDate);
        sysUserMsgListMapper.insertAfterSendMsg(sysUserMsgList);
    }
}
