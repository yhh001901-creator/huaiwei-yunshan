package com.restaurant.service.impl;

import com.restaurant.service.SystemService;
import com.restaurant.entity.SystemConfig;
import com.restaurant.mapper.SystemMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 系统配置服务实现
 */
@Service
public class SystemServiceImpl implements SystemService {
    
    @Autowired
    private SystemMapper systemMapper;
    
    /**
     * 获取系统配置
     * @return 系统配置
     */
    @Override
    public SystemConfig getConfig() {
        // 假设只有一条系统配置记录，获取第一条
        return systemMapper.selectList(null).stream().findFirst().orElse(new SystemConfig());
    }
    
    /**
     * 保存系统配置
     * @param systemConfig 系统配置
     * @return 是否保存成功
     */
    @Override
    public boolean save(SystemConfig systemConfig) {
        // 检查是否存在系统配置记录
        SystemConfig existing = getConfig();
        if (existing.getId() != null) {
            // 更新现有记录
            systemConfig.setId(existing.getId());
            return systemMapper.updateById(systemConfig) > 0;
        } else {
            // 新增记录
            return systemMapper.insert(systemConfig) > 0;
        }
    }
    
}
