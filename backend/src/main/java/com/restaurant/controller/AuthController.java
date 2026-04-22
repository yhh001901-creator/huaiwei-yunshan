package com.restaurant.controller;

import com.restaurant.common.Result;
import com.restaurant.dto.LoginDTO;
import com.restaurant.dto.LoginVO;
import com.restaurant.dto.RegisterDTO;
import com.restaurant.entity.SysUser;
import com.restaurant.enums.UserRole;
import com.restaurant.service.SysUserService;
import com.restaurant.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/login")
    public Result<?> login(@RequestBody LoginDTO loginDTO) {
        SysUser user = sysUserService.getByUsername(loginDTO.getUsername());
        
        if (user == null) {
            return Result.error("用户不存在");
        }
        
        if (!user.getPassword().equals(loginDTO.getPassword())) {
            return Result.error("密码错误");
        }

        String token = jwtUtil.generateToken(user.getId(), user.getUsername(), user.getRole());
        
        LoginVO loginVO = new LoginVO();
        loginVO.setUserId(user.getId());
        loginVO.setUsername(user.getUsername());
        loginVO.setPhone(user.getPhone());
        loginVO.setRole(user.getRole());
        loginVO.setRoleName(UserRole.getByCode(user.getRole()).getName());
        loginVO.setToken(token);
        
        return Result.success(loginVO);
    }

    @PostMapping("/register")
    public Result<?> register(@RequestBody RegisterDTO registerDTO) {
        // 检查用户名是否已存在
        SysUser existingUser = sysUserService.getByUsername(registerDTO.getUsername());
        if (existingUser != null) {
            return Result.error("用户名已存在");
        }
        
        // 创建新用户，默认角色为顾客（0）
        SysUser newUser = new SysUser();
        newUser.setUsername(registerDTO.getUsername());
        newUser.setPassword(registerDTO.getPassword());
        newUser.setPhone(registerDTO.getPhone());
        newUser.setRole(0); // 默认角色为顾客
        newUser.setCreateTime(new Date());
        newUser.setUpdateTime(new Date());
        
        boolean saved = sysUserService.save(newUser);
        if (!saved) {
            return Result.error("注册失败，请稍后重试");
        }
        
        // 注册成功后自动登录
        String token = jwtUtil.generateToken(newUser.getId(), newUser.getUsername(), newUser.getRole());
        
        LoginVO loginVO = new LoginVO();
        loginVO.setUserId(newUser.getId());
        loginVO.setUsername(newUser.getUsername());
        loginVO.setPhone(newUser.getPhone());
        loginVO.setRole(newUser.getRole());
        loginVO.setRoleName(UserRole.getByCode(newUser.getRole()).getName());
        loginVO.setToken(token);
        
        return Result.success(loginVO);
    }

    @GetMapping("/info")
    public Result<?> getUserInfo(HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        if (userId == null) {
            return Result.error("未登录");
        }
        
        SysUser user = sysUserService.getById(userId);
        if (user == null) {
            return Result.error("用户不存在");
        }
        
        LoginVO loginVO = new LoginVO();
        loginVO.setUserId(user.getId());
        loginVO.setUsername(user.getUsername());
        loginVO.setPhone(user.getPhone());
        loginVO.setRole(user.getRole());
        loginVO.setRoleName(UserRole.getByCode(user.getRole()).getName());
        
        return Result.success(loginVO);
    }

    @PostMapping("/logout")
    public Result<?> logout() {
        return Result.success(null);
    }
}
