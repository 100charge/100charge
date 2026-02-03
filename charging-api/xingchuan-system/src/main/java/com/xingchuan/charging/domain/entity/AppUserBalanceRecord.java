package com.xingchuan.charging.domain.entity;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.xingchuan.charging.enums.Payment;
import com.xingchuan.common.annotation.Excel;
import com.xingchuan.common.core.domain.BaseEntity;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 用户余额变动记录对象 user_balance_record
 *
 * @author ruoyi
 */
@Data
public class AppUserBalanceRecord extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * $column.columnComment
     */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    /**
     * 用户openId
     */
    @Excel(name = "用户openId")
    private String openId;

    /**
     * 操作类型（0：充值;1：消费;2：提现,3：订单退款,4:企业余额分配）
     */
    @Excel(name = "操作类型")
    private Integer type;

    /**
     * 场站名称
     */
    @Excel(name = "场站名称")
    private String stationName;

    /**
     * 订单编号
     */
    @Excel(name = "订单编号")
    private String tradeNo;

    /**
     * 三方订单编号
     */
    @Excel(name = "三方订单编号")
    private String outTradeNo;

    /**
     * 通联用户id
     */
    @Excel(name = "通联用户id")
    private String allinPayUserId;

    /**
     * 金额
     */
    @Excel(name = "金额")
    private BigDecimal amount;

    /**
     * 手续费
     */
    @Excel(name = "手续费")
    private BigDecimal fee;

    /**
     * 本次剩余余额
     */
    @Excel(name = "本次剩余余额")
    private BigDecimal lastAmount;

    /**
     * 可退款金额
     */
    @Excel(name = "可退款金额")
    private BigDecimal refundableAmount;

    /**
     * 已分账金额
     */
    @Excel(name = "已分账金额")
    private BigDecimal allocatedAmount;

    /**
     * 剩余可分账金额
     */
    @Excel(name = "剩余可分账金额")
    private BigDecimal remainingAmount;

    /**
     * 订单状态（-1:失效，0:处理中，1:已完成,2:失败）
     */
    @Excel(name = "订单状态（-1:失效，0:处理中，1:已完成,2:失败）")
    private Integer status;

    /**
     * 组织架构ID
     */
    @Excel(name = "组织架构ID")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long deptId;

    /**
     * '0'删除标志（0代表存在 2代表删除）
     */
    private String delFlag;

    /**
     * 支付渠道
     * WECHAT_PAY
     * ALI_PAY
     * ALLIN_PAY
     * UNION_PAY
     * YEE_PAY
     */
    private Payment payment;
    /**
     * 退款请求号，针对部分退款时需要
     */
    private String outRequestNo;
    /**
     * 支付订单号，在退款时需要维护，便于操作恢复金额
     */
    private String payTradeNo;
}
