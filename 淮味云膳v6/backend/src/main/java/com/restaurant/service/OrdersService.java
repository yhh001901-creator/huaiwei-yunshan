package com.restaurant.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.restaurant.entity.Orders;

/**
 * 订单Service接口
 */
public interface OrdersService extends IService<Orders> {
    
    /**
     * 提交订单
     * @param orders 订单信息
     * @return 订单信息（包含订单号）
     */
    Orders submitOrder(Orders orders);
    
    /**
     * 修改订单状态
     * @param orderNo 订单号
     * @param status 订单状态
     * @return 是否修改成功
     */
    boolean updateOrderStatus(String orderNo, Integer status);
    
    /**
     * 获取订单明细包含菜品
     * @param orderNo 订单号
     * @return 订单对象结构
     */
    Orders getOrderWithDetails(String orderNo);
    
    /**
     * 获取某日所有包含明细的订单
     * @param date 日期字符串, 格式 yyyy-MM-dd
     * @return 订单列表
     */
    java.util.List<Orders> getDailyOrdersWithDetails(String date);
    
    /**
     * 根据用户ID获取订单列表
     * @param userId 用户ID
     * @return 订单列表
     */
    java.util.List<Orders> listByUserId(Long userId);
    
    /**
     * 分页查询订单列表
     * @param page 分页对象
     * @return 分页结果
     */
    Page<Orders> page(Page<Orders> page);
    
}