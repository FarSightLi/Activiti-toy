package com.farsight.activititoy.controller;

import com.farsight.activititoy.dao.DeadmanDao;
import com.farsight.activititoy.entity.Deadman;
import com.farsight.activititoy.exception.CodeMsg;
import com.farsight.activititoy.service.DeadmanService;
import com.farsight.activititoy.uitl.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
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

    @Autowired
    DeadmanService deadmanService;
    @Autowired
    DeadmanDao deadmanDao;

    @PostMapping("/import")
    public Result importFile(@RequestParam("file") MultipartFile file){
        deadmanService.importFile(file);
        return Result.success();
    }
    @PostMapping("/importThread")
    public Result importThread(@RequestParam("file") MultipartFile file){
        deadmanService.importFileThread(file);
        return Result.success();
    }
    @PostMapping("/testDao")
    public Result testDao(){
        ArrayList<Deadman> deadmen = new ArrayList<>();
        Deadman deadman = new Deadman();
        deadman.setAge("11");
        deadman.setIdCard("card");
        deadmen.add(deadman);
        if (deadmanDao!= null){
            return Result.success();
        }else {
            return Result.error(CodeMsg.SUCCESS,"deadmamNull");
        }
    }
}
