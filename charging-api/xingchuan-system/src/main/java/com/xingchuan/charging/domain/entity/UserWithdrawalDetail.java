package com.xingchuan.charging.domain.entity;

import com.xingchuan.common.annotation.Excel;
import com.xingchuan.common.core.domain.BaseEntity;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 用户提现审核明细表对象 user_withdrawal_detail
 *
 * @author ruoyi
 */
@Data
public class UserWithdrawalDetail extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * $column.columnComment
     */
    private Long id;

    /**
     * 退款审核表id
     */
    @Excel(name = "退款审核表id")
    private Long parentId;

    /**
     * 退款订单编号
     */
    @Excel(name = "退款订单编号")
    private String tradeNo;

    /**
     * 充值订单编号
     */
    @Excel(name = "充值订单编号")
    private String rechargeOrderNo;

    /**
     * 三方退款订单编号
     */
    @Excel(name = "三方退款订单编号")
    private String outTradeNo;

    /**
     * 金额
     */
    @Excel(name = "金额")
    private BigDecimal amount;

    /**
     * 状态（0：处理中,1：退款成功,2：退款失败）
     */
    @Excel(name = "状态（0：处理中,1：退款成功,2：退款失败）")
    private Integer status;

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
