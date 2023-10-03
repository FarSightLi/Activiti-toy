package com.farsight.activititoy.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.farsight.activititoy.dao.SysDictTypeDao;
import com.farsight.activititoy.entity.SysDictType;
import com.farsight.activititoy.exception.BusinessException;
import com.farsight.activititoy.exception.CodeMsg;
import com.farsight.activititoy.service.SysDictTypeService;
import org.springframework.stereotype.Service;

/**
 * @author 骆灵上
 * @description 针对表【sys_dict_type】的数据库操作Service实现
 * @createDate 2023-10-03 13:40:54
 */
@Service
public class SysDictTypeServiceImpl extends ServiceImpl<SysDictTypeDao, SysDictType>
        implements SysDictTypeService {
    @Override
    public boolean checkDictTypeUnique(SysDictType dict) {
        long typeCount = this.count(new LambdaQueryWrapper<SysDictType>().eq(SysDictType::getType, dict.getType()));
        long nameCount = this.count(new LambdaQueryWrapper<SysDictType>().eq(SysDictType::getName, dict.getName()));
        if (typeCount >= 1) {
            throw new BusinessException(CodeMsg.DICT_ERROR, "字典type重复");
        }
        if (nameCount >= 1) {
            throw new BusinessException(CodeMsg.DICT_ERROR, "字典name重复");
        }
        return true;
    }
}




