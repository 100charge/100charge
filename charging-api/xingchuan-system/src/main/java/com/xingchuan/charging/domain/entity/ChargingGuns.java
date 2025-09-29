package com.xingchuan.charging.domain.entity;

import com.xingchuan.common.annotation.Excel;
import com.xingchuan.common.core.domain.BaseEntity;
import lombok.Data;

/**
 * 充电桩-充电枪对象 charging_guns
 */
@Data
public class ChargingGuns extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * $column.columnComment
     */
    private Long id;

    /**
     * 充电桩编号
     */
    @Excel(name = "充电桩编号")
    private String deviceNo;

    /**
     * 充电枪编号
     */
    @Excel(name = "充电枪编号")
    private String no;

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
