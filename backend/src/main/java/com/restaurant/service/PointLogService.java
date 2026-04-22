package com.restaurant.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.restaurant.entity.PointLog;

import java.util.List;

public interface PointLogService extends IService<PointLog> {
    
    boolean saveLog(Long memberId, int point, int type, String source);

    List<PointLog> listByMemberId(Long memberId);
}
