package com.farsight.activititoy.controller;


import com.farsight.activititoy.service.UserAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * <p>
 * 用户的账号密码表 前端控制器
 * </p>
 *
 * @author farsight
 * @since 2023-09-02
 */
@Controller
@RequestMapping("//user-account")
public class UserAccountController {
    @Autowired
    private UserAccountService userAccountService;

}
