package com.high.highprofit.controller;

import com.high.highprofit.bean.Income;
import com.high.highprofit.service.IncomeService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 收益信息相关控制类
 *
 * @author high
 * @version 1.0
 * @since 1.0
 */
@RestController
@RequestMapping("income")
@CrossOrigin
public class IncomeController {
    @DubboReference(version = "1.0.0")
    private final IncomeService incomeService;

    public IncomeController(IncomeService incomeService) {
        this.incomeService = incomeService;
    }

    @GetMapping("record")
    public List<Income> record(@RequestHeader(required = false) String token) {
        return incomeService.getLateLyRecord(token);
    }
}
