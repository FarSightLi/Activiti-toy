package com.farsight.activititoy.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.farsight.activititoy.dao.RolesDao;
import com.farsight.activititoy.entity.Roles;
import com.farsight.activititoy.service.RolesService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 角色表 服务实现类
 * </p>
 *
 * @author farsight
 * @since 2023-09-02
 */
@Service
public class RolesServiceImp extends ServiceImpl<RolesDao, Roles> implements RolesService {

}
