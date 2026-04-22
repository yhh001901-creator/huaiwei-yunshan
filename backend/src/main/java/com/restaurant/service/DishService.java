package com.restaurant.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.restaurant.entity.Dish;
import java.util.List;

/**
 * 菜品Service接口
 */
public interface DishService extends IService<Dish> {
    
    /**
     * 根据分类ID查询菜品列表
     * @param categoryId 分类ID
     * @return 菜品列表
     */
    List<Dish> getDishesByCategoryId(Long categoryId);
    
    /**
     * 根据库存更新菜品状态
     * @param dishId 菜品ID
     * @param stock 库存数量
     */
    void updateStatusByStock(Long dishId, Integer stock);
    
}