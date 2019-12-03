package com.qiang.practice.oauth;

import com.alibaba.fastjson.JSONObject;
import com.qiang.practice.base.Constants;
import com.qiang.practice.exception.AuthenticationException;
import com.qiang.practice.exception.TokenExpiredException;
import com.qiang.practice.model.SysUser;
import io.jsonwebtoken.*;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.time.Instant;
import java.util.Date;

/**
 * @ClassName: JwtTokenGenerator
 * @Author: CLQ
 * @Date: 2019/8/8
 * @Description: JwtToken生成器
 */
@SuppressWarnings("all")
@Service
public class JwtTokenGenerator implements TokenGenerator {

    public final static Charset DEFAULT_CHARSET = Charset.forName("UTF-8");

    @Override
    public String create(SysUser sysUser, long durationInSeconds) {
        try {
            sysUser.setLoginPwd(null);
            String userJson = JSONObject.toJSONString(sysUser);
            return Jwts.builder()
                    .signWith(SignatureAlgorithm.HS256, Constants.JWT_TOKEN_SECRET.getBytes(DEFAULT_CHARSET.name()))
                    .setSubject(userJson)
                    .setExpiration(Date.from(Instant.now().plusSeconds(durationInSeconds)))
                    .compact();
        } catch (UnsupportedEncodingException e) {
            throw new AuthenticationException(e.getMessage());
        }
    }

    @Override
    public Claims extract(String tokenStr) {
        try {
            Jws<Claims> jws = Jwts.parser()
                    .setSigningKey(Constants.JWT_TOKEN_SECRET.getBytes(DEFAULT_CHARSET.name()))
                    .parseClaimsJws(tokenStr);

            return jws.getBody();
        } catch (ExpiredJwtException e) {
            throw new TokenExpiredException("Access token is expired");
        } catch (UnsupportedEncodingException e) {
            throw new AuthenticationException(e.getMessage());
        }
    }
}
