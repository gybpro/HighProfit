package com.high.highprofit.util;

/**
 * 表单验证工具类
 *
 * @author high
 * @version 1.0
 * @since 1.0
 */
public class CheckFormat {
    private CheckFormat() {}

    public static void checkPhone(String phone) {
        Assert.isEmpty(phone, "手机号码不能为空");
        Assert.isFlag(!phone.matches("^1[3-9]\\d{9}$"), "手机号格式不正确！");
    }

    public static void checkPwd(String password) {
        Assert.isEmpty(password, "密码不能为空！");
        Assert.isFlag(!password.matches("^(?i)(?=.*\\d)(?=.*[a-z]).{6,20}$"), "密码格式不正确！");
    }

    public static void checkCode(String code, String realCode) {
        Assert.isEmpty(code, "验证码不能为空！");
        Assert.isFlag(!code.equals(realCode), "验证码不正确！");
    }
}
