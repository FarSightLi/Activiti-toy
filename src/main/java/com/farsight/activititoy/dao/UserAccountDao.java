package com.farsight.activititoy.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.farsight.activititoy.entity.UserAccount;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 用户的账号密码表 Mapper 接口
 * </p>
 *
 * @author farsight
 * @since 2023-09-02
 */
@Mapper
public interface UserAccountDao extends BaseMapper<UserAccount> {
}