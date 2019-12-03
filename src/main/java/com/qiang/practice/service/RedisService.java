package com.qiang.practice.service;

import com.qiang.practice.model.SysUser;

import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @Author: CLQ
 * @Date: 2019/8/15
 * @Description: TODO
 */
public interface RedisService {
    /**
     * 将  token 和 用户信息json串    保存到redis
     * @param key
     * @param value
     */
    void set(String key, String value);

    /**
     * 设置value  key-value
     * @param key
     * @param value
     * @param timeNum
     * @param timeUnit
     */
    void set(String key, String value, long timeNum, TimeUnit timeUnit);

    /**
     * 获取value  key-value
     * @param key
     * @return
     */
    String get(String key);

    /**
     * 根据token取用户信息
     * @param token
     * @return
     */
    SysUser findByToken(String token);

    /**
     * 设置值  key-hash  使用(field, key, value)形式
     * @param field
     * @param key
     * @param value
     */
    void hset(String field, String key, String value);

    /**
     * 设置值  key-hash   使用(key, map)形式
     * @param field
     * @param map
     */
    void hset(String field, Map<?, ?> map);

    /**
     * 获取值  field-hash
     * @param field
     * @param key
     * @return
     */
    String hget(String field, String key);

    Boolean hasKey(String key);

    Boolean hasHashKey(String hash, String hashKey);

    void deleteByKey(String key);

    void hashDeleteByKey(String hash, String hashKey);

}
