package com.lhrsite.xshop.core.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.*;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Component
public class RedisUtil<HK, T> {

    // 在构造器中获取redisTemplate实例, key(not hashKey) 默认使用String类型
    private RedisTemplate<String, T> redisTemplate;

    // 在构造器中通过redisTemplate的工厂方法实例化操作对象
    private HashOperations<String, HK, T> hashOperations;

    private ListOperations<String, T> listOperations;

    private ZSetOperations<String, T> zSetOperations;

    private SetOperations<String, T> setOperations;

    private ValueOperations<String, T> valueOperations;

    // IDEA虽然报错,但是依然可以注入成功, 实例化操作对象后就可以直接调用方法操作Redis数据库
    @Autowired
    public RedisUtil(RedisTemplate<String, T> redisTemplate) {
        this.redisTemplate = redisTemplate;
        this.hashOperations = redisTemplate.opsForHash();
        this.listOperations = redisTemplate.opsForList();
        this.zSetOperations = redisTemplate.opsForZSet();
        this.setOperations = redisTemplate.opsForSet();
        this.valueOperations = redisTemplate.opsForValue();
    }


    public void hashPut(String key, HK hashKey, T domain) {
        hashOperations.put(key, hashKey, domain);
    }

    public Map<HK, T> hashFindAll(String key) {
        return hashOperations.entries(key);
    }

    public T hashGet(String key, HK hashKey) {
        return hashOperations.get(key, hashKey);
    }

    public void hashRemove(String key, HK hashKey) {
        hashOperations.delete(key, hashKey);
    }

    public Long listPush(String key, T domain) {
        return listOperations.rightPush(key, domain);
    }

    public Long listUnshift(String key, T domain) {
        return listOperations.leftPush(key, domain);
    }

    public List<T> listFindAll(String key) {
        if (!redisTemplate.hasKey(key)) {
            return null;
        }
        return listOperations.range(key, 0, listOperations.size(key));
    }

    public T listLPop(String key) {
        return listOperations.leftPop(key);
    }

    public void remove(String key) {
        redisTemplate.delete(key);
    }

    public boolean expire(String key, long timeout, TimeUnit timeUnit) {
        return redisTemplate.expire(key, timeout, timeUnit);
    }
}