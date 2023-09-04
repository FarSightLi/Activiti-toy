package com.farsight.activititoy.controller;

import com.farsight.activititoy.dto.UserAccountDto;
import com.farsight.activititoy.service.impl.SecurityServiceImpl;
import com.farsight.activititoy.uitl.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<String> register(@RequestBody UserAccountDto dto) {
        securityService.register(dto);
        return ResponseEntity.ok("注册成功");
    }
}
