package com.restaurant.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.restaurant.entity.Coupon;
import com.restaurant.entity.CouponTemplate;
import com.restaurant.mapper.CouponMapper;
import com.restaurant.mapper.CouponTemplateMapper;
import com.restaurant.service.CouponService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
public class CouponServiceImpl extends ServiceImpl<CouponMapper, Coupon> implements CouponService {

    @Autowired
    private CouponTemplateMapper couponTemplateMapper;

    @Override
    public List<Coupon> listByMemberId(Long memberId) {
        LambdaQueryWrapper<Coupon> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Coupon::getMemberId, memberId)
               .ne(Coupon::getStatus, 2)
               .orderByAsc(Coupon::getExpireDate);
        return list(wrapper);
    }

    @Override
    public boolean useCoupon(Long couponId, String orderNo) {
        Coupon coupon = getById(couponId);
        if (coupon == null || coupon.getStatus() != 0) {
            throw new RuntimeException("优惠券不可使用");
        }
        if (coupon.getExpireDate() != null && coupon.getExpireDate().isBefore(LocalDate.now())) {
            throw new RuntimeException("优惠券已过期");
        }
        coupon.setStatus(1);
        coupon.setUseTime(java.time.LocalDateTime.now());
        coupon.setOrderNo(orderNo);
        return updateById(coupon);
    }

    @Override
    public BigDecimal calculateDiscount(Long couponId, BigDecimal orderAmount) {
        Coupon coupon = getById(couponId);
        if (coupon == null) {
            return BigDecimal.ZERO;
        }
        if (coupon.getStatus() != 0) {
            return BigDecimal.ZERO;
        }
        if (coupon.getExpireDate() != null && coupon.getExpireDate().isBefore(LocalDate.now())) {
            return BigDecimal.ZERO;
        }

        CouponTemplate template = couponTemplateMapper.selectById(coupon.getTemplateId());
        if (template == null) {
            return BigDecimal.ZERO;
        }

        BigDecimal discount = BigDecimal.ZERO;

        if (template.getType() == 1) {
            if (orderAmount.compareTo(template.getMinAmount()) >= 0) {
                discount = template.getDiscountValue();
            }
        } else if (template.getType() == 2) {
            BigDecimal originalDiscount = orderAmount.multiply(BigDecimal.ONE.subtract(template.getDiscountValue()));
            if (orderAmount.compareTo(template.getMinAmount()) >= 0) {
                discount = originalDiscount.setScale(2, BigDecimal.ROUND_HALF_UP);
            }
        }

        if (discount.compareTo(orderAmount) > 0) {
            discount = orderAmount;
        }

        return discount;
    }
}
