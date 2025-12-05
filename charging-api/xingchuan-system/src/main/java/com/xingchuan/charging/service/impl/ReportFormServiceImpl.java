package com.xingchuan.charging.service.impl;

import com.xingchuan.charging.enums.*;
import com.xingchuan.charging.domain.req.*;
import com.xingchuan.charging.domain.resp.*;
import com.xingchuan.charging.mapper.AppUserBalanceRecordMapper;
import com.xingchuan.charging.mapper.ChargingOrderMapper;
import com.xingchuan.charging.service.IReportFormService;
import com.xingchuan.common.utils.PageUtils;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
@Service
public class ReportFormServiceImpl implements IReportFormService {

    @Resource
    private ChargingOrderMapper chargingOrderMapper;
    @Resource
    private AppUserBalanceRecordMapper appUserBalanceRecordMapper;



    /***
     * 账户充值明细
     */
    @Override
    public Page<AccountRechargeDetailResponse> accountRechargeDetailList(AccountRechargeDetailRequest request) {
        Page<AccountRechargeDetailResponse> pageInfo = PageUtils.getPageInfo();
        return appUserBalanceRecordMapper.accountRechargeList(pageInfo, request, AppUserBalanceRecordEnum.RECHARGE.getCode());
    }
}
