package com.high.highprofit.service;

import com.high.highprofit.mapper.ProductMapper;
import org.apache.dubbo.config.annotation.DubboService;

/**
 * 产品相关业务实现类
 *
 * @author high
 * @version 1.0
 * @since 1.0
 */
@DubboService(version = "1.0.0")
public class ProductServiceImpl implements ProductService{
    private final ProductMapper productMapper;

    public ProductServiceImpl(ProductMapper productMapper) {
        this.productMapper = productMapper;
    }

    @Override
    public String getAvgRate() {
        return productMapper.getAvgRate();
    }
}
