package com.xingchuan.charging.service;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xingchuan.charging.domain.entity.AppUserBalanceRecord;
import com.xingchuan.charging.domain.resp.UserBalanceRecordListResponse;

/**
 * 用户余额变动记录Service接口
 */
public interface IAppUserBalanceRecordService extends IService<AppUserBalanceRecord> {

    /**
     * 小程序用户余额变动列表
     */
    Page<UserBalanceRecordListResponse> pageList(Integer type, String phone);

    /**
     * 小程序用户余额充值记录列表
     */
    Page<UserBalanceRecordListResponse> rechargeRecordList(String openId);
}
