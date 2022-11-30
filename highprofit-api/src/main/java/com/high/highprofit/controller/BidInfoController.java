package com.high.highprofit.controller;

import com.high.highprofit.service.BidInfoService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 投资记录相关控制类
 *
 * @author high
 * @version 1.0
 * @since 1.0
 */
@Controller
@RequestMapping("/bidInfo")
@CrossOrigin
public class BidInfoController {
    @DubboReference(version = "1.0.0")
    private final BidInfoService bidInfoService;

    public BidInfoController(BidInfoService bidInfoService) {
        this.bidInfoService = bidInfoService;
    }

    @GetMapping("/totalMoney")
    @ResponseBody
    public String totalMoney() {
        return bidInfoService.getTotalMoney();
    }
}
