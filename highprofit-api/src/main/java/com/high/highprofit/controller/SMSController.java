package com.high.highprofit.controller;

import com.aliyun.dysmsapi20170525.Client;
import com.aliyun.dysmsapi20170525.models.SendSmsRequest;
import com.aliyun.dysmsapi20170525.models.SendSmsResponse;
import com.aliyun.teaopenapi.models.Config;
import com.aliyun.teautil.Common;
import com.aliyun.teautil.models.RuntimeOptions;
import com.high.highprofit.config.SMSConfig;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.TimeUnit;

/**
 * 短信验证码相关控制类
 *
 * @author high
 * @version 1.0
 * @since 1.0
 */
@Controller
@RequestMapping("/sms")
@CrossOrigin
public class SMSController {
    private final SMSConfig smsConfig;

    private final RedisTemplate<String, Object> redisTemplate;

    public SMSController(SMSConfig smsConfig, RedisTemplate<String, Object> redisTemplate) {
        this.smsConfig = smsConfig;
        this.redisTemplate = redisTemplate;
    }

    @GetMapping("/register/{phone}")
    @ResponseBody
    @CrossOrigin
    public String register(@PathVariable String phone) throws Exception {
        return sendCode("register", phone);
    }

    @GetMapping("/login/{phone}")
    @ResponseBody
    @CrossOrigin
    public String login(@PathVariable String phone) throws Exception {
        return sendCode("login", phone);
    }

    private String sendCode(String actionName, String phone) {
        // 随机生成4为验证码
        StringBuilder code = new StringBuilder();
        for (int i = 0; i < 4; i++) {
            code.append((int) (Math.random() * 10));
        }

        redisTemplate.opsForValue().set("code:" + actionName + ":" + phone, code, 5, TimeUnit.MINUTES);

        /* 阿里短信验证码测试
        Config config = new Config()
                // 必填，您的 AccessKey ID
                .setAccessKeyId(smsConfig.getAccessKeyId())
                // 必填，您的 AccessKey Secret
                .setAccessKeySecret(smsConfig.getAccessKeySecret());
        // 访问的域名
        config.endpoint = smsConfig.getEndpoint();
        // 初始化 Client，采用 AK&SK 鉴权访问的方式，此方式可能会存在泄漏风险，建议使用 STS 方式。更多鉴权访问方式请参考：https://help.aliyun.com/document_detail/378657.html
        Client client = new Client(config);
        SendSmsRequest sendSmsRequest = new SendSmsRequest()
                .setSignName(smsConfig.getSignName())
                .setTemplateCode(smsConfig.getTemplateCode())
                .setPhoneNumbers(phone)
                // String.format: 参数1：需要格式化的字符串，参数2：参数1中的第1个%s
                .setTemplateParam(String.format(smsConfig.getTemplateParam(), code));
        RuntimeOptions runtime = new RuntimeOptions();
        SendSmsResponse resp = client.sendSmsWithOptions(sendSmsRequest, runtime);
        String result = Common.toJSONString(resp.getBody());
        System.out.println(result); */
        return code.toString();
    }
}
