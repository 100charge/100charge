package com.xingchuan.charging.domain.resp;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.xingchuan.common.annotation.Excel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;

@Data
@ApiModel(value = "账户充值明细响应参数")
public class AccountRechargeDetailResponse {

    /**
     * 订单号
     */
    @Excel(name = "订单号")
    @ApiModelProperty(value = "订单号")
    private String tradeNo;

    /**
     * 用户唯一标识
     */
    @Excel(name = "用户唯一标识")
    @ApiModelProperty(value = "用户唯一标识")
    private String openId;

    /**
     * 手机号
     */
    @Excel(name = "手机号")
    @ApiModelProperty(value = "手机号")
    private String phone;

    /**
     * 用户来源
     */
    @Excel(name = "用户来源")
    @ApiModelProperty(value = "用户来源")
    private String userSource;

    /**
     * 订单金额
     */
    @Excel(name = "订单金额",defaultValue = "0.00", scale = 2)
    @ApiModelProperty(value = "订单金额")
    private BigDecimal orderAmount;

    /**
     * 操作时间
     */
    @Excel(name = "操作时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "操作时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date operateTime;

    public BigDecimal getOrderAmount() {
        if (orderAmount == null) {
            return BigDecimal.ZERO;
        }
        return orderAmount.setScale(2, RoundingMode.HALF_UP);
    }
}
