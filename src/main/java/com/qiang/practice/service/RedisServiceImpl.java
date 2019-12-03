package com.qiang.practice.service;

import com.alibaba.fastjson.JSONObject;
import com.qiang.practice.base.Constants;
import com.qiang.practice.model.SysUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @Author: CLQ
 * @Date: 2019/8/15
 * @Description: TODO
 */
@Service
public class RedisServiceImpl implements RedisService {
    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    private ValueOperations<String, String> opsForValue() {
        return redisTemplate.opsForValue();
    }

    private HashOperations<String, String, String> opsForHash() {
        return redisTemplate.opsForHash();
    }

    public void set(String key, String value) {
        opsForValue().set(key, value);
    }

    @Override
    public void set(String key, String value, long timeNum, TimeUnit timeUnit) {
        opsForValue().set(key, value, timeNum, timeUnit);
    }

    @Override
    public String get(String key) {
        return opsForValue().get(key);
    }

    @Override
    public SysUser findByToken(String token) {
        return JSONObject.parseObject(get(Constants.LOGIN_TOKEN + token), SysUser.class);
    }

    @Override
    public void hset(String field, String key, String value) {
        opsForHash().put(field, key, value);
    }

    @Override
    public void hset(String field, Map<?, ?> map) {
        redisTemplate.opsForHash().putAll(field, map);
    }

    @Override
    public String hget(String field, String key) {
        return opsForHash().get(field, key);
    }

    @Override
    public Boolean hasKey(String key) throws NullPointerException {
        return redisTemplate.hasKey(key);
    }

    @Override
    public Boolean hasHashKey(String hash, String hashKey) {
        return opsForHash().hasKey(hash, hashKey);
    }

    @Override
    public void deleteByKey(String key) {
        redisTemplate.delete(key);
    }

    public void hashDeleteByKey(String hash, String hashKey) {
        opsForHash().delete(hash, hashKey);
    }
}
