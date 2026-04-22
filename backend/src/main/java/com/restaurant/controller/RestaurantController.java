package com.restaurant.controller;

import com.restaurant.common.Result;
import com.restaurant.service.RestaurantService;
import com.restaurant.entity.Restaurant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 餐厅信息Controller
 */
@RestController
@RequestMapping("/api/restaurant")
public class RestaurantController {
    
    @Autowired
    private RestaurantService restaurantService;
    
    /**
     * 获取餐厅信息
     * @return 餐厅信息
     */
    @GetMapping("/info")
    public Result<?> getInfo() {
        return Result.success(restaurantService.getInfo());
    }
    
    /**
     * 保存餐厅信息
     * @param restaurant 餐厅信息
     * @return 操作结果
     */
    @PostMapping("/save")
    public Result<?> save(@RequestBody Restaurant restaurant) {
        boolean saved = restaurantService.save(restaurant);
        if (saved) {
            return Result.success(restaurant);
        } else {
            return Result.error("保存餐厅信息失败");
        }
    }
    
}
