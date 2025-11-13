package com.xingchuan.charging.domain.entity;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.xingchuan.common.annotation.Excel;
import com.xingchuan.common.core.domain.BaseEntity;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 价格对象 rule_details
 *
 * @author ruoyi
 */
@Data
public class RuleDetails extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * $column.columnComment
     */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    /**
     * 计费规则id
     */
    @Excel(name = "计费规则id")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long ruleTimeId;

    /**
     * 开始时间
     */
    private Date startTime;

    /*
     * 结束时间
     */
    private Date endTime;

    /**
     * 电费
     */
    private BigDecimal chargeFee;

    /**
     * 服务费
     */
    private BigDecimal serviceFee;

    /**
     * 停车费
     */
    private BigDecimal parkingFee;

    /**
     * 超时占用费
     */
    private BigDecimal occupancyFee;

    /**
     * 电费类型 0:尖 1:峰 2:平 3:谷 4:深谷
     */
    @Excel(name = "电费类型 0:尖 1:峰 2:平 3:谷 4:深谷")
    private Integer type;

    /**
     * 组织架构id
     */
    @Excel(name = "组织架构id")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long deptId;

    /**
     * 删除标记，默认0：未删除，2 删除
     */
    private String delFlag;

    /**
     * 开始时间值
     */
    private Integer startValue;

    /*
     * 结束时间值
     */
    private Integer endValue;

    /**
     * 原服务费(互联平台)
     */
    private BigDecimal originalServiceFee;
}
