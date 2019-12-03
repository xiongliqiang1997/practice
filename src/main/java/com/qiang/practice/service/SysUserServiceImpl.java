package com.qiang.practice.service;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.qiang.practice.base.Constants;
import com.qiang.practice.base.TipsConstants;
import com.qiang.practice.config.ServerConfig;
import com.qiang.practice.mapper.SysUserMapper;
import com.qiang.practice.mapper.SysUserStatusMapper;
import com.qiang.practice.model.SysUser;
import com.qiang.practice.model.SysUserStatus;
import com.qiang.practice.model.vo.SysUserAndOrgVO;
import com.qiang.practice.model.vo.UpdatePwdObj;
import com.qiang.practice.oauth.TokenGenerator;
import com.qiang.practice.utils.HttpContextUtils;
import com.qiang.practice.utils.MD5Util;
import com.qiang.practice.utils.NumUtil;
import com.qiang.practice.utils.request.LoginParam;
import com.qiang.practice.utils.response.R;
import com.qiang.practice.utils.response.RUtil;
import com.qiang.practice.utils.response.Validator;
import com.qiang.practice.utils.response.WSResult;
import com.qiang.practice.websocket.WebSocketServer;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @Author: CLQ
 * @Date: 2019/7/9
 * @Description: TODO
 */
@SuppressWarnings("all")
@Transactional
@Service
public class SysUserServiceImpl implements SysUserService {

    private final Map<String, SysUser> loginUserMap = new HashMap<>();

    @Autowired
    private TokenGenerator tokenGenerator;

    @Autowired
    private SysUserMapper sysUserMapper;
    @Autowired
    private SysUserStatusMapper sysUserStatusMapper;
    @Autowired
    private RedisService redisService;
    @Autowired
    private ServerConfig serverConfig;
    @Autowired
    private WebSocketServer webSocketServer;

    @Override
    public R getByPage(Map<String, Object> paramMap) {
        //使用分页插件
        PageHelper.startPage(Integer.valueOf(paramMap.get("page").toString()), Integer.valueOf(paramMap.get("pageSize").toString()));
        //对查询条件中的性别字段做处理
        paramMap = NumUtil.convertUserSexToInteger(paramMap);
        //根据条件分页查询对应的用户
        List<Map<String, Object>> userPageInfo = sysUserMapper.getByPage(paramMap);
        PageInfo<Map<String, Object>> pageInfo = new PageInfo<>(userPageInfo);
        return R.ok(pageInfo);
    }

    @Override
    public R addOrUpdate(HttpServletRequest request, SysUser sysUser) {
        if (!checkLoginNameExist(sysUser.getLoginName(), sysUser.getId()).isRes()) {
            request.setAttribute("notLog", 1);
            return R.error(TipsConstants.LOGIN_NAME_HAS_EXISTED);
        }
        int rows = 0;
        //如果id为null，表示为新增，否则为修改
        if (null == sysUser.getId()) {
            //密码加密
            sysUser.setLoginPwd(MD5Util.getSlatMD5(sysUser.getLoginPwd(), Constants.LOGIN_PWD_SLAT));
            request.setAttribute("logType", "新建");
            sysUser.setCreateDate(new Date());
            //先查一下目前的最大排序号, 然后在此最大排序号基础上+1
            sysUser.setOrderby(NumUtil.getOrderbyByMaxOrderby(sysUserMapper.getMaxOrderby()));
            //新增用户
            rows = sysUserMapper.addOneSysOrg(sysUser);
            //为用户新增在线状态属性
            SysUserStatus sysUserStatus = new SysUserStatus();
            sysUserStatus.setSysUser(sysUser.getId());
            sysUserStatus.setStatus((byte) 0);
            sysUserStatusMapper.insert(sysUserStatus);
        } else {
            request.setAttribute("logType", "编辑");
            //如果密码不为空，将密码进行加密
            if (StringUtils.isNotEmpty(sysUser.getLoginPwd())) {
                sysUser.setLoginPwd(MD5Util.getSlatMD5(sysUser.getLoginPwd(), Constants.LOGIN_PWD_SLAT));
            }
            //修改操作，设置修改时间
            sysUser.setUpdateDate(new Date());
            //修改用户信息（只修改已经修改过的字段，没有改的字段不做操作）
            rows = sysUserMapper.updateByPrimaryKeySelective(sysUser);
            //如果该用户在线，将新的信息给用户发一下
            webSocketServer.sendNewPersonInfoToUser(sysUser);
        }
        //返回结果
        return RUtil.isOptionOk(rows);
    }

    @Override
    public R getById(Long id) {
        SysUserAndOrgVO sysUserAndOrgVO = sysUserMapper.getById(id);
        if (sysUserAndOrgVO.getImgPath() != null) {
            sysUserAndOrgVO.setImgPath(serverConfig.getImageUrl(sysUserAndOrgVO.getImgPath()));
        }
        return R.ok(sysUserAndOrgVO);
    }

    @Override
    public R removeByIdList(List<Long> idList) {
        //判断参数是否为空
        if (idList.isEmpty()) {
            return R.error(TipsConstants.PARAM_NOT_ENOUGH);
        }
        //批量删除用户,将有效字段置为0
        if (sysUserMapper.updateIsValidToZeroByIdList(idList) == idList.size()) {
            //将用户的在线状态删除
            sysUserStatusMapper.removeBySysUserList(idList);
        }
        return R.okDefault();
    }

    @Override
    public R getSortData(Long orgId) {
        //根据组织机构id查询该机构所有直属员工, 若id为空，则全查
        return R.ok(sysUserMapper.getFirstSonUserByOrgId(orgId));
    }

    @Override
    public R sort(Map<String, Object> param) {
        List<Map<String, Object>> oldSort = (List<Map<String, Object>>) param.get("oldSortList");
        List<Map<String, Object>> newSort = (List<Map<String, Object>>) param.get("newSortList");
        int flag = 1;
        for (int i = 0; i < oldSort.size(); i++) {
            //数据不一致，代表需要保存排序数据，否则代表用户没做操作，就不需要做任何操作
            if (!oldSort.get(i).get("id").toString().equals(newSort.get(i).get("id").toString())) {
                oldSort.get(i).put("id", newSort.get(i).get("id").toString());
                //保存排序数据
                if (1 != sysUserMapper.sort(oldSort.get(i))) {
                    flag = 0;
                }
            }
        }
        return RUtil.isOptionOk(flag);
    }

    @Override
    public Validator checkLoginNameExist(String loginName, Long id) {
        //查看该登录名是否已存在
        return RUtil.isNameExist(sysUserMapper.getLoginNameCount(loginName, id));
    }

    @Override
    public R login(HttpServletRequest request, HttpServletResponse response, LoginParam loginParam) {
        //根据用户名和密码查询该用户信息
        SysUser sysUser = sysUserMapper.getByLoginNameAndLoginPwd(
                loginParam.getLoginName(),
                MD5Util.getSlatMD5(loginParam.getLoginPwd(), Constants.LOGIN_PWD_SLAT));
        if (null == sysUser) {
            //登陆失败
            request.setAttribute("notLog", 1);
            return R.error(TipsConstants.LOGIN_FAILURE);
        } else {
            if (null == request.getHeader(Constants.TYPE_HEADER_PARAM) ||
                    !StringUtils.equals(Constants.TYPE_WEB_PARAM, request.getHeader(Constants.TYPE_HEADER_PARAM))) {
                if (sysUser.getHaveAuthority() == 0) {
                    //登陆失败
                    request.setAttribute("notLog", 1);
                    return R.error(TipsConstants.NOT_HAVE_AUTHORITY);
                }
            }
            //登录成功，生成token，过期时间4小时
            String token = tokenGenerator.create(sysUser, 4*60*60);
            //判断当前是否有已登陆的该用户
            if (redisService.hasHashKey(Constants.HAS_LOGIN_USER_LIST, sysUser.getId().toString())) {
                //将该用户下线，后登陆的顶替先登录的
                redisService.deleteByKey(redisService.hget(Constants.HAS_LOGIN_USER_LIST, sysUser.getId().toString()));
            }

            redisService.hset(Constants.HAS_LOGIN_USER_LIST, sysUser.getId().toString(), token);
            //将登录信息保存到redis,过期时间4小时
            redisService.set(Constants.LOGIN_TOKEN + token, JSONObject.toJSONString(sysUser), 4L, TimeUnit.HOURS);
            request.setAttribute("sysUserId", sysUser.getId());
            return R.ok(TipsConstants.LOGIN_SUCCESS, token);
        }
    }

    @Override
    public R getByToken(HttpServletRequest request) {
        String token = request.getHeader(Constants.TOKEN_HEADER_PARAM);
        SysUserAndOrgVO sysUserAndOrgVO = sysUserMapper.getById(redisService.findByToken(token).getId());
        sysUserAndOrgVO.setImgPath(serverConfig.getImageUrl(sysUserAndOrgVO.getImgPath()));
        return R.ok(sysUserAndOrgVO);
    }

    @Override
    public R logout(HttpServletRequest request, HttpServletResponse response) {
        String token = request.getHeader(Constants.TOKEN_HEADER_PARAM);
        Long sysUserId = redisService.findByToken(token).getId();
        request.setAttribute("sysUserId", sysUserId);
        //将用户的登录信息从redis移除
        redisService.deleteByKey(Constants.LOGIN_TOKEN + token);
        redisService.hashDeleteByKey(Constants.HAS_LOGIN_USER_LIST, sysUserId.toString());
        return R.okDefault();
    }

    @Override
    public R updatePassword(UpdatePwdObj updatePwdObj) {
        String token = HttpContextUtils.getHttpServletRequest().getHeader(Constants.TOKEN_HEADER_PARAM);
        Long sysUserId = redisService.findByToken(token).getId();
        updatePwdObj.setId(sysUserId);
        updatePwdObj.setOldLoginPwd(MD5Util.getSlatMD5(updatePwdObj.getOldLoginPwd(), Constants.LOGIN_PWD_SLAT));

        //首先验证旧密码是否正确
        if (sysUserMapper.checkOldPassword(updatePwdObj)) { //旧密码正确，修改密码
            updatePwdObj.setNewLoginPwd(MD5Util.getSlatMD5(updatePwdObj.getNewLoginPwd(), Constants.LOGIN_PWD_SLAT));
            sysUserMapper.updatePassword(updatePwdObj);
            //将用户的登录信息从redis移除,让其重新登录
            redisService.deleteByKey(Constants.LOGIN_TOKEN + token);
            redisService.hashDeleteByKey(Constants.HAS_LOGIN_USER_LIST, sysUserId.toString());
            return R.ok(TipsConstants.UPDATE_PASSWORD_SUCCESS);
        } else { //旧密码不正确
            return R.error(TipsConstants.OLD_PASSWORD_ERROR);
        }
    }

    @Override
    public WSResult getMailList(Long sysUserId) {
        //获取用户的通讯录
        List<SysUser> sysUserList = sysUserMapper.getMailList(sysUserId);
        for (SysUser sysUser : sysUserList) {
            sysUser.setImgPath(serverConfig.getImageUrl(sysUser.getImgPath()));
        }
        return new WSResult(5, sysUserList);
    }

    @Override
    public WSResult getMailFriendDetail(Long sysUserId) {
        SysUserAndOrgVO sysUserAndOrgVO = sysUserMapper.getById(sysUserId);
        sysUserAndOrgVO.setImgPath(serverConfig.getImageUrl(sysUserAndOrgVO.getImgPath()));
        return new WSResult(6, sysUserAndOrgVO);
    }

    @Override
    public void setUserStatus(Long sysUserId, Byte status) {
        sysUserStatusMapper.setUserStatus(sysUserId, status);
    }

    @Override
    public List<Long> getCustomerServiceIdList() {
        //获取客服id列表
        return sysUserMapper.getCustomerServiceIdList();
    }
}
