package com.farsight.activititoy.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.farsight.activititoy.dao.UserRolesDao;
import com.farsight.activititoy.entity.UserRoles;
import com.farsight.activititoy.service.UserRolesService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户角色对应表 服务实现类
 * </p>
 *
 * @author farsight
 * @since 2023-09-02
 */
@Service
public class UserRolesServiceImp extends ServiceImpl<UserRolesDao, UserRoles> implements UserRolesService {

}
