package com.farsight.activititoy.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.farsight.activititoy.entity.SysDictData;
import com.farsight.activititoy.service.SysDictDataService;
import com.farsight.activititoy.service.SysDictRedisService;
import com.farsight.activititoy.uitl.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 数据字典信息
 *
 * @author FarSightLi
 */
@RestController
@RequestMapping("/system/dict/data")
public class SysDictDataController {
    @Autowired
    private SysDictDataService dictDataService;
    @Autowired
    private SysDictRedisService dictRedisService;

    /**
     * 查看所有字典数据
     *
     * @return
     */
    @GetMapping("/list")
    public Result<List<SysDictData>> list() {
        return Result.success(dictDataService.list());
    }


    /**
     * 通过Type查询字典数据详细
     */
    @GetMapping(value = "/getInfoByType/{type}")
    public Result getInfoByType(@PathVariable String type) {
        return Result.success(dictDataService.list(new LambdaQueryWrapper<SysDictData>().eq(SysDictData::getType, type)));
    }

    /**
     * 新增字典类型
     */
    @PostMapping("/add")
    public Result add(@Validated @RequestBody SysDictData dict) {
        return Result.success(dictDataService.save(dict));
    }

    /**
     * 初始化缓存，将数据库中的字典信息放到redis里
     *
     * @return
     */
    @PostMapping("/initCache")
    public Result initCache() {
        dictDataService.saveDataToRedis(dictDataService.list());
        return Result.success();
    }

    /**
     * 从redis里拿到数据
     *
     * @return
     */
    @GetMapping("/cacheList")
    public Result<List<SysDictData>> cacheList(String key) {
        return Result.success(dictDataService.getDataFromRedis(key));
    }
}
