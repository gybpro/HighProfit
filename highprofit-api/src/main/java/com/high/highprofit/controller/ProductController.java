package com.high.highprofit.controller;

import com.high.highprofit.service.ProductService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 产品相关控制
 *
 * @author high
 * @version 1.0
 * @since 1.0
 */
@Controller
@RequestMapping("/product")
public class ProductController {
    @DubboReference(version = "1.0.0")
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/rate")
    @ResponseBody
    @CrossOrigin
    public String getAvgRate() {
        return productService.getAvgRate();
    }
}
