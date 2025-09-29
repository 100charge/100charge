package com.xingchuan.charging.domain.resp;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.xingchuan.common.config.BigDecimalSerializer;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * app用户新增对象
 *
 * @author ruoyi
 */
@Data
public class AppUserBalanceResponse {

    /**
     * 剩余余额
     */
    @ApiModelProperty("剩余余额")
    @JsonSerialize(using = BigDecimalSerializer.class)
    private BigDecimal balance;

    /**
     * 支出金额
     */
    @ApiModelProperty("支出金额")
    @JsonSerialize(using = BigDecimalSerializer.class)
    private BigDecimal consumptionTotal;

    /**
     * 充值金额
     */
    @ApiModelProperty("充值金额")
    @JsonSerialize(using = BigDecimalSerializer.class)
    private BigDecimal rechargeTotal;
    /**
     * 余额明细
     */
    @ApiModelProperty("余额明细")
    private List<AppUserBalanceDetailsResponse> detailsResponseList;

}
