package com.farsight.activititoy.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.farsight.activititoy.dao.SysDictDataDao;
import com.farsight.activititoy.entity.SysDictData;
import com.farsight.activititoy.service.SysDictDataService;
import com.farsight.activititoy.service.SysDictRedisService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author 骆灵上
 * @description 针对表【sys_dict_data(字典值)】的数据库操作Service实现
 * @createDate 2023-10-03 13:40:54
 */
@Service
@Slf4j
public class SysDictDataServiceImpl extends ServiceImpl<SysDictDataDao, SysDictData>
        implements SysDictDataService {
    @Autowired
    private SysDictRedisService redisService;
    @Autowired
    private ObjectMapper objectMapper;

    public void saveDataToRedis(List<SysDictData> data) {
        Map<String, List<SysDictData>> collect = data.stream().collect(Collectors.groupingBy(SysDictData::getType));
        collect.forEach((k, v) -> {
            try {
                String json = objectMapper.writeValueAsString(v);
                redisService.saveDataToRedis(k, json);
            } catch (JsonProcessingException e) {
                log.error(e.getMessage());
            }
        });
    }

    public List<SysDictData> getDataFromRedis(String key) {
        String dataFromRedis = (String) redisService.getDataFromRedis(key);
        try {
            TypeReference<List<SysDictData>> typeReference = new TypeReference<List<SysDictData>>() {
            };
            return objectMapper.readValue(dataFromRedis, typeReference);
        } catch (IOException e) {
            // 处理异常
            log.error(e.getMessage());
            return Collections.emptyList();
        }
    }
}




