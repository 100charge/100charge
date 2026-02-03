package com.xingchuan.charging.domain.resp;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.xingchuan.charging.enums.RefundDetailStatusEnum;
import com.xingchuan.common.config.BigDecimalSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 用户提现审核明细表对象 user_withdrawal_detail
 *
 * @author ruoyi
 */
@Data
@ApiModel(value = "用户提现明细返回")
public class WithdrawalDetailResponse {

    /**
     * id
     */
    @ApiModelProperty(value = "提现明细id")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    /**
     * 充值订单编号
     */
    @ApiModelProperty(value = "充值订单编号")
    private String rechargeOrderNo;

    /**
     * 金额
     */
    @ApiModelProperty(value = "金额")
    @JsonSerialize(using = BigDecimalSerializer.class)
    private BigDecimal amount;

    /**
     * 状态（0：处理中,1：退款成功,2：退款失败）
     */
    @ApiModelProperty(value = "状态（0：处理中,1：退款成功,2：退款失败）")
    private Integer status;

    /**
     * 状态描述（0：处理中,1：退款成功,2：退款失败）
     */
    @ApiModelProperty(value = "状态描述")
    private String statusDesc;

    public String getStatusDesc() {
        return statusDesc = RefundDetailStatusEnum.getDescByCode(status);
    }
}
