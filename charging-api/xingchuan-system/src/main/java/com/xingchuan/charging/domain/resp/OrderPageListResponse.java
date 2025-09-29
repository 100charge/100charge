package com.xingchuan.charging.domain.resp;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.xingchuan.common.config.BigDecimalSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.commons.lang3.ObjectUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;

@Data
@ApiModel(value = "充电订单分页查询返回实体")
public class OrderPageListResponse {

    /**
     * id
     */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    /**
     * 订单号（唯一）
     */
    @ApiModelProperty(value = "订单号")
    private String tradeNo;

    /**
     * 状态(0:进行中,1:待付款,2:已完成)
     */
    @ApiModelProperty(value = "状态(0:进行中,1:待付款,2:已完成)")
    private Integer status;

    /**
     * 状态描述
     */
    @ApiModelProperty(value = "状态描述(0:进行中,1:待付款,2:已完成)")
    private String statusDesc;

    /**
     * 场站id
     */
    @ApiModelProperty(value = "场站id")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long stationId;

    /**
     * 场站名称
     */
    @ApiModelProperty(value = "场站名称")
    private String stationName;

    /**
     * 开始时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "开始时间")
    private Date startTime;

    /**
     * 实际充电时长（分钟）（结束-开始）
     */
    @ApiModelProperty(value = "实际充电时长")
    private String realDuration = "0";

    /**
     * 耗电量 单位 kw
     */
    @ApiModelProperty(value = "耗电量 单位 kw")
    @JsonSerialize(using = ToStringSerializer.class)
    private BigDecimal totalPower;

    /**
     * 实际应付金额
     */
    @ApiModelProperty(value = "实际应付金额")
    @JsonSerialize(using = BigDecimalSerializer.class)
    private BigDecimal realAmount;

    /**
     * 停止原因
     */
    @ApiModelProperty(value = "停止原因")
    private String stopReason;

    /**
     * 是否评价
     */
    @ApiModelProperty(value = "是否评价")
    private boolean isReviewed;


    public void setTotalPower(BigDecimal totalPower) {
        if (ObjectUtils.isEmpty(totalPower)) {
            totalPower = BigDecimal.ZERO;
        }
        this.totalPower = totalPower.setScale(3, RoundingMode.HALF_UP);
    }
}
