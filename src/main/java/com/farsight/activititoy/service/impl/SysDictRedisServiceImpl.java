package com.farsight.activititoy.service.impl;

import com.farsight.activititoy.service.SysDictRedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SysDictRedisServiceImpl implements SysDictRedisService {
    private static final String KEY = "toy_dict";
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * 编写存储数据到Redis的方法
     *
     * @param key
     * @param value
     */
    @Override
    public void saveDataToRedis(String hashKey, String key, String value) {
        redisTemplate.opsForHash().put(KEY + ":" + hashKey, key, value);
    }

    /**
     * 获取字典中某个数据
     *
     * @param
     * @return
     */
    @Override
    public Object getDataFromRedisByLabel(String type, String label) {
        return redisTemplate.opsForHash().get(KEY + ":" + type, label);
    }

    /**
     * 获取字典中某个类型下的所有数据
     *
     * @param type
     * @return
     */
    @Override
    public Object getDataFromRedis(String type) {
        return redisTemplate.opsForHash().entries(KEY + ":" + type);
    }

    /**
     * 删除所有字典缓存
     *
     * @param keyList
     */
    @Override
    public void deleteAllData(List<String> keyList) {
        for (String key : keyList) {
            redisTemplate.delete(KEY + ":" + key);
        }
    }

    @Override
    public void putData(String type, String label, String value) {
        redisTemplate.opsForHash().put(KEY + ":" + type, label, value);
    }

    @Override
    public void deleteData(String type) {
        redisTemplate.delete(KEY + ":" + type);
    }

    @Override
    public void deleteLabel(String type, String label) {
        redisTemplate.opsForHash().delete(KEY + ":" + type, label);
    }
}
