package com.restaurant.controller;

import com.restaurant.common.Result;
import com.restaurant.entity.CouponTemplate;
import com.restaurant.service.CouponTemplateService;
import com.restaurant.service.MemberService;
import com.restaurant.entity.Member;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Api(tags = "优惠券模板管理")
@RestController
@RequestMapping("/api/coupon/template")
public class CouponTemplateController {

    @Autowired
    private CouponTemplateService couponTemplateService;

    @Autowired
    private MemberService memberService;

    @ApiOperation("获取可领取的优惠券列表")
    @GetMapping("/list")
    public Result list() {
        List<CouponTemplate> list = couponTemplateService.listAvailable();
        return Result.success(list);
    }

    @PostMapping("/receive/{templateId}")
    public Result receive(@PathVariable Long templateId, HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        Member member = memberService.getInfoByUserId(userId);
        if (member == null) {
            return Result.error("请先开通会员");
        }
        couponTemplateService.receiveCoupon(templateId, member.getId());
        return Result.success("领取成功");
    }
}
