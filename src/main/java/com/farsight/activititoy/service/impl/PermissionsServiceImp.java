package com.farsight.activititoy.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.farsight.activititoy.dao.PermissionsDao;
import com.farsight.activititoy.entity.Permissions;
import com.farsight.activititoy.service.PermissionsService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 权限表	 服务实现类
 * </p>
 *
 * @author farsight
 * @since 2023-09-06
 */
@Service
public class PermissionsServiceImp extends ServiceImpl<PermissionsDao, Permissions> implements PermissionsService {

}
