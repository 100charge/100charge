package com.xingchuan.charging.domain.entity;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.xingchuan.common.annotation.Excel;
import com.xingchuan.common.core.domain.BaseEntity;
import lombok.Data;

import java.util.Date;

/**
 * 计费策略时段对象 rules
 *
 * @author ruoyi
 */
@Data
public class RuleTime extends BaseEntity {
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
    private Long ruleId;

    /**
     * 开始时间
     */
    @Excel(name = "开始时间")
    private Date beginTime;

    /**
     * 截止时间
     */
    @Excel(name = "截止时间")
    private Date endTime;

    /**
     * 组织架构id
     */
    @Excel(name = "组织架构id")
    private Long deptId;

    /**
     * '0'删除标志（0代表存在 2代表删除）
     */
    private String delFlag;


    public RuleTime() {
    }

    public RuleTime(Long ruleId, Date beginTime, Date endTime, Long deptId, String remark) {
        this.ruleId = ruleId;
        this.beginTime = beginTime;
        this.endTime = endTime;
        this.deptId = deptId;
        setRemark(remark);
    }
}
