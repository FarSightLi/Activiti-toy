package com.farsight.activititoy.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.farsight.activititoy.entity.SysDictType;
import com.farsight.activititoy.service.SysDictTypeService;
import com.farsight.activititoy.uitl.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 数据字典信息
 *
 * @author FarSightLi
 */
@Slf4j
@RestController
@RequestMapping("/system/dict/type")
public class SysDictTypeController {
    @Autowired
    private SysDictTypeService sysDictTypeService;

    /**
     * 查询列表
     *
     * @param type
     * @return
     */
    @GetMapping("/getDictTypeByType/{type}")
    public Result<List<SysDictType>> listByType(@PathVariable String type) {
        return Result.success(sysDictTypeService.list(new LambdaQueryWrapper<SysDictType>().eq(SysDictType::getType, type)));
    }

    /**
     * 新增字典类型
     */
    @PostMapping("/add")
    public Result add(@Validated @RequestBody SysDictType dict) {
        sysDictTypeService.checkDictTypeUnique(dict);
        return Result.success(sysDictTypeService.save(dict));
    }

    /**
     * 通过id（code）查询字典数据详细
     */
    @GetMapping(value = "/getInfoByCode/{dictCode}")
    public Result getInfoByCode(@PathVariable Long dictCode) {
        return Result.success(sysDictTypeService.getById(dictCode));
    }

    /**
     * 查询所有列表
     *
     * @param
     * @return
     */
    @GetMapping("/list")
    public Result<List<SysDictType>> list() {
        return Result.success(sysDictTypeService.list());
    }

}
