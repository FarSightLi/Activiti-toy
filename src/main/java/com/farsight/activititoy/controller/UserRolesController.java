package com.farsight.activititoy.controller;


import com.farsight.activititoy.service.UserRolesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * <p>
 * 用户角色对应表 前端控制器
 * </p>
 *
 * @author farsight
 * @since 2023-09-02
 */
@Controller
@RequestMapping("//user-roles")
public class UserRolesController {
    @Autowired
    private UserRolesService userRolesService;


}
