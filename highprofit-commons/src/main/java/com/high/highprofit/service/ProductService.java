package com.high.highprofit.service;

import com.high.highprofit.bean.Product;

import java.util.List;

/**
 * 产品相关业务接口
 *
 * @author high
 * @version 1.0
 * @since 1.0
 */
public interface ProductService {
    String getAvgRate();

    Product getNewUserPro();

    Product getPrePro(Integer cycle);

    List<Product> getScatter();
}
