package com.qiang.practice.service;

import com.qiang.practice.model.SysUser;
import com.qiang.practice.model.vo.UpdatePwdObj;
import com.qiang.practice.utils.request.LoginParam;
import com.qiang.practice.utils.response.R;
import com.qiang.practice.utils.response.Validator;
import com.qiang.practice.utils.response.WSResult;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * @Author: CLQ
 * @Date: 2019/7/9
 * @Description: TODO
 */
public interface SysUserService {
    /**
     * 分页获取有效的用户，若各查询条件不为null，则为分页且条件查询
     * @param paramMap
     * @return
     */
    R getByPage(Map<String, Object> paramMap);

    /**
     * 新增或编辑用户
     * @param sysUser
     * @return
     */
    R addOrUpdate(HttpServletRequest request, SysUser sysUser);

    /**
     * 根据id数组删除选中的用户
     * @param idList
     * @return
     */
    R removeByIdList(List<Long> idList);

    /**
     * 根据id查单个用户的详细信息
     * @param id
     * @return
     */
    R getById(Long id);

    /**
     * 根据组织机构id获得需要排序的数据
     * @param id
     * @return
     */
    R getSortData(Long id);

    /**
     * 如果这次数据有改动，则保存对用户的新排序
     * 若无改动，则不对数据库做任何操作
     * @param param
     * @return
     */
    R sort(Map<String, Object> param);

    /**
     * 查看登录名是否已存在
     * @param loginName
     * @return
     */
    Validator checkLoginNameExist(String loginName, Long id);

    /**
     * 登录
     * @param request
     * @param loginName
     * @return
     */
    R login(HttpServletRequest request, HttpServletResponse response, LoginParam loginName);

    /**
     * 获取用户
     * @param request
     * @return
     */
    R getByToken(HttpServletRequest request);

    /**
     * 退出登录
     * @param request
     * @return
     */
    R logout(HttpServletRequest request, HttpServletResponse response);

    /**
     * 修改密码
     * @param updatePwdObj
     * @return
     */
    R updatePassword(UpdatePwdObj updatePwdObj);

    /**
     * 获取通讯录
     * @param sysUserId
     * @return
     */
    WSResult getMailList(Long sysUserId);

    /**
     * 根据好友id获取该好友详情
     * @param sysUserId
     * @return
     */
    WSResult getMailFriendDetail(Long sysUserId);

    /**
     * 设置用户的登陆状态(0离线 1在线)
     * @param sysUserId
     * @param status
     */
    void setUserStatus(Long sysUserId, Byte status);

    /**
     * 获取客服id列表
     * @return
     */
    List<Long> getCustomerServiceIdList();
}
