package com.restaurant.controller;

import com.restaurant.common.Result;
import com.restaurant.entity.Member;
import com.restaurant.entity.SysUser;
import com.restaurant.service.MemberService;
import com.restaurant.service.PointLogService;
import com.restaurant.service.SysUserService;
import com.restaurant.utils.JwtUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Api(tags = "会员管理")
@RestController
@RequestMapping("/api/member")
public class MemberController {

    @Autowired
    private MemberService memberService;

    @Autowired
    private PointLogService pointLogService;

    @Autowired
    private SysUserService sysUserService;

    @ApiOperation("获取当前用户会员信息")
    @GetMapping("/info")
    public Result getInfo(HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        Member member = memberService.getInfoByUserId(userId);
        if (member == null) {
            member = new Member();
            member.setUserId(userId);
            member.setPoint(0);
            member.setIsMember(0);
            SysUser user = sysUserService.getById(userId);
            if (user != null) {
                member.setPhone(user.getPhone());
                member.setNickname(user.getUsername());
            }
            memberService.save(member);
        }
        return Result.success(member);
    }

    @ApiOperation("开通会员")
    @PostMapping("/activate")
    public Result activate(HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        Member member = memberService.activateMember(userId);
        return Result.success(member);
    }

    @ApiOperation("更新会员资料")
    @PutMapping("/profile")
    public Result updateProfile(HttpServletRequest request, @RequestBody Member member) {
        Long userId = (Long) request.getAttribute("userId");
        memberService.updateProfile(userId, member);
        return Result.success("更新成功");
    }

    @ApiOperation("每日签到")
    @PostMapping("/checkin")
    public Result checkin(HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        memberService.checkin(userId);
        Map<String, Object> data = new HashMap<>();
        data.put("point", 10);
        data.put("msg", "签到成功");
        return Result.success(data);
    }

    @ApiOperation("获取积分流水")
    @GetMapping("/point/list")
    public Result pointList(HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        Member member = memberService.getInfoByUserId(userId);
        if (member == null) {
            return Result.success(new HashMap<String, Object>());
        }
        List logs = pointLogService.listByMemberId(member.getId());
        return Result.success(logs);
    }
    
    @ApiOperation("获取所有会员列表")
    @GetMapping("/list")
    public Result list() {
        List<Member> members = memberService.list();
        return Result.success(members);
    }
}
