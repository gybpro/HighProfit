package com.high.highprofit.controller;

import com.high.highprofit.bean.User;
import com.high.highprofit.dto.ResultDTO;
import com.high.highprofit.service.UserService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

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

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    @ResponseBody
    public ResultDTO register(String phone, String password, String code) {
        ResultDTO resultDTO = new ResultDTO();
        if (checkData(phone, password, code)) {
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
        } else {
            resultDTO.setCode("0");
            resultDTO.setMessage("注册失败");
        }
        return resultDTO;
    }

    private boolean checkData(String phone, String password, String code) {
        return true;
    }
}
