package com.restaurant.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import java.time.LocalDateTime;
import java.math.BigDecimal;
import lombok.Data;

@Data
@TableName("order_detail")
public class OrderDetail {
    
    @TableId(type = IdType.AUTO)
    private Long id;
    
    @TableField("order_no")
    private String orderNo;
    
    @TableField("dish_id")
    private Long dishId;
    
    @TableField("dish_name")
    private String dishName;
    
    @TableField("unit_price")
    private BigDecimal unitPrice;
    
    private Integer quantity;
    
    @TableField("create_time")
    private LocalDateTime createTime;
    
    @TableField("update_time")
    private LocalDateTime updateTime;
    
}