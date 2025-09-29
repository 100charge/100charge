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
@ApiModel(value = "充电订单分页详情返回实体")
public class OrderDetailResponse {

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
     * 订单支付状态：0:未支付；1:支付成功；2:支付失败；3:已退款;
     */
    @ApiModelProperty(value = "订单支付状态：0:未支付；1:支付成功；2:支付失败", hidden = true)
    private Integer payStatus;

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
     * 充电桩编号
     */
    @ApiModelProperty(value = "充电桩编号")
    private String deviceNo;

    /**
     * 充电枪号
     */
    @ApiModelProperty(value = "充电枪号")
    private String gunNo;
    /**
     * 开始时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "开始时间")
    private Date startTime;

    /**
     * 结束时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "结束时间")
    private Date endTime;

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
     * 支付金额（充电费+服务费+超时占用费+停车费）
     */
    @ApiModelProperty(value = "支付金额:电费+服务费+超时占用费+停车费")
    @JsonSerialize(using = BigDecimalSerializer.class)
    private BigDecimal totalAmount;

    /**
     * 电费
     */
    @ApiModelProperty(value = "电费")
    @JsonSerialize(using = BigDecimalSerializer.class)
    private BigDecimal chargeFee;

    /**
     * 服务费
     */
    @ApiModelProperty(value = "服务费")
    @JsonSerialize(using = BigDecimalSerializer.class)
    private BigDecimal serviceFee;

    /**
     * 停车费
     */
    @ApiModelProperty(value = "停车费")
    @JsonSerialize(using = BigDecimalSerializer.class)
    private BigDecimal parkingFee;

    /**
     * 超时占用费
     */
    @ApiModelProperty(value = "超时占用费")
    @JsonSerialize(using = BigDecimalSerializer.class)
    private BigDecimal overTimeFee;

    /**
     * 优惠金额
     */
    @ApiModelProperty(value = "优惠金额")
    @JsonSerialize(using = BigDecimalSerializer.class)
    private BigDecimal couponAmount;

    /**
     * 实际应付金额
     */
    @ApiModelProperty(value = "实际应付金额")
    @JsonSerialize(using = BigDecimalSerializer.class)
    private BigDecimal realAmount;

    /**
     * 支付时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "支付时间")
    private Date payTime;

    /**
     * 支付方式（0:余额、1:卡支付、2:微信支付分、3:支付宝信用分;4：预付费）
     */
    @ApiModelProperty(value = "支付方式:0余额、1卡支付、2微信支付分、3支付宝信用分;4：预付费")
    private Integer payment;

    /**
     * 支付方式（0:余额、1:卡支付、2:微信支付分、3:支付宝信用分;4：预付费）
     */
    @ApiModelProperty(value = "支付方式:0余额、1卡支付、2微信支付分、3支付宝信用分;4：预付费")
    private String paymentDesc;

    /**
     * 订单来源，1：微信小程序,2：支付宝小程序,3：钥匙卡,4：vin
     */
    @ApiModelProperty(value = "订单来源，1：微信小程序,2：支付宝小程序,3：钥匙卡,4：vin")
    private Integer orderSource;

    /**
     * 订单来源，1：微信小程序,2：支付宝小程序,3：钥匙卡,4：vin
     */
    @ApiModelProperty(value = "订单来源，1：微信小程序,2：支付宝小程序,3：钥匙卡,4：vin")
    private String orderSourceDesc;

    /**
     * 停止原因
     */
    @ApiModelProperty(value = "停止原因")
    private String stopReason;


    public void setTotalPower(BigDecimal totalPower) {
        if (ObjectUtils.isEmpty(totalPower)) {
            totalPower = BigDecimal.ZERO;
        }
        this.totalPower = totalPower.setScale(3, RoundingMode.HALF_UP);
    }

}
