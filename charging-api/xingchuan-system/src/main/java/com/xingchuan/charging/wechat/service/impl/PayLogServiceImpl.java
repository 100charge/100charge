package com.xingchuan.charging.wechat.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xingchuan.charging.domain.entity.PayApiLog;
import com.xingchuan.charging.domain.req.PayApiLogPagedRequest;
import com.xingchuan.charging.enums.CallDirectionEnum;
import com.xingchuan.charging.mapper.PayLogMapper;
import com.xingchuan.charging.wechat.service.IPayLogService;
import com.xingchuan.common.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;


@Slf4j
@Service
public class PayLogServiceImpl extends ServiceImpl<PayLogMapper, PayApiLog> implements IPayLogService {


    @Override
    public void saveLogs(PayApiLog apiLog) {
        baseMapper.insert(apiLog);
    }

    @Override
    public Page<PayApiLog> getPagedLogs(PayApiLogPagedRequest request) {
        int pageNumber = request.getPageNumber();
        int pageSize = request.getPageSize();

        LocalDateTime endTime = request.getEndTime();
        LocalDateTime startTime = request.getStartTime();
        CallDirectionEnum callDirection = request.getCallDirection();

        LambdaQueryWrapper<PayApiLog> lambdaQueryWrapper = Wrappers.lambdaQuery();
        if (callDirection != null) {
            lambdaQueryWrapper.eq(PayApiLog::getCallDirection, callDirection.getName());
        }
        if (startTime != null) {
            lambdaQueryWrapper.ge(PayApiLog::getCreateTime, startTime);
        }
        if (endTime != null) {
            lambdaQueryWrapper.le(PayApiLog::getCreateTime, endTime);
        }
        lambdaQueryWrapper.eq(request.getSuccess() != null, PayApiLog::isSuccess, request.getSuccess());
        lambdaQueryWrapper.like(StringUtils.isNotBlank(request.getErrorMsg()), PayApiLog::getErrorMsg, request.getErrorMsg());
        lambdaQueryWrapper.like(StringUtils.isNotBlank(request.getRequestKey()), PayApiLog::getRequest, request.getRequestKey());
        lambdaQueryWrapper.like(StringUtils.isNotBlank(request.getResponseKey()), PayApiLog::getResponse, request.getResponseKey());
        lambdaQueryWrapper.like(StringUtils.isNotBlank(request.getApiAddress()), PayApiLog::getApiAddress, request.getApiAddress());

        return baseMapper.selectPage(new Page<>(pageNumber, pageSize), lambdaQueryWrapper);
    }
}
