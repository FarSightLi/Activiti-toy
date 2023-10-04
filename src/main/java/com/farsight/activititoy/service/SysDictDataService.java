package com.farsight.activititoy.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.farsight.activititoy.entity.SysDictData;

import java.util.List;

/**
 * @author 骆灵上
 * @description 针对表【sys_dict_data(字典值)】的数据库操作Service
 * @createDate 2023-10-03 13:40:54
 */
public interface SysDictDataService extends IService<SysDictData> {
    void saveDataToRedis(List<SysDictData> data);

    /**
     * 根据字典类型获取一整个类型的字典数据
     *
     * @param type
     * @return
     */
    Object getDataFromRedis(String type);

    SysDictData getDataFromRedisByLabel(String type, String label);

    void updateRedisData(SysDictData sysDictData);
}
