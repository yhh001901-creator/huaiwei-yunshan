package com.restaurant.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.restaurant.entity.Coupon;
import com.restaurant.entity.CouponTemplate;
import com.restaurant.mapper.CouponMapper;
import com.restaurant.mapper.CouponTemplateMapper;
import com.restaurant.service.CouponService;
import com.restaurant.service.CouponTemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class CouponTemplateServiceImpl extends ServiceImpl<CouponTemplateMapper, CouponTemplate> implements CouponTemplateService {

    @Autowired
    private CouponService couponService;

    @Override
    public List<CouponTemplate> listAvailable() {
        LambdaQueryWrapper<CouponTemplate> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CouponTemplate::getStatus, 1)
               .gt(CouponTemplate::getRemainCount, 0);
        return list(wrapper);
    }

    @Override
    @Transactional
    public boolean receiveCoupon(Long templateId, Long memberId) {
        CouponTemplate template = getById(templateId);
        if (template == null || template.getStatus() != 1) {
            throw new RuntimeException("优惠券不可领取");
        }
        if (template.getRemainCount() > 0) {
            template.setRemainCount(template.getRemainCount() - 1);
            updateById(template);
        }

        Coupon coupon = new Coupon();
        coupon.setMemberId(memberId);
        coupon.setTemplateId(templateId);
        coupon.setStatus(0);
        if (template.getExpireDate() != null) {
            coupon.setExpireDate(template.getExpireDate());
        } else {
            coupon.setExpireDate(LocalDate.now().plusDays(template.getValidDays()));
        }
        return couponService.save(coupon);
    }
}
