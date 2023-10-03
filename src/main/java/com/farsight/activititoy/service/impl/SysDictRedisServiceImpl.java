package com.farsight.activititoy.service.impl;

import com.farsight.activititoy.service.SysDictRedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class SysDictRedisServiceImpl implements SysDictRedisService {
    private static final String KEY = "toy_dict";
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;


    // 编写存储数据到Redis的方法
    public void saveDataToRedis(String hashKey, String value) {
        redisTemplate.opsForValue().append(KEY + ":" + hashKey, value);
    }

    // 编写从Redis中检索数据的方法
    public Object getDataFromRedis(String hashKey) {
        return redisTemplate.opsForValue().get(KEY + ":" + hashKey);
    }
}
