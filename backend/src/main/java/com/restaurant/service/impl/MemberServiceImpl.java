package com.restaurant.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.restaurant.entity.Member;
import com.restaurant.mapper.MemberMapper;
import com.restaurant.service.MemberService;
import com.restaurant.service.PointLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class MemberServiceImpl extends ServiceImpl<MemberMapper, Member> implements MemberService {

    @Autowired
    private PointLogService pointLogService;

    @Override
    public Member getInfoByUserId(Long userId) {
        LambdaQueryWrapper<Member> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Member::getUserId, userId);
        return getOne(wrapper);
    }

    @Override
    public Member activateMember(Long userId) {
        Member member = getInfoByUserId(userId);
        if (member == null) {
            throw new RuntimeException("会员档案不存在");
        }
        member.setIsMember(1);
        member.setMemberExpire(null);
        updateById(member);
        return member;
    }

    @Override
    public boolean updateProfile(Long userId, Member member) {
        Member existing = getInfoByUserId(userId);
        if (existing == null) {
            throw new RuntimeException("会员档案不存在");
        }
        existing.setNickname(member.getNickname());
        existing.setAvatar(member.getAvatar());
        existing.setBirthday(member.getBirthday());
        existing.setGender(member.getGender());
        return updateById(existing);
    }

    @Override
    public boolean checkin(Long userId) {
        Member member = getInfoByUserId(userId);
        if (member == null) {
            throw new RuntimeException("会员档案不存在");
        }
        int checkinPoint = 10;
        member.setPoint(member.getPoint() + checkinPoint);
        updateById(member);
        pointLogService.saveLog(member.getId(), checkinPoint, 2, "每日签到");
        return true;
    }

    @Override
    public boolean addPoint(Long userId, int point, int type, String source) {
        Member member = getInfoByUserId(userId);
        if (member == null) {
            return false;
        }
        member.setPoint(member.getPoint() + point);
        updateById(member);
        pointLogService.saveLog(member.getId(), point, type, source);
        return true;
    }
}
