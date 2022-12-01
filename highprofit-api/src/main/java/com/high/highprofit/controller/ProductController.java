package com.high.highprofit.controller;

import com.high.highprofit.bean.Product;
import com.high.highprofit.service.ProductService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 产品相关控制
 *
 * @author high
 * @version 1.0
 * @since 1.0
 */
@RestController
@RequestMapping("/product")
@CrossOrigin
public class ProductController {
    @DubboReference(version = "1.0.0")
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/rate")
    public String rate() {
        return productService.getAvgRate();
    }

    @GetMapping("/newUserPro")
    public Product newUserPro() {
        return productService.getNewUserPro();
    }

    @GetMapping("/prePro/{cycle}")
    public Product prePro(@PathVariable Integer cycle) {
        return productService.getPrePro(cycle);
    }

    @GetMapping("/scatter")
    public List<Product> scatter() {
        return productService.getScatter();
    }

    @GetMapping("/pagingQuery/{pageNum}/{pageSize}/{proType}")
    public Object pagingQuery(@PathVariable("pageNum") Integer pageNum,
                              @PathVariable("pageSize") Integer pageSize,
                              @PathVariable("proType") Integer proType) {
        return productService.pagingQuery(pageNum, pageSize, proType);
    }
}
