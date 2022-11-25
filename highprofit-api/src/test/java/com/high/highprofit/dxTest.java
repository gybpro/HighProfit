package com.high.highprofit;

import com.aliyun.dysmsapi20170525.models.SendSmsRequest;
import com.aliyun.dysmsapi20170525.models.SendSmsResponse;
import com.aliyun.dysmsapi20170525.Client;
import com.aliyun.teaopenapi.models.Config;
import com.aliyun.teautil.Common;
import com.aliyun.teautil.models.RuntimeOptions;
import org.junit.jupiter.api.Test;

/**
 * 阿里短信验证码测试
 *
 * @author high
 * @version 1.0
 * @since 1.0
 */
public class dxTest {
    @Test
    public void testDx() throws Exception {
        // 随机生成4为验证码
        StringBuilder code = new StringBuilder();
        for (int i = 0; i < 4; i++) {
            code.append((int) (Math.random() * 10));
        }
        Config config = new Config()
                // 必填，您的 AccessKey ID
                .setAccessKeyId("AccessKey ID")
                // 必填，您的 AccessKey Secret
                .setAccessKeySecret("AccessKey Secret");
        // 访问的域名
        config.endpoint = "dysmsapi.aliyuncs.com";
        // 初始化 Client，采用 AK&SK 鉴权访问的方式，此方式可能会存在泄漏风险，建议使用 STS 方式。更多鉴权访问方式请参考：https://help.aliyun.com/document_detail/378657.html
        Client client = new Client(config);
        SendSmsRequest sendSmsRequest = new SendSmsRequest()
                .setSignName("阿里云短信测试")
                .setTemplateCode("SMS_154950909")
                .setPhoneNumbers("测试手机号")
                // String.format: 参数1：需要格式化的字符串，参数2：参数1中的第1个%s
                .setTemplateParam("{\"code\":\"" + code + "\"}");
        RuntimeOptions runtime = new RuntimeOptions();
        SendSmsResponse resp = client.sendSmsWithOptions(sendSmsRequest, runtime);
        String result = Common.toJSONString(resp.getBody());
        System.out.println(result);
    }
}
