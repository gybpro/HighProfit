package com.high.highprofit.controller;

import com.high.highprofit.bean.Recharge;
import com.high.highprofit.bean.User;
import com.high.highprofit.pay.Pkipair;
import com.high.highprofit.service.RechargeService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.UUID;

/**
 * 支付相关控制类
 *
 * @author high
 * @version 1.0
 * @since 1.0
 */
@Controller
public class PayController {
    @DubboReference(version = "1.0.0")
    private final RechargeService rechargeService;

    private final RedisTemplate<String, Object> redisTemplate;

    public PayController(RechargeService rechargeService, RedisTemplate<String, Object> redisTemplate) {
        this.rechargeService = rechargeService;
        this.redisTemplate = redisTemplate;
    }

    @GetMapping("pay")
    public String pay(String token, String money, HttpServletRequest request) throws UnsupportedEncodingException {
        // 人民币网关账号，该账号为11位人民币网关商户编号+01,该参数必填。
        String merchantAcctId = "1001214035601"; //
        // 编码方式，1代表 UTF-8; 2 代表 GBK; 3代表 GB2312 默认为1,该参数必填。
        String inputCharset = "1";
        // 接收支付结果的页面地址，该参数一般置为空即可。
        String pageUrl = "";
        // 服务器接收支付结果的后台地址，该参数务必填写，不能为空。
        String bgUrl = "http://sf6wx6.natappfree.cc/notice";
        // 网关版本，固定值：v2.0,该参数必填。
        String version = "v2.0";
        // 语言种类，1代表中文显示，2代表英文显示。默认为1,该参数必填。
        String language = "1";
        // 签名类型,该值为4，代表PKI加密方式,该参数必填。
        String signType = "4";

        User user = (User) redisTemplate.opsForValue().get(token);

        // 支付人姓名,可以为空。
        String payerName = user.getName();
        // 支付人联系类型，1 代表电子邮件方式；2 代表手机联系方式。可以为空。
        String payerContactType = "2";
        // 支付人联系方式，与payerContactType设置对应，payerContactType为1，则填写邮箱地址；payerContactType为2，则填写手机号码。可以为空。
        String payerContact = user.getPhone();
        // 指定付款人，可以为空,3 代表付款方在商户方的会员编号
        String payerIdType = "3";
        // 付款人标识，可以为空
        String payerId = user.getId().toString();
        // 付款人IP，可以为空
        String payerIP = request.getRemoteAddr();
        // 商户订单号，以下采用时间来定义订单号，商户可以根据自己订单号的定义规则来定义该值，不能为空。
        String orderId = UUID.randomUUID().toString().replace("-", "");
        // 订单金额，金额以“分”为单位，商户测试以1分测试即可，切勿以大金额测试。该参数必填。
        BigDecimal rechargeMoney = new BigDecimal(money);
        BigDecimal realMoney = rechargeMoney.multiply(BigDecimal.valueOf(100)); // 界面输入的金额单位是元，因此需要乘100
        String orderAmount = realMoney.intValue() + "";
        // 订单提交时间，格式：yyyyMMddHHmmss，如：20071117020101，不能为空。
        String orderTime = new java.text.SimpleDateFormat("yyyyMMddHHmmss").format(new java.util.Date());
        // 快钱时间戳，格式：yyyyMMddHHmmss，如：20071117020101， 可以为空
        String orderTimestamp = new java.text.SimpleDateFormat("yyyyMMddHHmmss").format(new java.util.Date());

        // 商品名称，可以为空。
        String productName = "盈利宝充值";
        // 商品数量，可以为空。
        String productNum = "1";
        // 商品代码，可以为空。
        String productId = "10000";
        // 商品描述，可以为空。
        String productDesc = "盈利宝充值";
        // 扩展字段1，商户可以传递自己需要的参数，支付完快钱在通知时会原值返回，可以为空。
        String ext1 = user.getId().toString();
        // 扩展自段2，商户可以传递自己需要的参数，支付完快钱会原值返回，可以为空。
        String ext2 = "扩展2";
        // 支付方式，一般为00，代表所有的支付方式。如果是银行直连商户，该值为10-1或10-2，必填。
        String payType = "00";
        // 银行代码，如果payType为00，该值可以为空；如果payType为10-1或10-2，该值必须填写，具体请参考银行列表。
        String bankId = "";
        // 同一订单禁止重复提交标志，实物购物车填1，虚拟产品用0。1代表只能提交一次，0代表在支付不成功情况下可以再提交。可为空。
        String redoFlag = "0";
        // 快钱合作伙伴的帐户号，即商户编号，可为空。
        String pid = "";

        // signMsg 签名字符串 不可空，生成加密签名串
        String signMsgVal = "";
        signMsgVal = appendParam(signMsgVal, "inputCharset", inputCharset);
        signMsgVal = appendParam(signMsgVal, "pageUrl", pageUrl);
        signMsgVal = appendParam(signMsgVal, "bgUrl", bgUrl);
        signMsgVal = appendParam(signMsgVal, "version", version);
        signMsgVal = appendParam(signMsgVal, "language", language);
        signMsgVal = appendParam(signMsgVal, "signType", signType);
        signMsgVal = appendParam(signMsgVal, "merchantAcctId", merchantAcctId);
        signMsgVal = appendParam(signMsgVal, "payerName", payerName);
        signMsgVal = appendParam(signMsgVal, "payerContactType", payerContactType);
        signMsgVal = appendParam(signMsgVal, "payerContact", payerContact);
        signMsgVal = appendParam(signMsgVal, "payerIdType", payerIdType);
        signMsgVal = appendParam(signMsgVal, "payerId", payerId);
        signMsgVal = appendParam(signMsgVal, "payerIP", payerIP);
        signMsgVal = appendParam(signMsgVal, "orderId", orderId);
        signMsgVal = appendParam(signMsgVal, "orderAmount", orderAmount);
        signMsgVal = appendParam(signMsgVal, "orderTime", orderTime);
        signMsgVal = appendParam(signMsgVal, "orderTimestamp", orderTimestamp);
        signMsgVal = appendParam(signMsgVal, "productName", productName);
        signMsgVal = appendParam(signMsgVal, "productNum", productNum);
        signMsgVal = appendParam(signMsgVal, "productId", productId);
        signMsgVal = appendParam(signMsgVal, "productDesc", productDesc);
        signMsgVal = appendParam(signMsgVal, "ext1", ext1);
        signMsgVal = appendParam(signMsgVal, "ext2", ext2);
        signMsgVal = appendParam(signMsgVal, "payType", payType);
        signMsgVal = appendParam(signMsgVal, "bankId", bankId);
        signMsgVal = appendParam(signMsgVal, "redoFlag", redoFlag);
        signMsgVal = appendParam(signMsgVal, "pid", pid);

        System.out.println(signMsgVal);
        Pkipair pki = new Pkipair();
        // 商家生成签名，这个签名在快钱方需要进行认证，确保支付安全
        String signMsg = pki.signMsg(signMsgVal);

        String url = "https://sandbox.99bill.com/gateway/recvMerchantInfoAction.htm";

        url += "?inputCharset=" + URLEncoder.encode(inputCharset, StandardCharsets.UTF_8);
        url += "&pageUrl=" + URLEncoder.encode(pageUrl, StandardCharsets.UTF_8);
        url += "&bgUrl=" + URLEncoder.encode(bgUrl, StandardCharsets.UTF_8);
        url += "&version=" + URLEncoder.encode(version, StandardCharsets.UTF_8);
        url += "&language=" + URLEncoder.encode(language, StandardCharsets.UTF_8);
        url += "&signType=" + URLEncoder.encode(signType, StandardCharsets.UTF_8);
        url += "&signMsg=" + URLEncoder.encode(signMsg, StandardCharsets.UTF_8);
        url += "&merchantAcctId=" + URLEncoder.encode(merchantAcctId, StandardCharsets.UTF_8);
        url += "&payerName=" + URLEncoder.encode(payerName, StandardCharsets.UTF_8);
        url += "&payerContactType=" + URLEncoder.encode(payerContactType, StandardCharsets.UTF_8);
        url += "&payerContact=" + URLEncoder.encode(payerContact, StandardCharsets.UTF_8);
        url += "&payerIdType=" + URLEncoder.encode(payerIdType, StandardCharsets.UTF_8);
        url += "&payerId=" + URLEncoder.encode(payerId, StandardCharsets.UTF_8);
        url += "&payerIP=" + URLEncoder.encode(payerIP, StandardCharsets.UTF_8);
        url += "&orderId=" + URLEncoder.encode(orderId, StandardCharsets.UTF_8);
        url += "&orderAmount=" + URLEncoder.encode(orderAmount, StandardCharsets.UTF_8);
        url += "&orderTime=" + URLEncoder.encode(orderTime, StandardCharsets.UTF_8);
        url += "&orderTimestamp=" + URLEncoder.encode(orderTimestamp, StandardCharsets.UTF_8);
        url += "&productName=" + URLEncoder.encode(productName, StandardCharsets.UTF_8);
        url += "&productNum=" + URLEncoder.encode(productNum, StandardCharsets.UTF_8);
        url += "&productId=" + URLEncoder.encode(productId, StandardCharsets.UTF_8);
        url += "&productDesc=" + URLEncoder.encode(productDesc, StandardCharsets.UTF_8);
        url += "&ext1=" + URLEncoder.encode(ext1, StandardCharsets.UTF_8);
        url += "&ext2=" + URLEncoder.encode(ext2, StandardCharsets.UTF_8);
        url += "&payType=" + URLEncoder.encode(payType, StandardCharsets.UTF_8);
        url += "&bankId=" + URLEncoder.encode(bankId, StandardCharsets.UTF_8);
        url += "&redoFlag=" + URLEncoder.encode(redoFlag, StandardCharsets.UTF_8);
        url += "&pid=" + URLEncoder.encode(pid, StandardCharsets.UTF_8);

        // 添加充值记录：状态为充值中
        Recharge record = new Recharge();
        record.setUid(user.getId());
        record.setRechargeNo(orderId);
        record.setRechargeStatus(0);
        record.setRechargeMoney(new BigDecimal(money));
        record.setRechargeTime(new Date());
        record.setRechargeDesc(productDesc);
        rechargeService.add(record);

        return "redirect:" + url;
    }

    public String appendParam(String returns, String paramId, String paramValue) {
        if (!"".equals(returns)) {
            if (!"".equals(paramValue)) {
                returns += "&" + paramId + "=" + paramValue;
            }
        } else {
            if (!"".equals(paramValue)) {
                returns = paramId + "=" + paramValue;
            }
        }
        return returns;
    }
}
