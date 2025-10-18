package com.xingchuan.common.core.domain.entity;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.xingchuan.common.annotation.Excel;
import com.xingchuan.common.core.domain.BaseEntity;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 省市区对象 area
 *
 * @author ruoyi
 */
@Data
public class Area extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * $column.columnComment
     */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    /**
     * 编码GB/T 2260-207
     */
    @Excel(name = "编码GB/T 2260-207")
    private String code;

    /**
     * 简称
     */
    @Excel(name = "简称")
    private String name;

    /**
     * 全称
     */
    @Excel(name = "全称")
    private String fullName;

    /**
     * 祖籍路径
     */
    @Excel(name = "祖籍路径")
    private String treePath;

    /**
     * 上级id
     */
    @Excel(name = "上级id")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long parentId;

    /**
     * 是否展示（0隐藏  1展示）
     */
    @Excel(name = "是否展示", readConverterExp = "0=隐藏,1=展示")
    private Integer open;

    /**
     * 纬度
     */
    @Excel(name = "纬度")
    private BigDecimal lat;

    /**
     * 经度
     */
    @Excel(name = "经度")
    private BigDecimal lng;

    /**
     * 部门id
     */
    @Excel(name = "部门id")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long deptId;

    /**
     * 删除状态（0正常  2删除）
     */
    private String delFlag;
}
