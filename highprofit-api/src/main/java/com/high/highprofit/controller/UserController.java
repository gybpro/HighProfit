package com.high.highprofit.controller;

import com.high.highprofit.bean.User;
import com.high.highprofit.dto.ResultDTO;
import com.high.highprofit.service.UserService;
import com.high.highprofit.util.Assert;
import com.high.highprofit.util.CheckFormat;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.Date;

/**
 * 用户相关控制类
 *
 * @author high
 * @version 1.0
 * @since 1.0
 */
@Controller
@RequestMapping("/user")
@CrossOrigin
public class UserController {
    @DubboReference
    private final UserService userService;

    private final RedisTemplate<String, Object> redisTemplate;

    public UserController(UserService userService, RedisTemplate<String, Object> redisTemplate) {
        this.userService = userService;
        this.redisTemplate = redisTemplate;
    }

    @PostMapping("/register")
    @ResponseBody
    public ResultDTO register(String phone, String password, String code) {
        // 表单验证
        String realCode = getRealCode("register", phone);
        // 表单验证
        CheckFormat.checkCode(code, realCode);
        CheckFormat.checkPhone(phone);
        CheckFormat.checkPwd(password);
        Assert.isFlag(!checkPhone(phone), "手机号码已被注册");

        ResultDTO resultDTO = new ResultDTO();
        User user = new User();
        user.setPhone(phone);
        user.setLoginPassword(password);
        user.setAddTime(new Date());
        if (userService.registerUser(user) > 0) {
            resultDTO.setCode("1");
            resultDTO.setMessage("注册成功");
        } else {
            resultDTO.setCode("0");
            resultDTO.setMessage("系统忙，请稍候再试......");
        }
        return resultDTO;
    }

    @GetMapping("/check/{phone}")
    @ResponseBody
    public boolean checkPhone(@PathVariable String phone) {
        return userService.checkPhone(phone);
    }

    /* 前后端分离导致跨域无法共享同一个session，需要自己实现获取或nginx设为同域获取其他方法
    这里自己实现一个session
    @GetMapping("/xxx")
    @ResponseBody
    public String xxx(HttpSession session) {
        session.setAttribute("user", "zs");
        return "xxx";
    } */

    @PostMapping("/pwdLogin")
    @ResponseBody
    public ResultDTO pwdLogin(String phone, String password) {
        // 表单验证
        CheckFormat.checkPhone(phone);
        CheckFormat.checkPwd(password);
        Assert.isFlag(checkPhone(phone), "手机号码尚未注册");

        ResultDTO resultDTO = new ResultDTO();
        User user = userService.login(phone, password);
        if (user != null) {
            resultDTO.setCode("1");
            resultDTO.setMessage("登录成功");
            resultDTO.setResult(user);
        } else {
            resultDTO.setCode("0");
            resultDTO.setMessage("用户名或密码错误");
        }
        return resultDTO;
    }

    @PostMapping("/codeLogin")
    @ResponseBody
    public ResultDTO codeLogin(String phone, String code) {
        String realCode = getRealCode("login", phone);
        // 表单验证
        CheckFormat.checkCode(code, realCode);
        Assert.isFlag(checkPhone(phone), "手机号码尚未注册");

        ResultDTO resultDTO = new ResultDTO();
        User user = userService.login(phone, null);
        if (user != null) {
            resultDTO.setCode("1");
            resultDTO.setMessage("登录成功");
            resultDTO.setResult(user);
        } else {
            resultDTO.setCode("0");
            resultDTO.setMessage("用户名或密码错误");
        }
        return resultDTO;
    }

    private String getRealCode(String actionName, String phone) {
        return (String) redisTemplate.opsForValue().get("code:" + actionName + ":" + phone);
    }
}
