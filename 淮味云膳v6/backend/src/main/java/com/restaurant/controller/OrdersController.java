package com.restaurant.controller;

import com.restaurant.common.Result;
import com.restaurant.service.OrdersService;
import com.restaurant.entity.Orders;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;

/**
 * 订单Controller
 */
@RestController
@RequestMapping("/api/orders")
public class OrdersController {
    
    @Autowired
    private OrdersService ordersService;
    
    /**
     * 提交订单
     * @param orders 订单信息
     * @return 操作结果
     */
    @PostMapping
    public Result<?> submit(@RequestBody Orders orders) {
        try {
            Orders savedOrder = ordersService.submitOrder(orders);
            return Result.success(savedOrder);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    /**
     * 修改订单状态
     * @param orderNo 订单号
     * @param status 订单状态：1-待接单，2-制作中，3-已完成，4-已取消
     * @return 操作结果
     */
    @PutMapping("/status")
    public Result<?> updateStatus(@RequestParam String orderNo, @RequestParam Integer status) {
        boolean updated = ordersService.updateOrderStatus(orderNo, status);
        if (updated) {
            return Result.success(null);
        } else {
            return Result.error("修改订单状态失败");
        }
    }
    
    /**
     * 分页查询订单列表
     * @param page 当前页码
     * @param size 每页条数
     * @return 分页结果
     */
    @GetMapping("/page")
    public Result<?> page(@RequestParam Integer page, @RequestParam Integer size) {
        Page<Orders> ordersPage = ordersService.page(new Page<>(page, size));
        return Result.success(ordersPage);
    }
    
    /**
     * 获取当前用户的订单列表
     * @param request 请求对象，用于获取当前用户的userId
     * @return 订单列表
     */
    @GetMapping("/list")
    public Result<?> list(HttpServletRequest request) {
        // 获取当前用户的userId
        Long userId = (Long) request.getAttribute("userId");
        if (userId == null) {
            return Result.error("未登录或登录已过期");
        }
        // 查询当前用户的订单
        return Result.success(ordersService.listByUserId(userId));
    }
    
    /**
     * 获取订单详细信息
     * @param orderNo 订单号
     * @return 包含明细的订单
     */
    @GetMapping("/{orderNo}")
    public Result<?> getOrderDetails(@PathVariable String orderNo) {
        Orders order = ordersService.getOrderWithDetails(orderNo);
        if (order != null) {
            return Result.success(order);
        } else {
            return Result.error("订单不存在");
        }
    }
    
    /**
     * 获取指定日期的所有订单及其明细
     * @param date 日期 (yyyy-MM-dd)
     * @return 订单列表
     */
    @GetMapping("/daily-details")
    public Result<?> getDailyDetails(@RequestParam String date) {
        return Result.success(ordersService.getDailyOrdersWithDetails(date));
    }
    
}