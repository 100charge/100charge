package com.xingchuan.charging.domain.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.xingchuan.common.annotation.Excel;
import com.xingchuan.common.core.domain.BaseEntity;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 充电订单对象 charging_order
 *
 * @author ruoyi
 */
@Data
public class ChargingOrder extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    private Long id;

    /**
     * 订单号（唯一）
     */
    @Excel(name = "订单号", readConverterExp = "唯=一")
    private String tradeNo;

    /**
     * 第三方订单编号
     */
    @Excel(name = "第三方订单编号")
    private String outTradeNo;

    /**
     * app用户openid
     */
    @Excel(name = "app用户openid")
    private String openId;

    /**
     * 手机号
     */
    @Excel(name = "手机号")
    private String mobile;

    /**
     * vin号
     */
    @Excel(name = "vin号")
    private String vin;

    /**
     * 卡号(卡支付时插入)
     */
    @Excel(name = "卡号(卡支付时插入)")
    private String cardNo;

    /**
     * 企业名称
     */
    @Excel(name = "企业名称")
    private String companyName;

    /**
     * 车牌号
     */
    @Excel(name = "车牌号")
    private String plateNo;

    /**
     * 订单状态：0:未开始；1:充电中；2:充电结束（未拔枪）；3:充电结束；4:异常
     */
    @Excel(name = "订单状态：0:未开始；1:充电中；2:充电结束（未拔枪）；3:充电结束；4:异常")
    private Integer orderState;

    /**
     * 订单支付状态：0:未支付；1:支付成功；2:支付失败
     */
    @Excel(name = "订单支付状态：0:未支付；1:支付成功；2:支付失败")
    private Integer payStatus;

    /**
     * 场站id
     */
    @Excel(name = "场站id")
    private Long stationId;

    /**
     * 场站名称
     */
    @Excel(name = "场站名称")
    private String stationName;

    /**
     * 充电桩编号
     */
    @Excel(name = "充电桩编号")
    private String deviceNo;

    /**
     * 充电枪号
     */
    @Excel(name = "充电枪号")
    private String gunNo;

    /**
     * 开始时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "开始时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date startTime;

    /**
     * 结束时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "结束时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date endTime;

    /**
     * 实际充电时长（分钟）（结束-开始）
     */
    @Excel(name = "实际充电时长", readConverterExp = "分=钟")
    private Long realDuration;

    /**
     * 支付金额（充电费+服务费+超时占用费+停车费）
     */
    @Excel(name = "支付金额", readConverterExp = "充=电费+服务费+超时占用费+停车费")
    private BigDecimal totalAmount;

    /**
     * 耗电量 单位 kw
     */
    @Excel(name = "耗电量 单位 kw")
    private BigDecimal totalPower;

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
    @Excel(name = "停车费")
    private BigDecimal parkingFee;

    /**
     * 超时占用费
     */
    @Excel(name = "超时占用费")
    private BigDecimal overTimeFee;

    /**
     * 优惠金额
     */
    @Excel(name = "优惠金额")
    private BigDecimal couponAmount;

    /**
     * 优惠券类型（0: "满减券-服务费",1: "满减券-电费+服务费",2: "直减券-服务费",3: "直减券-电费+服务费",4: "折扣券-服务费%";）
     */
    @Excel(name = "优惠券类型（0: \"满减券-服务费\",1: \"满减券-电费+服务费\",2: \"直减券-服务费\",3: \"直减券-电费+服务费\",4: \"折扣券-服务费%\";）")
    private Integer discountType;

    /**
     * 实际应付金额
     */
    @Excel(name = "实际应付金额")
    private BigDecimal realAmount;

    /**
     * 场站金额
     */
    @Excel(name = "场站金额")
    private BigDecimal stationAmount;

    /**
     * 退款金额
     */
    @Excel(name = "退款金额")
    private BigDecimal refundAmount;

    /**
     * 支付时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "支付时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date payTime;

    /**
     * 支付方式（0余额、1卡支付、2微信支付分、3支付宝信用分;4：预付费）
     */
    @Excel(name = "支付方式", readConverterExp = "0=余额、1卡支付、2微信支付分、3支付宝信用分;4：预付费")
    private Integer payment;

    /**
     * 订单来源，1：微信小程序,2：支付宝小程序,3：钥匙卡,4：vin,5：互联互通,6：三方互联
     */
    @Excel(name = "订单来源，1：微信小程序,2：支付宝小程序,3：钥匙卡,4：vin,5：互联互通,6：三方互联")
    private Integer orderSource;

    /**
     * 停止原因
     */
    @Excel(name = "停止原因")
    private String stopReason;

    /**
     * 是否离线订单（0离线订单；1在线订单）
     */
    @Excel(name = "是否离线订单", readConverterExp = "0=离线订单；1在线订单")
    private Boolean offline;

    /**
     * 组织架构ID
     */
    @Excel(name = "组织架构ID")
    private Long deptId;

    /**
     * 删除标志（0代表存在 2代表删除）
     */
    private String delFlag;

    /**
     * 车队ID
     */
    @Excel(name = "车队ID")
    private Long fleetId;

    /**
     * 预付费金额
     */
    @Excel(name = "预付费金额")
    private BigDecimal prepaidAmount;

    /**
     * 预付费充值订单
     */
    @Excel(name = "预付费充值订单")
    private String rechargeOrderNo;

    /**
     * 互联互通运营商id
     */
    private String operatorId;

    /**
     * 互联互通商户费用系数
     */
    private BigDecimal ratio;

    /**
     * 互联互通订单金额
     */
    private BigDecimal orgAmount;

    /**
     * 发票id
     */
    private Long invoiceId;

    /**
     * 发票号
     */
    private String invoiceNo;

    /**
     * 设备来源(互联互通商户id)
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
}
