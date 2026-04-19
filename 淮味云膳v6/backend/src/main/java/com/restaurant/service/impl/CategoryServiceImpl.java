package com.restaurant.service.impl;

import com.restaurant.service.CategoryService;
import com.restaurant.entity.Category;
import com.restaurant.mapper.CategoryMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * 菜品分类服务实现
 */
@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {
    
}
