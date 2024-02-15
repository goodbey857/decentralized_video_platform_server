package top.kingdon.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import top.kingdon.utils.ApiResponse;
import top.kingdon.utils.HttpContextUtil;

@RestController
public class TestController {

    @GetMapping("/setSession")
    public ApiResponse setSession(){
        HttpContextUtil.setSessionAttribute("testKey","testValue");
        return ApiResponse.ok();
    }

    @GetMapping("/getSession")
    public  ApiResponse getSession(){
        //获取session中的值
        Object test = HttpContextUtil.getSessionAttribute("testKey");
        System.out.println(test);
        return ApiResponse.ok();
    }

    @GetMapping("/hello")
    public String hello() {
        return "hello";
    }
}

