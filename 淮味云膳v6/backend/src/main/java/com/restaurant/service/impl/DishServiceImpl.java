package com.restaurant.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.restaurant.mapper.DishMapper;
import com.restaurant.service.DishService;
import com.restaurant.entity.Dish;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * 菜品Service实现类
 */
@Service
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish> implements DishService {
    
    /**
     * 根据分类ID查询菜品列表
     * @param categoryId 分类ID
     * @return 菜品列表
     */
    @Override
    public List<Dish> getDishesByCategoryId(Long categoryId) {
        QueryWrapper<Dish> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("category_id", categoryId);
        queryWrapper.eq("status", 1); // 只查询起售状态的菜品
        return baseMapper.selectList(queryWrapper);
    }
    
    /**
     * 根据库存更新菜品状态
     * @param dishId 菜品ID
     * @param stock 库存数量
     */
    public void updateStatusByStock(Long dishId, Integer stock) {
        Dish dish = new Dish();
        dish.setId(dishId);
        dish.setStock(stock);
        // 如果库存为0，自动设置为售罄状态
        if (stock <= 0) {
            dish.setStatus(0);
        } else {
            dish.setStatus(1);
        }
        updateById(dish);
    }
    
}