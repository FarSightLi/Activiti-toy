package com.farsight.activititoy.service;

public interface SysDictRedisService {
    Object getDataFromRedis(String hashKey);

    void saveDataToRedis(String hashKey, String value);
}
