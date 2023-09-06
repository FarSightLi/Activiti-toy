package com.farsight.activititoy.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.farsight.activititoy.entity.Roles;
import org.apache.ibatis.annotations.Mapper;

import java.io.Serializable;

/**
 * <p>
 * 角色表 Mapper 接口
 * </p>
 *
 * @author farsight
 * @since 2023-09-02
 */
@Mapper
public interface RolesDao extends BaseMapper<Roles> {
}