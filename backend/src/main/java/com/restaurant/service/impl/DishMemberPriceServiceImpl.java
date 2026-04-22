package com.restaurant.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.restaurant.entity.DishMemberPrice;
import com.restaurant.mapper.DishMemberPriceMapper;
import com.restaurant.service.DishMemberPriceService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class DishMemberPriceServiceImpl extends ServiceImpl<DishMemberPriceMapper, DishMemberPrice> implements DishMemberPriceService {

    @Override
    public List<DishMemberPrice> listAll() {
        return list();
    }

    @Override
    public boolean saveOrUpdatePrice(Long dishId, BigDecimal memberPrice) {
        LambdaQueryWrapper<DishMemberPrice> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(DishMemberPrice::getDishId, dishId);
        DishMemberPrice existing = getOne(wrapper);
        
        if (existing != null) {
            existing.setMemberPrice(memberPrice);
            return updateById(existing);
        } else {
            DishMemberPrice price = new DishMemberPrice();
            price.setDishId(dishId);
            price.setMemberPrice(memberPrice);
            return save(price);
        }
    }

    @Override
    public BigDecimal getMemberPrice(Long dishId) {
        LambdaQueryWrapper<DishMemberPrice> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(DishMemberPrice::getDishId, dishId);
        DishMemberPrice price = getOne(wrapper);
        return price != null ? price.getMemberPrice() : null;
    }
}
