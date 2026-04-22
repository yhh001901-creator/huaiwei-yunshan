package com.restaurant.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.restaurant.entity.Coupon;

import java.math.BigDecimal;
import java.util.List;

public interface CouponService extends IService<Coupon> {

    List<Coupon> listByMemberId(Long memberId);

    boolean useCoupon(Long couponId, String orderNo);

    BigDecimal calculateDiscount(Long couponId, BigDecimal orderAmount);
}
