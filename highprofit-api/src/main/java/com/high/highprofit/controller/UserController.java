package com.high.highprofit.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.high.highprofit.bean.User;
import com.high.highprofit.dto.ResultDTO;
import com.high.highprofit.dto.VerifyDTO;
import com.high.highprofit.service.UserService;
import com.high.highprofit.util.Assert;
import com.high.highprofit.util.CheckFormat;
import com.high.highprofit.util.HttpClientUtils;
import com.high.highprofit.util.UUIDUtil;
import org.apache.dubbo.config.annotation.DubboReference;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

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
        if (userService.registerUser(phone, password) > 0) {
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

        User user = userService.login(phone, password);
        return loginPostProcess(new ResultDTO(), user);
    }

    @PostMapping("/codeLogin")
    @ResponseBody
    public ResultDTO codeLogin(String phone, String code) {
        String realCode = getRealCode("login", phone);
        // 表单验证
        CheckFormat.checkCode(code, realCode);
        Assert.isFlag(checkPhone(phone), "手机号码尚未注册");

        User user = userService.login(phone, null);
        return loginPostProcess(new ResultDTO(), user);
    }

    @PostMapping("/verify")
    @ResponseBody
    // public ResultDTO verify(@RequestBody Map<String, String> map,
    public ResultDTO verify(String idCard, String name,
                            @RequestHeader("token") String token) throws Exception {
        Map<String, Object> params = new HashMap<>();
        params.put("idcard", idCard);
        params.put("name", name);
        params.put("appkey", "jd申请的appkey");
        String result = HttpClientUtils.doPost("https://way.jd.com/lundear/idcard", params);
        // 检查结果
        // Jackson提供的对象映射工具(json字符串 <==> Java对象)
        ObjectMapper objectMapper = new ObjectMapper();
        // readValue(json字符串, .class类型) json字符串 => Java对象
        VerifyDTO verifyDTO = objectMapper.readValue(result, VerifyDTO.class);
        ResultDTO resultDTO = new ResultDTO();
        if ("0".equals(verifyDTO.getResult().get("code") + "")) {
            // 认证成功
            // 更新令牌
            ValueOperations<String, Object> ops = redisTemplate.opsForValue();
            User user = (User) ops.get(token);
            if (user != null) {
                if (userService.verify(user.getId(), idCard, name) > 0) {
                    user = userService.login(user.getPhone(), user.getLoginPassword());
                    ops.set(token, user, 30, TimeUnit.MINUTES);
                    resultDTO.setCode("1");
                    resultDTO.setMessage("实名认证成功");
                } else {
                    resultDTO.setCode("0");
                    resultDTO.setMessage("系统忙，请稍后再试......");
                }
            } else {
                resultDTO.setCode("2");
                resultDTO.setMessage("登录已过期，请重新登录");
            }
        } else {
            resultDTO.setCode("0");
            resultDTO.setMessage("身份证信息错误");
        }
        return resultDTO;
    }

    private String getRealCode(String actionName, String phone) {
        return (String) redisTemplate.opsForValue().get("code:" + actionName + ":" + phone);
    }

    @NotNull
    private ResultDTO loginPostProcess(ResultDTO resultDTO, User user) {
        if (user != null) {
            // 更新登录时间
            if (userService.logLastLoginTime(user.getId()) > 0) {
                // 为客户端分配令牌token
                // 生成随机令牌
                String token = UUIDUtil.getUUID().toUpperCase();
                // 将令牌存入Redis，后续用户可通过令牌登录
                redisTemplate.opsForValue().set(token, user, 30, TimeUnit.MINUTES);
                Map<String, Object> map = new HashMap<>();
                map.put("user", user);
                map.put("token", token);
                resultDTO.setCode("1");
                resultDTO.setMessage("登录成功");
                resultDTO.setResult(map);
            } else {
                resultDTO.setCode("0");
                resultDTO.setMessage("系统忙，请稍后再试......");
            }
        } else {
            resultDTO.setCode("0");
            resultDTO.setMessage("用户名或密码错误");
        }
        return resultDTO;
    }
}
