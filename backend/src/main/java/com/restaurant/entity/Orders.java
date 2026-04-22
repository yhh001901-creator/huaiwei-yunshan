package com.restaurant.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import java.time.LocalDateTime;
import java.math.BigDecimal;
import lombok.Data;

@Data
@TableName("orders")
public class Orders {
    
    @TableId(type = IdType.AUTO)
    private Long id;
    
    @TableField("order_no")
    private String orderNo;
    
    @TableField("user_id")
    private Long userId;
    
    @TableField("total_amount")
    private BigDecimal totalAmount;
    
    private Integer status;
    
    @TableField("table_no")
    private String tableNo;
    
    @TableField("create_time")
    private LocalDateTime createTime;
    
    @TableField("update_time")
    private LocalDateTime updateTime;
    
    @TableField(exist = false)
    private java.util.List<OrderDetail> orderDetails;
    
}