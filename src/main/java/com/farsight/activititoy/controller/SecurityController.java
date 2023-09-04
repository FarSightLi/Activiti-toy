package com.farsight.activititoy.controller;

import com.farsight.activititoy.dto.UserAccountDto;
import com.farsight.activititoy.entity.UserAccountEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/securityTest")
public class SecurityController {
    @GetMapping("/test1")
    public String test(){
        return "test";
    }

    @PostMapping("/register")
    public String register(@RequestBody UserAccountDto userAccount){
        return "sucess";
    }
}
