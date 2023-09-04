package com.farsight.activititoy.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.farsight.activititoy.entity.ToyBusinessEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author farsight
 * @since 2023-07-22
 */
@Mapper
public interface ToyBusinessDao extends BaseMapper<ToyBusinessEntity> {
}
