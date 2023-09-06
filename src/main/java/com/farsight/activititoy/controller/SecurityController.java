package com.farsight.activititoy.controller;

import com.farsight.activititoy.dto.UserAccountDto;
import com.farsight.activititoy.entity.UserAccount;
import com.farsight.activititoy.service.impl.SecurityServiceImpl;
import com.farsight.activititoy.uitl.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/security")
public class SecurityController {
    @Autowired
    private SecurityServiceImpl securityService;

    @GetMapping("/test1")
    public Result test() {
        return Result.success("ok");
    }

    @PostMapping("/register")
    public Result register(@RequestBody UserAccountDto dto) {
        securityService.register(dto);
        return Result.success("注册成功");
    }

    @PostMapping("/login")
    public Result<String> login(@RequestBody UserAccount userAccount) {
        String token = securityService.login(userAccount);
        return Result.success("登录成功", token);
    }
}
