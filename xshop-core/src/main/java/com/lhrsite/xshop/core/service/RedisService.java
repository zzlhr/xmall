package com.lhrsite.xshop.core.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class RedisService<T> {
    private final RedisTemplate<String, T> redisTemplate;

    @Autowired
    public RedisService(RedisTemplate<String, T> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void set(String key, T value) {
        //更改在redis里面查看key编码问题
        RedisSerializer redisSerializer = new StringRedisSerializer();
        redisTemplate.setKeySerializer(redisSerializer);
        ValueOperations<String, T> vo = redisTemplate.opsForValue();
        vo.set(key, value);
    }

    public void set(String key, T value, Integer expireTime, TimeUnit timeUnit) {
        //更改在redis里面查看key编码问题
        RedisSerializer redisSerializer = new StringRedisSerializer();
        redisTemplate.setKeySerializer(redisSerializer);
        ValueOperations<String, T> vo = redisTemplate.opsForValue();
        vo.set(key, value, expireTime, timeUnit);
    }


    public T get(String key) {
        ValueOperations<String, T> vo = redisTemplate.opsForValue();
        return vo.get(key);
    }
}