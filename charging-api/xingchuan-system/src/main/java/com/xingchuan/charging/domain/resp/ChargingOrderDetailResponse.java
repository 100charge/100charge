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
public class ChargingOrderDetailResponse {

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
     * 第三方订单编号
     */
    @ApiModelProperty(value = "第三方订单编号")
    private String outTradeNo;

    /**
     * app用户openid
     */
    @ApiModelProperty(value = "app用户openid")
    private String openId;

    /**
     * 手机号
     */
    @ApiModelProperty(value = "手机号")
    private String mobile;

    /**
     * vin号
     */
    @ApiModelProperty(value = "vin号")
    private String vin;

    /**
     * 卡号(卡支付时插入)
     */
    @ApiModelProperty(value = "卡号(卡支付时插入)")
    private String cardNo;

    /**
     * 企业名称
     */
    @ApiModelProperty(value = "企业名称")
    private String companyName;

    /**
     * 车牌号
     */
    @ApiModelProperty(value = "车牌号")
    private String plateNo;

    /**
     * 订单状态：0:未开始；1:充电中；2:充电结束（未拔枪）；3:充电结束；4:异常
     */
    @ApiModelProperty(value = "订单状态：0:未开始；1:充电中；2:充电结束（未拔枪）；3:充电结束；4:异常")
    private String orderState;

    /**
     * 订单状态描述
     */
    @ApiModelProperty(value = "订单状态描述")
    private String orderStateDesc;

    /**
     * 订单支付状态：0:未支付；1:支付成功；2:支付失败；3:已退款;
     */
    @ApiModelProperty(value = "订单支付状态：0:未支付；1:支付成功；2:支付失败；3:已退款;")
    private Integer payStatus;

    /**
     * 订单支付状态描述
     */
    @ApiModelProperty(value = "订单支付状态描述")
    private String payStatusDesc;

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
    private String realDuration;

    /**
     * 支付金额（充电费+服务费+超时占用费+停车费）
     */
    @JsonSerialize(using = BigDecimalSerializer.class)
    @ApiModelProperty(value = "支付金额")
    private BigDecimal totalAmount;

    /**
     * 耗电量 单位 kw
     */
    @JsonSerialize(using = ToStringSerializer.class)
    @ApiModelProperty(value = "耗电量 单位 kw")
    private BigDecimal totalPower;

    /**
     * 电费
     */
    @JsonSerialize(using = BigDecimalSerializer.class)
    @ApiModelProperty(value = "电费")
    private BigDecimal chargeFee;

    /**
     * 服务费
     */
    @JsonSerialize(using = BigDecimalSerializer.class)
    @ApiModelProperty(value = "服务费")
    private BigDecimal serviceFee;

    /**
     * 停车费
     */
    @JsonSerialize(using = BigDecimalSerializer.class)
    @ApiModelProperty(value = "停车费")
    private BigDecimal parkingFee;

    /**
     * 超时占用费
     */
    @JsonSerialize(using = BigDecimalSerializer.class)
    @ApiModelProperty(value = "超时占用费")
    private BigDecimal overTimeFee;

    /**
     * 优惠金额
     */
    @JsonSerialize(using = BigDecimalSerializer.class)
    @ApiModelProperty(value = "优惠金额")
    private BigDecimal couponAmount;

    /**
     * 实际应付金额
     */
    @JsonSerialize(using = BigDecimalSerializer.class)
    @ApiModelProperty(value = "实际应付金额")
    private BigDecimal realAmount;

    /**
     * 场站金额
     */
    @JsonSerialize(using = BigDecimalSerializer.class)
    @ApiModelProperty(value = "场站金额")
    private BigDecimal stationAmount;

    /**
     * 退款金额
     */
    @JsonSerialize(using = BigDecimalSerializer.class)
    @ApiModelProperty(value = "退款金额")
    private BigDecimal refundAmount;

    /**
     * 支付时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "支付时间")
    private Date payTime;

    /**
     * 支付方式（0:余额;1:卡支付;2:微信支付分;3:支付宝信用分;4：预付费）
     */
    @ApiModelProperty(value = "支付方式")
    private Integer payment;

    /**
     * 支付方式描述
     */
    @ApiModelProperty(value = "支付方式描述")
    private String paymentDesc;

    /**
     * 订单来源，1：微信小程序,2：支付宝小程序,3：钥匙卡,4：vin
     */
    @ApiModelProperty(value = "订单来源，1：微信小程序,2：支付宝小程序,3：钥匙卡,4：vin")
    private Integer orderSource;

    /**
     * 订单来源描述
     */
    @ApiModelProperty(value = "订单来源描述")
    private String orderSourceDesc;

    /**
     * 停止原因
     */
    @ApiModelProperty(value = "停止原因")
    private String stopReason;

    /**
     * 是否离线订单（0:离线订单；1:在线订单）
     */
    @ApiModelProperty(value = "是否离线订单")
    private Boolean offline;

    /**
     * 用户名
     */
    @ApiModelProperty(value = "用户名")
    private String userName;

    /**
     * 来源平台
     */
    private String platform;

    /**
     * 设备来源(互联互通商户)
     */
    private String equipmentSourceOperator;

    /**
     * 三方互联订单号
     */
    private String resaleOrderNo;

    /**
     * 转售系数
     */
    private BigDecimal resaleCoefficient;

    public void setTotalPower(BigDecimal totalPower) {
        if (ObjectUtils.isEmpty(totalPower)) {
            totalPower = BigDecimal.ZERO;
        }
        this.totalPower = totalPower.setScale(3, RoundingMode.HALF_UP);
    }

}
