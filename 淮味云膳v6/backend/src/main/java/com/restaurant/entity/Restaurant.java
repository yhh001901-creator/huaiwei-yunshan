package com.restaurant.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.util.Date;

/**
 * 餐厅信息实体类
 */
@Data
@TableName("restaurant")
public class Restaurant {
    
    @TableId(type = IdType.AUTO)
    private Long id;
    
    private String name; // 餐厅名称
    
    private String phone; // 联系电话
    
    private String address; // 餐厅地址
    
    private String openTime; // 营业时间
    
    private String closeTime; // 打烊时间
    
    private String description; // 餐厅简介
    
    private Date createTime; // 创建时间
    
    private Date updateTime; // 更新时间
    
}
