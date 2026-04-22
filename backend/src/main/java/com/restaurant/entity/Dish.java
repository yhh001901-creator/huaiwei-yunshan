package com.restaurant.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import java.time.LocalDateTime;
import java.math.BigDecimal;
import lombok.Data;

@Data
@TableName("dish")
public class Dish {
    
    @TableId(type = IdType.AUTO)
    private Long id;
    
    @TableField("category_id")
    private Long categoryId;
    
    private String name;
    
    private BigDecimal price;
    
    @TableField("image_url")
    private String imageUrl;
    
    private String description;
    
    private Integer status;
    
    @TableField("stock")
    private Integer stock;
    
    @TableField("create_time")
    private LocalDateTime createTime;
    
    @TableField("update_time")
    private LocalDateTime updateTime;
    
}