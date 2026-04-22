package com.restaurant.controller;

import com.restaurant.common.Result;
import com.restaurant.service.AiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ai")
public class AiController {

    @Autowired
    private AiService aiService;

    @GetMapping("/analyze-orders")
    public Result<?> analyzeOrders() {
        try {
            String result = aiService.analyzeOrders();
            return Result.success(result);
        } catch (Exception e) {
            return Result.error(500, e.getMessage());
        }
    }
}
