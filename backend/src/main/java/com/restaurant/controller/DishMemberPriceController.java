package com.restaurant.controller;

import com.restaurant.common.Result;
import com.restaurant.entity.DishMemberPrice;
import com.restaurant.service.DishMemberPriceService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@Api(tags = "菜品会员价管理")
@RestController
@RequestMapping("/api/dish/member-price")
public class DishMemberPriceController {

    @Autowired
    private DishMemberPriceService dishMemberPriceService;

    @ApiOperation("获取所有会员价")
    @GetMapping("/list")
    public Result list() {
        List<DishMemberPrice> list = dishMemberPriceService.listAll();
        return Result.success(list);
    }

    @PostMapping("/save")
    public Result save(@RequestParam Long dishId, @RequestParam BigDecimal memberPrice) {
        dishMemberPriceService.saveOrUpdatePrice(dishId, memberPrice);
        return Result.success("保存成功");
    }
}
