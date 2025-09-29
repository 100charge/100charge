package com.xingchuan.charging.domain.entity;

import com.xingchuan.common.annotation.Excel;
import com.xingchuan.common.core.domain.BaseEntity;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 用户提现审核对象 user_withdrawal_request
 */
@Data
public class UserWithdrawalRequest extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * $column.columnComment
     */
    private Long id;

    /**
     * 用户openId
     */
    @Excel(name = "用户openId")
    private String openId;

    /**
     * 订单编号
     */
    @Excel(name = "订单编号")
    private String tradeNo;

    /**
     * 操作类型（0：小程序用户;1：运营商）
     */
    @Excel(name = "操作类型（0：小程序用户;1：运营商）")
    private Integer type;

    /**
     * 金额
     */
    @Excel(name = "金额")
    private BigDecimal amount;

    /**
     * 处理中金额
     */
    @Excel(name = "处理中金额")
    private BigDecimal processingAmount;

    /**
     * 已退款金额
     */
    @Excel(name = "已退款金额")
    private BigDecimal refundAmount;

    /**
     * 审核状态（0：待审核,1：审核通过,2：审核驳回）
     */
    @Excel(name = "审核状态（0：待审核,1：审核通过,2：审核驳回）")
    private Integer status;

    /**
     * 退款状态（0：待退款,1：退款中,2：退款完成）
     */
    @Excel(name = "退款状态（0：待退款,1：退款中,2：退款完成）")
    private Integer refundStatus;

    /**
     * 审核人
     */
    @Excel(name = "审核人")
    private String approveBy;

    /**
     * 审核时间
     */
    @Excel(name = "审核时间")
    private Date approveTime;

    /**
     * 组织架构ID
     */
    @Excel(name = "组织架构ID")
    private Long deptId;

    /**
     * '0'删除标志（0代表存在 2代表删除）
     */
    private String delFlag;

}
