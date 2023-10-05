package com.farsight.activititoy.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.farsight.activititoy.dao.SysDictDataDao;
import com.farsight.activititoy.entity.SysDictData;
import com.farsight.activititoy.exception.BusinessException;
import com.farsight.activititoy.exception.CodeMsg;
import com.farsight.activititoy.service.SysDictDataService;
import com.farsight.activititoy.service.SysDictRedisService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
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

    @Override
    public void saveDataToRedis(List<SysDictData> data) {
        Map<String, List<SysDictData>> collect = data.stream().collect(Collectors.groupingBy(SysDictData::getType));
        collect.forEach((k, v) -> {
            try {
                for (SysDictData sysDictData : v) {
                    String json = objectMapper.writeValueAsString(sysDictData);
                    redisService.saveDataToRedis(sysDictData.getType(), sysDictData.getLabel(), json);
                }
            } catch (JsonProcessingException e) {
                log.error(e.getMessage());
                throw new BusinessException(CodeMsg.JACKSON_ERROR);
            }
        });
    }

    @Override
    public Object getDataFromRedis(String type) {
        LinkedHashMap<String, String> data = (LinkedHashMap<String, String>) redisService.getDataFromRedis(type);
        List<SysDictData> sysDictDataList = new ArrayList<>();
        data.forEach((k, v) -> {
            try {
                SysDictData sysDictData = objectMapper.readValue(v, SysDictData.class);
                sysDictDataList.add(sysDictData);
            } catch (JsonProcessingException e) {
                log.error(e.getMessage());
                throw new BusinessException(CodeMsg.JACKSON_ERROR);
            }
        });
        return sysDictDataList;
    }

    @Override
    public SysDictData getDataFromRedisByLabel(String type, String label) {
        Object redisData = redisService.getDataFromRedisByLabel(type, label);
        if (redisData == null) {
            SysDictData databaseData = getOne(new LambdaQueryWrapper<SysDictData>().eq(SysDictData::getType, type).eq(SysDictData::getLabel, label));
            if (databaseData != null) {
                this.addDataToRedis(databaseData);
            }
            return databaseData;
        } else {
            String data = (String) redisData;
            try {
                SysDictData sysDictData = objectMapper.readValue(data, SysDictData.class);
                return sysDictData;
            } catch (IOException e) {
                // 处理异常
                log.error(e.getMessage());
                throw new BusinessException(CodeMsg.JACKSON_ERROR);
            }
        }
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void updateData(SysDictData sysDictData) {
        updateById(sysDictData);
        // 删除缓存
        redisService.deleteData(sysDictData.getType());
        // 从数据库中获得字典
        List<SysDictData> list = this.list(new LambdaQueryWrapper<SysDictData>().eq(SysDictData::getType, sysDictData.getType()));
        try {
            for (SysDictData dictData : list) {
                String json = objectMapper.writeValueAsString(dictData);
                redisService.putData(dictData.getType(), dictData.getLabel(), json);
            }
        } catch (JsonProcessingException e) {
            throw new BusinessException(CodeMsg.JACKSON_ERROR);
        }
    }

    @Override
    @Transactional
    public void deleteData(String type, String label) {
        this.remove(new LambdaQueryWrapper<SysDictData>().eq(SysDictData::getType, type).eq(SysDictData::getLabel, label));
        redisService.deleteLabel(type, label);
    }

    @Override
    @Transactional
    public void addData(SysDictData sysDictData) {
        save(sysDictData);
        addDataToRedis(sysDictData);
    }

    private void addDataToRedis(SysDictData sysDictData) {
        try {
            String json = objectMapper.writeValueAsString(sysDictData);
            redisService.putData(sysDictData.getType(), sysDictData.getLabel(), json);
        } catch (JsonProcessingException e) {
            log.error(e.getMessage());
            throw new BusinessException(CodeMsg.JACKSON_ERROR);
        }
    }
}




