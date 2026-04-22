package com.restaurant.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.restaurant.entity.PointLog;
import com.restaurant.mapper.PointLogMapper;
import com.restaurant.service.PointLogService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PointLogServiceImpl extends ServiceImpl<PointLogMapper, PointLog> implements PointLogService {

    @Override
    public boolean saveLog(Long memberId, int point, int type, String source) {
        PointLog log = new PointLog();
        log.setMemberId(memberId);
        log.setPoint(point);
        log.setType(type);
        log.setSource(source);
        return save(log);
    }

    @Override
    public List<PointLog> listByMemberId(Long memberId) {
        LambdaQueryWrapper<PointLog> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(PointLog::getMemberId, memberId)
               .orderByDesc(PointLog::getCreateTime);
        return list(wrapper);
    }
}
