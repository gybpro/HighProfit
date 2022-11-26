package com.high.highprofit.controller;

import com.high.highprofit.bean.User;
import com.high.highprofit.dto.ResultDTO;
import com.high.highprofit.exception.ServiceException;
import com.high.highprofit.service.UserService;
import com.high.highprofit.util.Assert;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

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
        checkData(phone, password, code);
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

    private void checkData(String phone, String password, String code) {
        Assert.isEmpty(phone, "手机号码不能为空");
        Assert.isFlag(!phone.matches("^1[3-9]\\d{9}$"), "手机号格式不正确！");
        Assert.isEmpty(password, "密码不能为空！");
        Assert.isFlag(!password.matches("^(?i)(?=.*\\d)(?=.*[a-z]).{6,20}$"), "密码格式不正确！");

        String realCode = (String) redisTemplate.opsForValue().get("code:register:" + phone);
        Assert.isEmpty(code, "验证码不能为空！");
        Assert.isFlag(!realCode.equals(code), "验证码不正确！");
    }
}
