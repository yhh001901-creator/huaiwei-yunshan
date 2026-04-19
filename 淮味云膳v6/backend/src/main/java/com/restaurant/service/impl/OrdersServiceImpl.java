package com.restaurant.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.restaurant.mapper.OrdersMapper;
import com.restaurant.service.OrdersService;
import com.restaurant.entity.Orders;
import com.restaurant.entity.Dish;
import com.restaurant.mapper.DishMapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.stereotype.Service;
import com.restaurant.entity.OrderDetail;
import com.restaurant.mapper.OrderDetailMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;
import java.util.List;

/**
 * 订单Service实现类
 */
@Service
public class OrdersServiceImpl extends ServiceImpl<OrdersMapper, Orders> implements OrdersService {
    
    @Autowired
    private OrderDetailMapper orderDetailMapper;
    
    @Autowired
    private DishMapper dishMapper;
    
    @Override
    public Orders getOrderWithDetails(String orderNo) {
        QueryWrapper<Orders> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("order_no", orderNo);
        Orders order = getOne(queryWrapper);
        if (order != null) {
            QueryWrapper<OrderDetail> detailWrapper = new QueryWrapper<>();
            detailWrapper.eq("order_no", orderNo);
            List<OrderDetail> details = orderDetailMapper.selectList(detailWrapper);
            order.setOrderDetails(details);
        }
        return order;
    }
    
    @Override
    public List<Orders> getDailyOrdersWithDetails(String date) {
        QueryWrapper<Orders> queryWrapper = new QueryWrapper<>();
        // Assuming create_time is DATETIME, we can use LIKE or DATE() function. 
        // For portability across MySQL versions, using LIKE 'YYYY-MM-DD%' is simple and effective
        queryWrapper.likeRight("create_time", date);
        List<Orders> orders = list(queryWrapper);
        
        if (orders != null && !orders.isEmpty()) {
            for (Orders order : orders) {
                QueryWrapper<OrderDetail> detailWrapper = new QueryWrapper<>();
                detailWrapper.eq("order_no", order.getOrderNo());
                List<OrderDetail> details = orderDetailMapper.selectList(detailWrapper);
                order.setOrderDetails(details);
            }
        }
        return orders;
    }
    
    /**
     * 提交订单
     * @param orders 订单信息
     * @return 订单信息（包含订单号）
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Orders submitOrder(Orders orders) {
        List<OrderDetail> details = orders.getOrderDetails();
        
        if (details != null && !details.isEmpty()) {
            for (OrderDetail detail : details) {
                Dish dish = dishMapper.selectById(detail.getDishId());
                if (dish == null) {
                    throw new RuntimeException("菜品不存在");
                }
                if (dish.getStock() == null || dish.getStock() < detail.getQuantity()) {
                    throw new RuntimeException(dish.getName() + "库存不足");
                }
            }
        }
        
        String orderNo = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")) 
                + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        orders.setOrderNo(orderNo);
        if (orders.getStatus() == null) {
            orders.setStatus(1);
        }
        orders.setCreateTime(LocalDateTime.now());
        orders.setUpdateTime(LocalDateTime.now());
        
        boolean saved = save(orders);
        if (saved) {
            if (details != null && !details.isEmpty()) {
                for (OrderDetail detail : details) {
                    detail.setOrderNo(orderNo);
                    detail.setCreateTime(LocalDateTime.now());
                    detail.setUpdateTime(LocalDateTime.now());
                    orderDetailMapper.insert(detail);
                    
                    Dish existingDish = dishMapper.selectById(detail.getDishId());
                    if (existingDish != null && existingDish.getStock() != null) {
                        int newStock = existingDish.getStock() - detail.getQuantity();
                        Dish dish = new Dish();
                        dish.setId(detail.getDishId());
                        dish.setStock(newStock);
                        if (newStock <= 0) {
                            dish.setStatus(0);
                        }
                        dishMapper.updateById(dish);
                    }
                }
            }
            return orders;
        } else {
            throw new RuntimeException("提交订单失败");
        }
    }
    
    /**
     * 修改订单状态
     * @param orderNo 订单号
     * @param status 订单状态
     * @return 是否修改成功
     */
    @Override
    public boolean updateOrderStatus(String orderNo, Integer status) {
        QueryWrapper<Orders> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("order_no", orderNo);
        
        Orders orders = new Orders();
        orders.setStatus(status);
        orders.setUpdateTime(LocalDateTime.now());
        
        return update(orders, queryWrapper);
    }
    
    /**
     * 根据用户ID获取订单列表
     * @param userId 用户ID
     * @return 订单列表
     */
    @Override
    public List<Orders> listByUserId(Long userId) {
        QueryWrapper<Orders> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);
        queryWrapper.orderByDesc("create_time");
        return list(queryWrapper);
    }
    
    /**
     * 分页查询订单列表
     * @param page 分页对象
     * @return 分页结果
     */
    @Override
    public Page<Orders> page(Page<Orders> page) {
        // 按创建时间降序排列
        return super.page(page, new QueryWrapper<Orders>().orderByDesc("create_time"));
    }
    
}