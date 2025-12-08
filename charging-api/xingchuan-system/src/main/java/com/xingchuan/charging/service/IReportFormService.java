package com.xingchuan.charging.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xingchuan.charging.domain.req.*;
import com.xingchuan.charging.domain.resp.*;

import java.util.List;

/**
 * @Author DYL
 **/
public interface IReportFormService {
    /***
     * 账户充值明细
     */
    Page<AccountRechargeDetailResponse> accountRechargeDetailList(AccountRechargeDetailRequest request);

}
