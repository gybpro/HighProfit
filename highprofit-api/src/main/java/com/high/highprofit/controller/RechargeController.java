package com.high.highprofit.controller;

import com.high.highprofit.bean.Recharge;
import com.high.highprofit.service.RechargeService;
import com.high.highprofit.util.Assert;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 充值信息相关控制类
 *
 * @author high
 * @version 1.0
 * @since 1.0
 */
@RestController
@RequestMapping("/recharge")
@CrossOrigin
public class RechargeController {
    @DubboReference(version = "1.0.0")
    private final RechargeService rechargeService;

    public RechargeController(RechargeService rechargeService) {
        this.rechargeService = rechargeService;
    }

    @GetMapping("/record")
    public List<Recharge> record(@RequestHeader(required = false) String token) {
        return rechargeService.getLatelyRecord(token);
    }
}
