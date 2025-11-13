package com.xingchuan.charging.domain.resp;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 首页-场站首页
 */
@Data
@ApiModel(value = "首页-场站首页")
public class DashboardOperationDataResponse {

    /**
     * 年度充电量
     */
    private BigDecimal annualChargingVolume = BigDecimal.ZERO;

    /**
     * 年度充电次数
     */
    private Integer annualChargingCount = 0;

    /**
     * 年营收
     */
    private BigDecimal annualRevenue = BigDecimal.ZERO;

    /**
     * 月度充电次数
     */
    private Integer monthlyChargingCount = 0;

    /**
     * 月度充电量
     */
    private BigDecimal monthlyChargingVolume = BigDecimal.ZERO;

    /**
     * 月度营收
     */
    private BigDecimal monthlyRevenue = BigDecimal.ZERO;

    /**
     * 今日充电次数
     */
    private Integer todayChargingCount = 0;

    /**
     * 今日充电量
     */
    private BigDecimal todayChargingVolume = BigDecimal.ZERO;

    /**
     * 今日营收
     */
    private BigDecimal todayRevenue = BigDecimal.ZERO;

}
