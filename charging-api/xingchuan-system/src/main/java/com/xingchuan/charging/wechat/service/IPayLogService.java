package com.xingchuan.charging.wechat.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xingchuan.charging.domain.entity.PayApiLog;
import com.xingchuan.charging.domain.req.PayApiLogPagedRequest;

/**
 * 日志服务
 */
public interface IPayLogService {
    /**
     * 保存日志
     *
     * @param apiLog 日志实体
     */
    void saveLogs(PayApiLog apiLog);

    /**
     * 获取分页列表
     *
     * @param request 请求参数
     */
    Page<PayApiLog> getPagedLogs(PayApiLogPagedRequest request);
}
