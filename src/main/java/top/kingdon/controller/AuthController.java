package top.kingdon.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import top.kingdon.config.SessionKey;
import top.kingdon.dataobject.bo.AuthUserBO;
import top.kingdon.dataobject.dto.AuthUserDTO;
import top.kingdon.service.UsersService;
import top.kingdon.utils.ApiResponse;
import top.kingdon.utils.HttpContextUtil;
import top.kingdon.utils.MetamaskUtil;

import javax.annotation.Resource;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Slf4j
@RestController
@RequestMapping("/auth")
public class AuthController {

    @Resource
    UsersService usersService;

    @Resource
    RedisTemplate redisTemplate;

    // 登录接口
    @PostMapping("/signin")
    public ApiResponse login(@RequestBody AuthUserDTO authUserDTO) {
        log.info("收到登录请求: {}", authUserDTO);
        // 验证签名
        boolean result = MetamaskUtil.validate(authUserDTO.getSignature(), authUserDTO.getMessage(), authUserDTO.getAddress());
        // 判断用户是否已经注册，是的话直接登录，否则注册并登录
        if(!result){
            return ApiResponse.error("身份验证失败");
        }

        AuthUserBO authUserBO = usersService.signIn(authUserDTO.getAddress());
        HttpContextUtil.setSessionAttribute(SessionKey.USER_ADDRESS,authUserBO.getAddress());
        log.info("登录成功，user: {} ", authUserDTO.getAddress());
        return ApiResponse.ok().put("user", authUserBO);

    }

    @GetMapping("/message/{address}")
    public String getMessage(@PathVariable("address") String address) {
        String message = MetamaskUtil.getMessage(address);
        log.info("消息内容: {}", message);
        return message;
    }

    @DeleteMapping("/signout")
    public ApiResponse logout() {
        String address = HttpContextUtil.getSessionAttribute("address");
        HttpContextUtil.cleanSession();
        log.info("退出登录:{}", address);
        return ApiResponse.ok();
    }





}
