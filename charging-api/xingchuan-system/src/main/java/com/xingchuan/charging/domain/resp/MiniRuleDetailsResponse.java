package com.xingchuan.charging.domain.resp;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;


/**
 * 价格对象 rule_details
 *
 * @author ruoyi
 */
@Data
@ApiModel(value = "计费规则详情查询返回时间段实体类")
public class MiniRuleDetailsResponse {

    /**
     * 充电时段
     */
    @ApiModelProperty(value = "充电时段")
    private String timePeriod;

    /**
     * 费用类型 尖 峰 平 谷 深谷
     */
    @ApiModelProperty(value = "费用类型 尖 峰 平 谷 深谷")
    private String type;

    /**
     * 电费
     */
    @ApiModelProperty(value = "电费")
    @JsonSerialize(using = ToStringSerializer.class)
    private BigDecimal electricityFee;

    /**
     * 服务费
     */
    @ApiModelProperty(value = "服务费")
    @JsonSerialize(using = ToStringSerializer.class)
    private BigDecimal serviceFee;

    /**
     * 价格
     */
    @ApiModelProperty(value = "价格")
    @JsonSerialize(using = ToStringSerializer.class)
    private BigDecimal totalAmount;

    /**
     * 是否当前时间段
     */
    @ApiModelProperty(value = "是否当前时间段")
    private Boolean currentTimePeriod = false;

    /**
     * 是否最低价
     */
    @ApiModelProperty(value = "是否最低价")
    private Boolean lowestPrice = false;

}
