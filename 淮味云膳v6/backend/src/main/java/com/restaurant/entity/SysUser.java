package com.restaurant.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.util.Date;

/**
 * 用户实体类
 */
@Data
@TableName("sys_user")
public class SysUser {
    
    @TableId(type = IdType.AUTO)
    private Long id;
    
    private String username; // 用户名
    
    private String password; // 密码
    
    private String phone; // 手机号
    
    private Integer role; // 角色：0-普通顾客，1-管理员
    
    private Date createTime; // 创建时间
    
    private Date updateTime; // 更新时间
    
}
