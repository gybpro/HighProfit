package com.high.highprofit.controller;

import com.high.highprofit.service.FinanceAccountService;
import com.high.highprofit.util.Assert;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

/**
 * 财务账户相关控制类
 *
 * @author high
 * @version 1.0
 * @since 1.0
 */
@RestController
@RequestMapping("/account")
@CrossOrigin
public class FinanceAccountController {
    @DubboReference(version = "1.0.0")
    private final FinanceAccountService financeAccountService;

    public FinanceAccountController(FinanceAccountService financeAccountService) {
        this.financeAccountService = financeAccountService;
    }

    @GetMapping("/balance")
    public String balance(@RequestHeader("token") String token) {
        Assert.isEmpty(token, "用户未登录，请前往登录");
        return financeAccountService.getBalance(token).toString();
    }
}
