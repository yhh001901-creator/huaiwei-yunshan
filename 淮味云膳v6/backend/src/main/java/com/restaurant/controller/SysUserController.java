package com.restaurant.controller;

import com.restaurant.common.Result;
import com.restaurant.service.SysUserService;
import com.restaurant.entity.SysUser;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 用户管理Controller
 */
@RestController
@RequestMapping("/api/user")
public class SysUserController {
    
    @Autowired
    private SysUserService sysUserService;
    
    /**
     * 新增用户
     * @param sysUser 用户信息
     * @return 操作结果
     */
    @PostMapping
    public Result<?> add(@RequestBody SysUser sysUser) {
        boolean saved = sysUserService.save(sysUser);
        if (saved) {
            return Result.success(sysUser);
        } else {
            return Result.error("新增用户失败");
        }
    }
    
    /**
     * 修改用户
     * @param sysUser 用户信息
     * @return 操作结果
     */
    @PutMapping
    public Result<?> update(@RequestBody SysUser sysUser) {
        boolean updated = sysUserService.updateById(sysUser);
        if (updated) {
            return Result.success(sysUser);
        } else {
            return Result.error("修改用户失败");
        }
    }
    
    /**
     * 删除用户
     * @param id 用户ID
     * @return 操作结果
     */
    @DeleteMapping("/{id}")
    public Result<?> delete(@PathVariable Long id) {
        boolean deleted = sysUserService.removeById(id);
        if (deleted) {
            return Result.success(null);
        } else {
            return Result.error("删除用户失败");
        }
    }
    
    /**
     * 分页查询用户列表
     * @param page 当前页码
     * @param size 每页条数
     * @return 分页结果
     */
    @GetMapping("/page")
    public Result<?> page(@RequestParam Integer page, @RequestParam Integer size) {
        Page<SysUser> userPage = sysUserService.page(new Page<>(page, size));
        return Result.success(userPage);
    }
    
    /**
     * 获取所有用户列表
     * @return 用户列表
     */
    @GetMapping("/list")
    public Result<?> list() {
        return Result.success(sysUserService.list());
    }
    
    /**
     * 获取当前用户信息
     * @return 当前用户信息
     */
    @GetMapping("/profile")
    public Result<?> getProfile() {
        // 这里应该从会话中获取当前用户ID，暂时使用固定ID 1
        Long userId = 1L;
        SysUser user = sysUserService.getById(userId);
        if (user != null) {
            return Result.success(user);
        } else {
            return Result.error("获取用户信息失败");
        }
    }
    
    /**
     * 更新用户信息
     * @param user 用户信息
     * @return 操作结果
     */
    @PutMapping("/profile")
    public Result<?> updateProfile(@RequestBody SysUser user) {
        // 这里应该从会话中获取当前用户ID，暂时使用固定ID 1
        Long userId = 1L;
        user.setId(userId);
        boolean updated = sysUserService.updateById(user);
        if (updated) {
            return Result.success(user);
        } else {
            return Result.error("更新用户信息失败");
        }
    }
    
    /**
     * 修改密码
     * @param passwordInfo 密码信息
     * @return 操作结果
     */
    @PutMapping("/password")
    public Result<?> updatePassword(@RequestBody PasswordInfo passwordInfo) {
        // 这里应该从会话中获取当前用户ID，暂时使用固定ID 1
        Long userId = 1L;
        SysUser user = sysUserService.getById(userId);
        if (user != null) {
            // 简单验证旧密码（实际项目中应该加密验证）
            if (user.getPassword().equals(passwordInfo.getOldPassword())) {
                user.setPassword(passwordInfo.getNewPassword());
                boolean updated = sysUserService.updateById(user);
                if (updated) {
                    return Result.success(null);
                } else {
                    return Result.error("修改密码失败");
                }
            } else {
                return Result.error("旧密码错误");
            }
        } else {
            return Result.error("用户不存在");
        }
    }
    
    // 密码信息类
    static class PasswordInfo {
        private String oldPassword;
        private String newPassword;
        
        public String getOldPassword() {
            return oldPassword;
        }
        
        public void setOldPassword(String oldPassword) {
            this.oldPassword = oldPassword;
        }
        
        public String getNewPassword() {
            return newPassword;
        }
        
        public void setNewPassword(String newPassword) {
            this.newPassword = newPassword;
        }
    }
    
}
