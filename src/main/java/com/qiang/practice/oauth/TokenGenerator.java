package com.qiang.practice.oauth;

import com.qiang.practice.model.SysUser;
import io.jsonwebtoken.Claims;

/**
 * @InterfaceName: TokenGenerator
 * @Author: CLQ
 * @Date: 2019/8/8
 * @Description: 生成token、验证token
 */
public interface TokenGenerator {
    //生成token
    String create(SysUser sysUser, long durationInSeconds);
    //从token中提取值
    Claims extract(String token);
}
