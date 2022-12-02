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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
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
@RestController
@RequestMapping("/user")
@CrossOrigin
public class UserController {
    @DubboReference(version = "1.0.0")
    private final UserService userService;

    private final RedisTemplate<String, Object> redisTemplate;

    private final String savePath;

    private final String saveAddress;

    public UserController(UserService userService,
                          RedisTemplate<String, Object> redisTemplate,
                          @Value("${userPic.savePath}") String savePath,
                          @Value("${userPic.saveAddress}") String saveAddress) {
        this.userService = userService;
        this.redisTemplate = redisTemplate;
        this.savePath = savePath;
        this.saveAddress = saveAddress;
    }

    @PostMapping("/register")
    public ResultDTO register(String phone, String password, String code) {
        // 表单验证
        String realCode = getRealCode("register", phone);
        // 表单验证
        CheckFormat.checkCode(code, realCode);
        CheckFormat.checkPhone(phone);
        CheckFormat.checkPwd(password);
        Assert.isFlag(checkPhone(phone), "手机号码已被注册");

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
    public ResultDTO pwdLogin(String phone, String password) {
        // 表单验证
        CheckFormat.checkPhone(phone);
        CheckFormat.checkPwd(password);
        Assert.isFlag(!checkPhone(phone), "手机号码尚未注册");

        User user = userService.login(phone, password);
        return loginPostProcess(new ResultDTO(), user);
    }

    @PostMapping("/codeLogin")
    public ResultDTO codeLogin(String phone, String code) {
        String realCode = getRealCode("login", phone);
        // 表单验证
        CheckFormat.checkCode(code, realCode);
        Assert.isFlag(!checkPhone(phone), "手机号码尚未注册");

        User user = userService.login(phone, null);
        return loginPostProcess(new ResultDTO(), user);
    }

    @GetMapping("/checkLogin")
    public ResultDTO checkLogin(@RequestHeader(required = false) String token) {
        Assert.isEmpty(token, "用户未登录，请前往登录");
        Assert.isFlag(redisTemplate.opsForValue().get(token) != null, "登录超时，请重新登录");
        ResultDTO resultDTO = new ResultDTO();
        resultDTO.setCode("1");
        resultDTO.setMessage("已登录，可访问");
        return resultDTO;
    }

    @PostMapping("/verify")
    // public ResultDTO verify(@RequestBody Map<String, String> map,
    public ResultDTO verify(String idCard, String name,
                            @RequestHeader(required = false) String token) throws Exception {
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
                    user = userService.login(user.getPhone(), null);
                    ops.set(token, user, 30, TimeUnit.MINUTES);
                    resultDTO.setCode("1");
                    resultDTO.setMessage("实名认证成功");
                    Map<String, Object> map = new HashMap<>();
                    map.put("user", user);
                    resultDTO.setResult(map);
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

    @GetMapping("/logout")
    public ResultDTO logout(@RequestHeader(required = false) String token) {
        ResultDTO resultDTO = new ResultDTO();
        redisTemplate.opsForValue().set(token, "", 0);
        resultDTO.setCode("1");
        resultDTO.setMessage("退出成功");
        return resultDTO;
    }

    @GetMapping("/userCount")
    public String userCount() {
        return userService.getUserCount();
    }

    @PostMapping("/upload")
    // MultipartFile传输文件对象
    public String upload(MultipartFile userPic, @RequestHeader(required = false) String token) throws IOException {
        User user = (User) redisTemplate.opsForValue().get(token);
        Assert.isFlag(user != null, "用户未登录，请前往登录");
        Integer id = user.getId();

        // 文件存储目录路径
        File savePathFile = new File(savePath);
        boolean mkdirs = savePathFile.mkdirs();// 如果文件不存在，则创建

        // 存储目标文件路径
        File file = new File(savePathFile, id.toString());
        // 将文件传输到存储目标文件路径
        userPic.transferTo(file);

        String headerImage = saveAddress + "user/img/" + id;

        if (userService.updatePic(id, headerImage)) {
            return headerImage;
        }
        return "";
    }

    @GetMapping("/img/{id}")
    public void img(@PathVariable String id, HttpServletResponse response) throws Exception {
        InputStream in = new FileInputStream(savePath + id);
        OutputStream out = response.getOutputStream();
        byte[] bytes = new byte[1024];
        int len;
        while ((len = in.read(bytes)) != -1) {
            out.write(bytes, 0, len);
        }
        out.close();
        in.close();
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
