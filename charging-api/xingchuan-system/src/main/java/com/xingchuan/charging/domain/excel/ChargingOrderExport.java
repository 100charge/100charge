package com.xingchuan.charging.domain.excel;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.xingchuan.common.annotation.Excel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class ChargingOrderExport {

    /**
     * app用户openid
     */
    @Excel(name = "用户openid", width = 35)
    private String openId;

    /**
     * 手机号
     */
    @Excel(name = "手机号", width = 30)
    private String mobile;

    /**
     * 订单号（唯一）
     */
    @Excel(name = "订单编号", width = 30)
    private String tradeNo;

    /**
     * 三方订单号（唯一）
     */
    @Excel(name = "互联订单号")
    private String outTradeNo;

    /**
     * 场站名称
     */
    @Excel(name = "场站", width = 30)
    private String stationName;

    /**
     * 桩号
     */
    @Excel(name = "桩号", width = 30)
    private String deviceNo;

    /**
     * 枪号
     */
    @Excel(name = "枪号", width = 30)
    private String gunNo;

    /**
     * 订单状态 0:未开始；1:充电中；2:充电结束（未拔枪）；3:充电结束；4:账单已上传,5:异常
     */
    @Excel(name = "订单状态", readConverterExp = "0=未开始,1=充电中,2=充电结束（未拔枪）,3=充电结束,4=账单已上传,5=异常")
    private Integer orderState;

    /**
     * 耗电量 单位 kw
     */
    @Excel(name = "充电量/度")
    private BigDecimal totalPower;

    /**
     * 支付金额（充电费+服务费+超时占用费+停车费）
     */
    @Excel(name = "总费用")
    private BigDecimal totalAmount;

    /**
     * 电费
     */
    @Excel(name = "电费")
    private BigDecimal chargeFee;

    /**
     * 服务费
     */
    @Excel(name = "服务费")
    private BigDecimal serviceFee;

    /**
     * 停车费
     */
    @ApiModelProperty(value = "停车费")
    private BigDecimal parkingFee;

    /**
     * 超时占用费
     */
    @Excel(name = "超时占用费")
    private BigDecimal overTimeFee;

    /**
     * 实际应付金额
     */
    @Excel(name = "实际支付金额")
    private BigDecimal realAmount;

    /**
     * 优惠金额
     */
    @Excel(name = "优惠金额")
    private BigDecimal couponAmount;

    /**
     * 订单来源，1：微信小程序,2：支付宝小程序,3：钥匙卡,4：vin
     */
    @Excel(name = "订单来源", readConverterExp = "1=微信小程序,2=支付宝小程序,3=钥匙卡,4=vin")
    private Integer orderSource;

    /**
     * 开始时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "充电开始时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date startTime;

    /**
     * 结束时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "充电结束时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;

    /**
     * 订单支付状态：0:未支付；1:支付成功；2:支付失败
     */
    @Excel(name = "订单支付状态", readConverterExp = "0=未支付,1=支付成功,2=支付失败")
    private Integer payStatus;

    /**
     * 支付时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "支付时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date payTime;

    /**
     * vin号
     */
    @Excel(name = "vin号", width = 30)
    private String vin;

    /**
     * 车牌号
     */
    @Excel(name = "车牌号")
    private String plateNo;

    /**
     * 来源平台
     */
    @Excel(name = "来源平台")
    private String platform;


    /**
     * 设备来源(互联互通商户)
     */
    @Excel(name = "设备来源")
    private String equipmentSourceOperator;

    /**
     * 三方互联订单号
     */
    @Excel(name = "三方互联订单号")
    private String resaleOrderNo;

    /**
     * 转售系数
     */
    @Excel(name = "转售系数")
    private BigDecimal resaleCoefficient;

    /**
     * 转售系数
     */
    @Excel(name = "停止原因")
    private String stopReason;

}
