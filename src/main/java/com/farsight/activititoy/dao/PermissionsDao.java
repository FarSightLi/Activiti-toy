package com.farsight.activititoy.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.farsight.activititoy.entity.Permissions;
import org.apache.ibatis.annotations.Mapper;

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