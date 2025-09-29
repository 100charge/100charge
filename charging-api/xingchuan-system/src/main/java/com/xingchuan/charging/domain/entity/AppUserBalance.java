package com.xingchuan.charging.domain.entity;

import com.xingchuan.common.annotation.Excel;
import com.xingchuan.common.core.domain.BaseEntity;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 客户余额对象 app_user_balance
 *
 * @author ruoyi
 */
@Data
public class AppUserBalance extends BaseEntity {
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
     * 余额
     */
    @Excel(name = "余额")
    private BigDecimal balance;

    /**
     * 预付费金额
     */
    @Excel(name = "预付费金额")
    private BigDecimal prepaidAmount;

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
