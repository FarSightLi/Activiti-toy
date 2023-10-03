package com.farsight.activititoy.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.farsight.activititoy.entity.Deadman;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author farsight
 * @since 2023-09-22
 */
@Mapper
public interface DeadmanDao extends BaseMapper<Deadman> {
    int insertBatch(@Param("deadmanCollection") List<Deadman> deadmanCollection);
}