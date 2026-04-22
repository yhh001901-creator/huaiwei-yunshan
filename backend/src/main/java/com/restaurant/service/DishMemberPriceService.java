package com.restaurant.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.restaurant.entity.DishMemberPrice;

import java.util.List;

public interface DishMemberPriceService extends IService<DishMemberPrice> {

    List<DishMemberPrice> listAll();

    boolean saveOrUpdatePrice(Long dishId, java.math.BigDecimal memberPrice);

    java.math.BigDecimal getMemberPrice(Long dishId);
}
