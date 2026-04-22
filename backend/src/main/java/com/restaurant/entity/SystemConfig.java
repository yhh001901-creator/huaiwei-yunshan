package com.restaurant.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.util.Date;

/**
 * 系统配置实体类
 */
@Data
@TableName("system_config")
public class SystemConfig {
    
    @TableId(type = IdType.AUTO)
    private Long id;
    
    private String systemName; // 系统名称
    
    private String version; // 系统版本
    
    private String databaseUrl; // 数据库连接
    
    private Integer port; // 服务器端口
    
    private String aiApiKey; // DeepSeek R1 API Key
    
    private Date createTime; // 创建时间
    
    private Date updateTime; // 更新时间
    
}
