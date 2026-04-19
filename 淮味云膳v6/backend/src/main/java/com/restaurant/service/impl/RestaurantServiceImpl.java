package com.restaurant.service.impl;

import com.restaurant.service.RestaurantService;
import com.restaurant.entity.Restaurant;
import com.restaurant.mapper.RestaurantMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 餐厅信息服务实现
 */
@Service
public class RestaurantServiceImpl implements RestaurantService {
    
    @Autowired
    private RestaurantMapper restaurantMapper;
    
    /**
     * 获取餐厅信息
     * @return 餐厅信息
     */
    @Override
    public Restaurant getInfo() {
        // 假设只有一条餐厅信息记录，获取第一条
        return restaurantMapper.selectList(null).stream().findFirst().orElse(new Restaurant());
    }
    
    /**
     * 保存餐厅信息
     * @param restaurant 餐厅信息
     * @return 是否保存成功
     */
    @Override
    public boolean save(Restaurant restaurant) {
        // 检查是否存在餐厅信息记录
        Restaurant existing = getInfo();
        if (existing.getId() != null) {
            // 更新现有记录
            restaurant.setId(existing.getId());
            return restaurantMapper.updateById(restaurant) > 0;
        } else {
            // 新增记录
            return restaurantMapper.insert(restaurant) > 0;
        }
    }
    
}
