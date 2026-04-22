package com.restaurant.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.restaurant.entity.Member;

public interface MemberService extends IService<Member> {
    
    Member getInfoByUserId(Long userId);

    Member activateMember(Long userId);

    boolean updateProfile(Long userId, Member member);

    boolean checkin(Long userId);

    boolean addPoint(Long userId, int point, int type, String source);
}
