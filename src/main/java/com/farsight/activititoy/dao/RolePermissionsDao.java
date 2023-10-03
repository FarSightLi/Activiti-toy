package com.farsight.activititoy.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.farsight.activititoy.entity.RolePermissions;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 角色权限表 Mapper 接口
 * </p>
 *
 * @author farsight
 * @since 2023-09-04
 */
@Mapper
public interface RolePermissionsDao extends BaseMapper<RolePermissions> {
}