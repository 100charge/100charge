package com.xingchuan.charging.domain.entity;

import com.xingchuan.common.annotation.Excel;
import com.xingchuan.common.core.domain.BaseEntity;
import lombok.Data;

/**
 * banner对象 banner
 *
 * @author ruoyi
 */
@Data
public class Banner extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * $column.columnComment
     */
    private Long id;

    /**
     * banner名称
     */
    @Excel(name = "banner名称")
    private String name;

    /**
     * 链接地址
     */
    @Excel(name = "链接地址")
    private String link;

    /**
     * 图片地址
     */
    @Excel(name = "图片地址")
    private String imageUrl;

    /**
     * 是否开启（0:不开启；1:开启）
     */
    @Excel(name = "是否开启", readConverterExp = "0=:不开启；1:开启")
    private Integer open;

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
