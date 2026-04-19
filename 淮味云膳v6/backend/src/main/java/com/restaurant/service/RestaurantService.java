package com.restaurant.service;

import com.restaurant.entity.Restaurant;

/**
 * 餐厅信息服务接口
 */
public interface RestaurantService {
    
    /**
     * 获取餐厅信息
     * @return 餐厅信息
     */
    Restaurant getInfo();
    
    /**
     * 保存餐厅信息
     * @param restaurant 餐厅信息
     * @return 是否保存成功
     */
    boolean save(Restaurant restaurant);
    
}
