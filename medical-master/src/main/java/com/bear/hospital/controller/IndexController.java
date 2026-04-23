package com.bear.hospital.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * 首页控制器
 * 用于处理前端路由的 Hash 模式
 */
@Controller
public class IndexController {

    /**
     * 处理根路径请求
     * 返回前端页面的 HTML
     */
    @GetMapping("/")
    public String index() {
        return "forward:/index.html";
    }
}
