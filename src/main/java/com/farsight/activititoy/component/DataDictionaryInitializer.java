package com.farsight.activititoy.component;

import com.farsight.activititoy.entity.SysDictData;
import com.farsight.activititoy.service.SysDictDataService;
import com.farsight.activititoy.service.SysDictRedisService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 将字典信息从数据库中加载到Redis
 */
@Component
@Slf4j
public class DataDictionaryInitializer implements CommandLineRunner {
    @Autowired
    private SysDictDataService dictDataService;
    @Autowired
    private SysDictRedisService dictRedisService;

    @Override
    public void run(String... args) throws Exception {
        long start = System.currentTimeMillis();
        List<SysDictData> list = dictDataService.list();
        List<String> collect = list.stream().map(SysDictData::getType).collect(Collectors.toList());
        // 清除原有缓存
        dictRedisService.deleteAllData(collect);
        // 加载所有数据到缓存
        dictDataService.saveDataToRedis(list);
        long end = System.currentTimeMillis();
        log.info("消耗时间" + (end - start) + "ms");
    }
}
