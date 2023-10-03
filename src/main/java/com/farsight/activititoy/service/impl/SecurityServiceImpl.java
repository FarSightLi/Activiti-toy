package com.farsight.activititoy.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.farsight.activititoy.dto.UserAccountDto;
import com.farsight.activititoy.entity.UserAccount;
import com.farsight.activititoy.entity.UserRoles;
import com.farsight.activititoy.exception.BusinessException;
import com.farsight.activititoy.exception.CodeMsg;
import com.farsight.activititoy.service.SecurityService;
import com.farsight.activititoy.uitl.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class SecurityServiceImpl implements SecurityService {
    @Autowired
    JwtTokenUtil jwtTokenUtil;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserAccountServiceImp userAccountService;
    @Autowired
    private UserDetailsServiceImpl userDetailsService;
    @Autowired
    private UserRolesServiceImp userRolesService;

    @Override
    public boolean register(UserAccountDto dto) {
        // 两次密码一致的
        if (dto.getRepeatPassword().equals(dto.getPassword())) {
            // 生成随机盐值
            String salt = generateSalt();
            // 用盐值和密码编码器加密
            String hashedPassword = passwordEncoder.encode(dto.getPassword());
            UserAccount userAccount = new UserAccount();
            // 存储的密码是加密后的密码
            userAccount.setPassword(hashedPassword);
            userAccount.setUsername(dto.getUsername());
            userAccount.setSalt(salt);
            userAccountService.save(userAccount);
            // 进行授权
            authorize(userAccount);
            identify(dto.getUsername());
        } else {
            throw new BusinessException(CodeMsg.PARAMETER_ERROR, "两次密码不一致，请重新输入");
        }
        return true;
    }

    private void authorize(UserAccount userAccount) {
        UserRoles userRoles = new UserRoles();
        String uid = userAccountService.getOne(new LambdaQueryWrapper<UserAccount>().eq(UserAccount::getUsername, userAccount.getUsername())).getUid();
        userRoles.setUserId(uid);
        userRoles.setRoleId("1");
        userRolesService.save(userRoles);
    }

    private String identify(String username) {
        // 权限认证
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                userDetails.getUsername(),
                null,
                userDetails.getAuthorities()
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return jwtTokenUtil.generateToken(userDetails);
    }

    @Override
    public String login(UserAccount userAccount) {
        String username = userAccount.getUsername();
        UserAccount datebaseUser = userAccountService.getOne(new QueryWrapper<UserAccount>().eq("username", username));
        if (datebaseUser == null) {
            // 用户不存在
            throw new BusinessException(CodeMsg.PARAMETER_ERROR, "用户名或密码出错，请检查");
        }
        String hashedPassword = datebaseUser.getPassword();
        String token = "";
        // 如果密码符合
        if (passwordEncoder.matches(userAccount.getPassword(), hashedPassword)) {
            token = identify(username);
        } else {
            throw new BusinessException(CodeMsg.PARAMETER_ERROR, "用户名或密码出错，请检查");
        }
        return token;
    }

    // 生成随机盐值
    private String generateSalt() {
        return String.valueOf(UUID.randomUUID());
    }
}
