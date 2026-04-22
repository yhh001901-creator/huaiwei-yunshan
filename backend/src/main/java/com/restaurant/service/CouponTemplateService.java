package com.restaurant.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.restaurant.entity.CouponTemplate;

import java.util.List;

public interface CouponTemplateService extends IService<CouponTemplate> {
    
    List<CouponTemplate> listAvailable();

    boolean receiveCoupon(Long templateId, Long memberId);
}
