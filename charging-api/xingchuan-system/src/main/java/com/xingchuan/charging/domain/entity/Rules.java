package com.xingchuan.charging.domain.entity;

import com.xingchuan.common.annotation.Excel;
import com.xingchuan.common.core.domain.BaseEntity;
import lombok.Data;

/**
 * 计费规则对象 rules
 *
 * @author ruoyi
 */
@Data
public class Rules extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * $column.columnComment
     */
    private Long id;

    /**
     * 规则名称
     */
    @Excel(name = "规则名称")
    private String name;

    /**
     * 组织架构id
     */
    @Excel(name = "组织架构id")
    private Long deptId;

    /**
     * '0'删除标志（0代表存在 2代表删除）
     */
    private String delFlag;

}
