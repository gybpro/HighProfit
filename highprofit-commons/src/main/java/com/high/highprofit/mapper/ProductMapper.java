package com.high.highprofit.mapper;

import com.high.highprofit.bean.Product;

import java.util.List;

public interface ProductMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Product row);

    int insertSelective(Product row);

    Product selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Product row);

    int updateByPrimaryKey(Product row);

    String selectAvgRate();

    Product selectNewUserPro();

    Product selectPrePro(Integer cycle);

    List<Product> selectScatter();

    List<Product> selectByProductType(Integer proType);
}
