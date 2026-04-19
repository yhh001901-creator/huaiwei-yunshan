package com.restaurant.service;

import com.restaurant.entity.SysUser;
import com.baomidou.mybatisplus.extension.service.IService;

public interface SysUserService extends IService<SysUser> {
    
    SysUser getByUsername(String username);
}
