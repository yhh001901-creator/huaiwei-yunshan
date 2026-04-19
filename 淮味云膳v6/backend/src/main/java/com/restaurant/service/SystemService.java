package com.restaurant.service;

import com.restaurant.entity.SystemConfig;

/**
 * 系统配置服务接口
 */
public interface SystemService {
    
    /**
     * 获取系统配置
     * @return 系统配置
     */
    SystemConfig getConfig();
    
    /**
     * 保存系统配置
     * @param systemConfig 系统配置
     * @return 是否保存成功
     */
    boolean save(SystemConfig systemConfig);
    
}
