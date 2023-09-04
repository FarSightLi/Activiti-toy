package com.farsight.activititoy.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.farsight.activititoy.dao.UserInfoDao;
import com.farsight.activititoy.entity.UserInfo;
import com.farsight.activititoy.service.UserInfoService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户详细信息表 服务实现类
 * </p>
 *
 * @author farsight
 * @since 2023-09-04
 */
@Service
public class UserInfoServiceImp extends ServiceImpl<UserInfoDao, UserInfo> implements UserInfoService {

}
