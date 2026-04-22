package com.restaurant.controller;

import com.restaurant.common.Result;
import com.restaurant.entity.Coupon;
import com.restaurant.entity.Member;
import com.restaurant.service.CouponService;
import com.restaurant.service.MemberService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Api(tags = "优惠券管理")
@RestController
@RequestMapping("/api/coupon")
public class CouponController {

    @Autowired
    private CouponService couponService;

    @Autowired
    private MemberService memberService;

    @ApiOperation("我的优惠券列表")
    @GetMapping("/my/list")
    public Result myList(HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        Member member = memberService.getInfoByUserId(userId);
        if (member == null) {
            return Result.success();
        }
        List<Coupon> list = couponService.listByMemberId(member.getId());
        return Result.success(list);
    }
}
