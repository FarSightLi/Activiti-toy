package com.farsight.activititoy.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.farsight.activititoy.dao.UserAccountDao;
import com.farsight.activititoy.entity.UserAccount;
import com.farsight.activititoy.service.UserAccountService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户的账号密码表 服务实现类
 * </p>
 *
 * @author farsight
 * @since 2023-09-02
 */
@Service
public class UserAccountServiceImp extends ServiceImpl<UserAccountDao, UserAccount> implements UserAccountService {

}
