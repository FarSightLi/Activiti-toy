package com.farsight.activititoy.dao;

import com.farsight.activititoy.entity.Permissions;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
/**
* <p>
    * 权限表	 Mapper 接口
    * </p>
*
* @author farsight
* @since 2023-09-06
*/
@Mapper
public interface PermissionsDao extends BaseMapper<Permissions> {
}