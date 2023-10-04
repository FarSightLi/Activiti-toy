package com.farsight.activititoy.service;

import java.util.List;

public interface SysDictRedisService {
    /**
     * 获取某个字典值数据
     *
     * @param type  字典type
     * @param label 字典标签
     * @return
     */
    Object getDataFromRedisByLabel(String type, String label);

    /**
     * 获取某个type字典
     *
     * @param type
     * @return
     */
    Object getDataFromRedis(String type);

    void saveDataToRedis(String hashKey, String key, String value);

    void deleteAllData(List<String> keyList);

    void updateData(String key, String value);
}
