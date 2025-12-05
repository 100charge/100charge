package com.xingchuan.charging.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xingchuan.charging.domain.entity.AppUserBalanceRecord;
import com.xingchuan.charging.domain.req.*;
import com.xingchuan.charging.domain.resp.*;
import org.apache.ibatis.annotations.Param;

/**
 * 用户余额变动记录Mapper接口
 *
 * @author ruoyi
 */
public interface AppUserBalanceRecordMapper extends BaseMapper<AppUserBalanceRecord> {

    /***
     * 账户充值明细-分页
     */
    Page<AccountRechargeDetailResponse> accountRechargeList(Page<AccountRechargeDetailResponse> pageInfo,
                                                            @Param("request") AccountRechargeDetailRequest request,
                                                            @Param("type") Integer type);

}
