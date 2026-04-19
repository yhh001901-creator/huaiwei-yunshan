package com.restaurant.controller;

import com.restaurant.common.Result;
import com.restaurant.service.DishService;
import com.restaurant.entity.Dish;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import java.io.File;
import java.io.IOException;

/**
 * 菜品Controller
 */
@Api(tags = "菜品管理", description = "菜品相关的API接口")
@RestController
@RequestMapping("/api/dish")
public class DishController {
    
    @Autowired
    private DishService dishService;
    
    @Value("${upload.path}")
    private String uploadPath;
    
    /**
     * 新增菜品
     * @param dish 菜品信息
     * @return 操作结果
     */
    @ApiOperation(value = "新增菜品", notes = "添加新的菜品信息")
    @ApiResponses({
        @ApiResponse(code = 200, message = "操作成功"),
        @ApiResponse(code = 500, message = "操作失败")
    })
    @PostMapping
    public Result<?> add(@ApiParam(value = "菜品信息", required = true) @RequestBody Dish dish) {
        boolean saved = dishService.save(dish);
        if (saved) {
            return Result.success(dish);
        } else {
            return Result.error("新增菜品失败");
        }
    }
    
    /**
     * 修改菜品
     * @param dish 菜品信息
     * @return 操作结果
     */
    @ApiOperation(value = "修改菜品", notes = "更新菜品信息")
    @ApiResponses({
        @ApiResponse(code = 200, message = "操作成功"),
        @ApiResponse(code = 500, message = "操作失败")
    })
    @PutMapping
    public Result<?> update(@ApiParam(value = "菜品信息", required = true) @RequestBody Dish dish) {
        // 如果imageUrl为空，则不更新imageUrl字段
        if (dish.getImageUrl() == null) {
            // 获取原有菜品信息
            Dish existingDish = dishService.getById(dish.getId());
            if (existingDish != null) {
                // 保持原有imageUrl
                dish.setImageUrl(existingDish.getImageUrl());
            }
        }
        boolean updated = dishService.updateById(dish);
        if (updated) {
            return Result.success(dish);
        } else {
            return Result.error("修改菜品失败");
        }
    }
    
    /**
     * 删除菜品
     * @param id 菜品ID
     * @return 操作结果
     */
    @ApiOperation(value = "删除菜品", notes = "根据菜品ID删除菜品")
    @ApiResponses({
        @ApiResponse(code = 200, message = "操作成功"),
        @ApiResponse(code = 500, message = "操作失败")
    })
    @DeleteMapping("/{id}")
    public Result<?> delete(@ApiParam(value = "菜品ID", required = true) @PathVariable Long id) {
        boolean deleted = dishService.removeById(id);
        if (deleted) {
            return Result.success(null);
        } else {
            return Result.error("删除菜品失败");
        }
    }
    
    /**
     * 分页查询菜品列表
     * @param page 当前页码
     * @param size 每页条数
     * @return 分页结果
     */
    @ApiOperation(value = "分页查询菜品列表", notes = "分页获取菜品信息")
    @ApiResponses({
        @ApiResponse(code = 200, message = "操作成功"),
        @ApiResponse(code = 500, message = "操作失败")
    })
    @GetMapping("/page")
    public Result<?> page(@ApiParam(value = "当前页码", required = true) @RequestParam Integer page, @ApiParam(value = "每页条数", required = true) @RequestParam Integer size) {
        Page<Dish> dishPage = dishService.page(new Page<>(page, size));
        return Result.success(dishPage);
    }
    
    /**
     * 根据分类ID查询菜品列表
     * @param categoryId 分类ID
     * @return 菜品列表
     */
    @ApiOperation(value = "根据分类ID查询菜品列表", notes = "根据分类ID获取菜品信息")
    @ApiResponses({
        @ApiResponse(code = 200, message = "操作成功"),
        @ApiResponse(code = 500, message = "操作失败")
    })
    @GetMapping("/category/{categoryId}")
    public Result<?> getByCategoryId(@ApiParam(value = "分类ID", required = true) @PathVariable Long categoryId) {
        return Result.success(dishService.getDishesByCategoryId(categoryId));
    }
    
    /**
     * 获取所有菜品列表
     * @return 菜品列表
     */
    @ApiOperation(value = "获取所有菜品列表", notes = "获取所有菜品信息")
    @ApiResponses({
        @ApiResponse(code = 200, message = "操作成功"),
        @ApiResponse(code = 500, message = "操作失败")
    })
    @GetMapping("/list")
    public Result<?> list() {
        return Result.success(dishService.list());
    }
    
    /**
     * 上传菜品图片
     * @param file 图片文件
     * @return 图片访问URL
     */
    @ApiOperation(value = "上传菜品图片", notes = "上传菜品图片并返回访问URL")
    @ApiResponses({
        @ApiResponse(code = 200, message = "操作成功"),
        @ApiResponse(code = 500, message = "操作失败")
    })
    @PostMapping("/upload")
    public Result<?> upload(@ApiParam(value = "图片文件", required = true) @RequestParam("file") MultipartFile file) {
        // 增加日志，打印文件名和目录路径
        System.out.println("收到文件上传请求，文件名: " + file.getOriginalFilename());
        
        // 获取应用根目录的绝对路径
        String basePath = System.getProperty("user.dir");
        System.out.println("应用根目录: " + basePath);
        
        // 构建完整的上传目录路径
        String fullUploadPath = basePath + "/uploads/dishes/";
        File directory = new File(fullUploadPath);
        System.out.println("上传目录绝对路径: " + directory.getAbsolutePath());
        
        if (file.isEmpty()) {
            return Result.error("上传文件不能为空");
        }
        
        // 确保上传目录存在
        if (!directory.exists()) {
            boolean created = directory.mkdirs();
            System.out.println("创建上传目录: " + fullUploadPath + ", 结果: " + created);
        }
        
        // 生成唯一文件名
        String fileName = "dish_" + System.currentTimeMillis() + "_" + file.getOriginalFilename();
        File dest = new File(fullUploadPath + fileName);
        System.out.println("保存文件到: " + dest.getAbsolutePath());
        
        try {
            file.transferTo(dest);
            System.out.println("文件上传成功: " + fileName);
            // 返回图片访问URL - 注意：WebMvcConfig将/uploads/**映射到/uploads/dishes/目录
            String imageUrl = "/uploads/" + fileName;
            System.out.println("返回图片URL: " + imageUrl);
            return Result.success(imageUrl);
        } catch (IOException e) {
            e.printStackTrace();
            return Result.error("上传失败");
        }
    }
    
}