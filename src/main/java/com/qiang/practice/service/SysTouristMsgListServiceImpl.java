package com.qiang.practice.service;

import com.qiang.practice.config.ServerConfig;
import com.qiang.practice.mapper.SysTouristMsgListMapper;
import com.qiang.practice.model.SysTouristMsgList;
import com.qiang.practice.model.vo.SysTouristMsgListUserNameMsgVO;
import com.qiang.practice.utils.DateUtil;
import com.qiang.practice.utils.NumUtil;
import com.qiang.practice.utils.response.WSResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * @Author: CLQ
 * @Date: 2019/9/5
 * @Description: TODO
 */
@Transactional
@Service
public class SysTouristMsgListServiceImpl implements SysTouristMsgListService {
    @Autowired
    private SysTouristMsgListMapper sysTouristMsgListMapper;
    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private ServerConfig serverConfig;

    @Override
    public void createChatListWithCustomerService(String clientId) {
        //获取客服id列表
        List<Long> customerServiceIdList = sysUserService.getCustomerServiceIdList();
        List<SysTouristMsgList> sysTouristMsgLists = new ArrayList<>();
        for (Long customerServiceId : customerServiceIdList) {
            SysTouristMsgList sysTouristMsgList = new SysTouristMsgList();
            sysTouristMsgList.setAnotherUserId(customerServiceId.toString());
            sysTouristMsgList.setSysUser(clientId);
            sysTouristMsgLists.add(sysTouristMsgList);
        }
        sysTouristMsgListMapper.batchInsertIfAbsent(sysTouristMsgLists);
    }

    @Override
    public WSResult friendList(String clientId) {
        //获取最近聊天列表
        List<SysTouristMsgListUserNameMsgVO> sysTouristMsgListUserNameMsgVOList
                = sysTouristMsgListMapper.friendList(clientId, NumUtil.isInteger(clientId));

        for (SysTouristMsgListUserNameMsgVO sysTouristMsgListUserNameMsgVO : sysTouristMsgListUserNameMsgVOList) {

            //为客服头像图片Url拼上前缀
            if (!Objects.isNull(sysTouristMsgListUserNameMsgVO.getImgPath())) {
                sysTouristMsgListUserNameMsgVO.setImgPath(serverConfig.getImageUrl(sysTouristMsgListUserNameMsgVO.getImgPath()));
            }

            //对最后一条消息的发送时间进行格式化
            if (sysTouristMsgListUserNameMsgVO.getLastMsgDate() != null) {
                sysTouristMsgListUserNameMsgVO.setLastMsgDateStr(DateUtil.getPartTime(sysTouristMsgListUserNameMsgVO.getLastMsgDate()));
            }

        }
        return new WSResult(1, false, sysTouristMsgListUserNameMsgVOList);
    }

    @Override
    public void clearMsgInfo(String clientId) {
        sysTouristMsgListMapper.clearMsgInfo(clientId); //将游客的最近聊天列表和聊天记录清空
    }

    @Override
    public List<String> getIdListWhoChatWithHim(String clientId) {
        return sysTouristMsgListMapper.getIdListWhoChatWithHim(clientId);
    }

    @Override
    public void remove(Long id) {
        sysTouristMsgListMapper.deleteByPrimaryKey(id);
    }

    @Override
    public void insertAfterSendMsg(String clientId, String toUserId, Date lastMsgDate) {
        //如果现在双方的聊天列表中没有对方,则增加,并将双方的最后消息时间置为最新的
        SysTouristMsgList sysTouristMsgList = new SysTouristMsgList();
        sysTouristMsgList.setSysUser(clientId);
        sysTouristMsgList.setAnotherUserId(toUserId);
        sysTouristMsgList.setLastMsgDate(lastMsgDate);
        sysTouristMsgListMapper.insertAfterSendMsg(sysTouristMsgList);
    }
}
