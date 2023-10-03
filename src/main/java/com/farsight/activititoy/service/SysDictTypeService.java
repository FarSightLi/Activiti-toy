package com.farsight.activititoy.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.farsight.activititoy.entity.SysDictType;

/**
 * @author 骆灵上
 * @description 针对表【sys_dict_type】的数据库操作Service
 * @createDate 2023-10-03 13:40:54
 */
public interface SysDictTypeService extends IService<SysDictType> {
    boolean checkDictTypeUnique(SysDictType dict);
}
