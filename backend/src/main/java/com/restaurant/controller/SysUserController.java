package com.restaurant.controller;

import com.restaurant.common.Result;
import com.restaurant.service.SysUserService;
import com.restaurant.entity.SysUser;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;

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
     * @param request 请求对象，用于获取当前用户的userId
     * @return 当前用户信息
     */
    @GetMapping("/profile")
    public Result<?> getProfile(HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        if (userId == null) {
            return Result.error("未登录或登录已过期");
        }
        SysUser user = sysUserService.getById(userId);
        if (user != null) {
            user.setPassword(null);
            return Result.success(user);
        } else {
            return Result.error("获取用户信息失败");
        }
    }
    
    /**
     * 更新用户信息
     * @param user 用户信息
     * @param request 请求对象，用于获取当前用户的userId
     * @return 操作结果
     */
    @PutMapping("/profile")
    public Result<?> updateProfile(@RequestBody SysUser user, HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        if (userId == null) {
            return Result.error("未登录或登录已过期");
        }
        user.setId(userId);
        user.setPassword(null);
        user.setRole(null);
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
     * @param request 请求对象，用于获取当前用户的userId
     * @return 操作结果
     */
    @PutMapping("/password")
    public Result<?> updatePassword(@RequestBody PasswordInfo passwordInfo, HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        if (userId == null) {
            return Result.error("未登录或登录已过期");
        }
        SysUser user = sysUserService.getById(userId);
        if (user != null) {
            if (user.getPassword().equals(passwordInfo.getOldPassword())) {
                user.setPassword(passwordInfo.getNewPassword());
                user.setUpdateTime(new java.util.Date());
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
