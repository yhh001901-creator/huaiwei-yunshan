package com.restaurant.controller;

import com.restaurant.common.Result;
import com.restaurant.service.CategoryService;
import com.restaurant.entity.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 菜品分类Controller
 */
@RestController
@RequestMapping("/api/category")
public class CategoryController {
    
    @Autowired
    private CategoryService categoryService;
    
    /**
     * 获取所有分类列表
     * @return 分类列表
     */
    @GetMapping("/list")
    public Result<?> list() {
        return Result.success(categoryService.list());
    }
    
    /**
     * 新增分类
     * @param category 分类信息
     * @return 操作结果
     */
    @PostMapping
    public Result<?> add(@RequestBody Category category) {
        boolean saved = categoryService.save(category);
        if (saved) {
            return Result.success(category);
        } else {
            return Result.error("新增分类失败");
        }
    }
    
    /**
     * 修改分类
     * @param category 分类信息
     * @return 操作结果
     */
    @PutMapping
    public Result<?> update(@RequestBody Category category) {
        boolean updated = categoryService.updateById(category);
        if (updated) {
            return Result.success(category);
        } else {
            return Result.error("修改分类失败");
        }
    }
    
    /**
     * 删除分类
     * @param id 分类ID
     * @return 操作结果
     */
    @DeleteMapping("/{id}")
    public Result<?> delete(@PathVariable Long id) {
        boolean deleted = categoryService.removeById(id);
        if (deleted) {
            return Result.success(null);
        } else {
            return Result.error("删除分类失败");
        }
    }
    
}
