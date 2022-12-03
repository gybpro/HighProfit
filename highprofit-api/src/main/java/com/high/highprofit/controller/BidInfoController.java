package com.high.highprofit.controller;

import com.high.highprofit.bean.BidInfo;
import com.high.highprofit.dto.ResultDTO;
import com.high.highprofit.service.BidInfoService;
import com.high.highprofit.util.Assert;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 投资记录相关控制类
 *
 * @author high
 * @version 1.0
 * @since 1.0
 */
@RestController
@RequestMapping("/bidInfo")
@CrossOrigin
public class BidInfoController {
    @DubboReference(version = "1.0.0")
    private final BidInfoService bidInfoService;

    public BidInfoController(BidInfoService bidInfoService) {
        this.bidInfoService = bidInfoService;
    }

    @GetMapping("/totalMoney")
    public String totalMoney() {
        return bidInfoService.getTotalMoney();
    }

    @GetMapping("/top3")
    public List<Map<String, Object>> top3() {
        return bidInfoService.getTop3();
    }

    @GetMapping("/record")
    public List<BidInfo> record(@RequestHeader(required = false) String token) {
        return bidInfoService.getLatelyRecord(token);
    }

    @GetMapping("/record/{prodId}")
    public List<BidInfo> record(@PathVariable Integer prodId) {
        return bidInfoService.getRecordByProdId(prodId);
    }

    @PostMapping("invest/{prodId}/{bidMoney}")
    public ResultDTO invest(@PathVariable Integer prodId,
                            @PathVariable String bidMoney,
                            @RequestHeader String token) {
        ResultDTO resultDTO = new ResultDTO();
        if (bidInfoService.invest(prodId, bidMoney, token)) {
            resultDTO.setCode("1");
            resultDTO.setMessage("投资成功");
        } else {
            resultDTO.setCode("0");
            resultDTO.setMessage("系统异常");
        }
        return resultDTO;
    }
}
