package com.qiang.practice.mapper;

import com.qiang.practice.model.SysUser;
import com.qiang.practice.model.vo.SysUserAndOrgVO;
import com.qiang.practice.model.vo.UpdatePwdObj;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface SysUserMapper {
    int removeById(Long id);

    int addOneSysOrg(SysUser record);

    int insertSelective(SysUser record);

    SysUser selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(SysUser record);

    int updateByPrimaryKey(SysUser record);

    /**
     * 哪些条件不为null就根据哪些条件查，全为null则查所有
     * @param sysOrgId
     * @return
     */
    List<SysUser> getValidSysUserByCondition(@Param(value = "sysOrgId") Long sysOrgId,
                                            @Param(value = "userName") String userName,
                                            @Param(value = "userSex") Integer userSex,
                                            @Param(value = "loginName") String loginName,
                                            @Param(value = "haveAuthority") Integer haveAuthority);

    /**
     * 将当前目前的最大排序号查出来
     * @return
     */
    Integer getMaxOrderby();

    /**
     * 批量删除用户,将有效字段置为0
     * @param idList
     * @return
     */
    int updateIsValidToZeroByIdList(@Param(value = "idList") List<Long> idList);

    /**
     * 根据条件分页查询对应的用户
     * @param paramMap
     * @return
     */
    List<Map<String, Object>> getByPage(Map<String, Object> paramMap);

    /**
     * 根据组织机构id查询该机构所有直属员工，若id为空，则全查
     * @param orgId
     * @return
     */
    List<Map<String, Object>> getFirstSonUserByOrgId(@Param(value = "orgId") Long orgId);

    /**
     * 保存排序数据
     * @param paramMap
     * @return
     */
    int sort(Map<String, Object> paramMap);

    /**
     * 根据机构id将每个机构下的员工都删除
     * @param orgId
     */
    void updateIsValidToZeroByOrgId(Long orgId);

    /**
     * 获取该登录名的数量
     * @param loginName
     * @return
     */
    int getLoginNameCount(@Param(value = "loginName") String loginName,
                          @Param(value = "id") Long id);

    /**
     * 根据id获取单个用户的详细信息
     * @param id
     * @return
     */
    SysUserAndOrgVO getById(Long id);

    /**
     * 根据用户名和密码查询该用户信息
     * @return
     */
    SysUser getByLoginNameAndLoginPwd(@Param(value = "loginName") String loginName,
                                      @Param(value = "loginPwd") String loginPwd);

    /**
     * 获取客服id
     * @return
     */
    List<Long> getCustomerServiceIdList();

    /**
     * 获取用户的通讯录
     * @return
     */
    List<SysUser> getMailList(Long sysUserId);

    /**
     * 验证旧密码是否正确
     * @param updatePwdObj
     * @return
     */
    boolean checkOldPassword(UpdatePwdObj updatePwdObj);

    /**
     * 修改密码
     * @param updatePwdObj
     */
    void updatePassword(UpdatePwdObj updatePwdObj);
}