package com.xingchuan.charging.domain.entity;

import com.xingchuan.common.annotation.Excel;
import com.xingchuan.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 场站评价对象 charging_station_reviews
 *
 * @author ruoyi
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ChargingStationReviews extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * $column.columnComment
     */
    private Long id;

    /**
     * 场站id
     */
    @Excel(name = "场站id")
    private Long chargingStationId;

    /**
     * 充电订单id
     */
    @Excel(name = "充电订单id")
    private Long orderId;

    /**
     * 评价用户id
     */
    @Excel(name = "评价用户id")
    private Long appUserId;

    /**
     * 评价标签
     */
    @Excel(name = "评价标签")
    private String rating;

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
