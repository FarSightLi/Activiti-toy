package com.farsight.activititoy.service.impl;

import com.farsight.activititoy.dto.UserAccountDto;
import com.farsight.activititoy.entity.UserAccountEntity;
import com.farsight.activititoy.service.SecurityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SecurityServiceImpl implements SecurityService {
//    @Autowired
//    UserDetailsServiceImpl userDetailsService;

    @Override
    public boolean register(UserAccountDto dto) {
        // 两次密码已知的
        if (dto.getRepeatPassword().equals(dto.getPassword())){
        }
        return false;
    }
}
