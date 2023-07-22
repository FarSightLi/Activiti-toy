package com.farsight.activititoy.controller;


import com.farsight.activititoy.service.ToyBusinessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author farsight
 * @since 2023-07-22
 */
@Controller
@RequestMapping("//toy-business-entity")
public class ToyBusinessController {
    @Autowired
    private ToyBusinessService toyBusinessEntityService;

}
