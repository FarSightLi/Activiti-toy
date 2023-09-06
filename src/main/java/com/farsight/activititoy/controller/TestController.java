package com.farsight.activititoy.controller;

import com.farsight.activititoy.uitl.Result;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

@RestController
@RequestMapping("/test")
public class TestController {
    @GetMapping("/test")
    public Result<Collection<? extends GrantedAuthority>> test() {
        // 获取当前线程的 SecurityContext
        SecurityContext securityContext = SecurityContextHolder.getContext();

        // 从 SecurityContext 中获取 Authentication 对象
        Authentication authentication = securityContext.getAuthentication();

        // 获取已登录用户的用户名
        String username = authentication.getName();

        // 获取已登录用户的权限（GrantedAuthority）
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        return Result.success(authorities);
    }

}
