package com.restaurant.controller;

import com.restaurant.common.Result;
import com.restaurant.service.SystemService;
import com.restaurant.entity.SystemConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 系统配置Controller
 */
@RestController
@RequestMapping("/api/system")
public class SystemController {
    
    @Autowired
    private SystemService systemService;
    
    /**
     * 获取系统配置
     * @return 系统配置
     */
    @GetMapping("/config")
    public Result<?> getConfig() {
        return Result.success(systemService.getConfig());
    }
    
    /**
     * 保存系统配置
     * @param systemConfig 系统配置
     * @return 操作结果
     */
    @PostMapping("/save")
    public Result<?> save(@RequestBody SystemConfig systemConfig) {
        boolean saved = systemService.save(systemConfig);
        if (saved) {
            return Result.success(systemConfig);
        } else {
            return Result.error("保存系统配置失败");
        }
    }
    
}
