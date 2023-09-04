package com.farsight.activititoy.service.impl;

import com.farsight.activititoy.dto.UserAccountDto;
import com.farsight.activititoy.entity.UserAccount;
import com.farsight.activititoy.exception.BusinessException;
import com.farsight.activititoy.exception.CodeMsg;
import com.farsight.activititoy.service.SecurityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class SecurityServiceImpl implements SecurityService {
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserAccountServiceImp userAccountService;

    @Override
    public boolean register(UserAccountDto dto) {
        // 两次密码一致的
        if (dto.getRepeatPassword().equals(dto.getPassword())) {
            // 生成随机盐值
            String salt = generateSalt();
            // 用盐值和密码编码器加密
            String hashedPassword = passwordEncoder.encode(dto.getPassword()+salt);
            UserAccount userAccount = new UserAccount();
            userAccount.setPassword(hashedPassword);
            userAccount.setUsername(dto.getUsername());
            userAccount.setSalt(salt);
            userAccountService.save(userAccount);
        }else {
            throw new BusinessException(CodeMsg.PARAMETER_ERROR,"两次密码不一致，请重新输入");
        }
        return true;
    }

    // 生成随机盐值
    private String generateSalt() {
        return String.valueOf(UUID.randomUUID());
    }
}
