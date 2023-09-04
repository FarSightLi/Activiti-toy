package com.farsight.activititoy.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.farsight.activititoy.entity.UserInfo;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 用户详细信息表 Mapper 接口
 * </p>
 *
 * @author farsight
 * @since 2023-09-04
 */
@Mapper
public interface UserInfoDao extends BaseMapper<UserInfo> {
}