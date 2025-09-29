package com.xingchuan.common.core.domain.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.xingchuan.common.core.domain.entity.Area;
import lombok.Data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * 省市区对象 area
 *
 * @author ruoyi
 */
@Data
public class AreaModel {

    /**
     * id
     */
    private Long id;

    /**
     * 编号
     */
    private String code;

    /**
     * 简称
     */
    private String name;

    /**
     * 全称
     */
    private String fullName;

    /**
     * 祖籍路径
     */
    private String treePath;

    /**
     * 上级id
     */
    private Long parentId;

    /**
     * 纬度
     */
    private BigDecimal lat;

    /**
     * 经度
     */
    private BigDecimal lng;

    /**
     * 子部门
     */
    @TableField(exist = false)
    private List<AreaModel> children = new ArrayList<>();

    public AreaModel() {
    }

    public AreaModel(Area area) {
        this.id = area.getId();
        this.code = area.getCode();
        this.name = area.getName();
        this.fullName = area.getFullName();
        this.treePath = area.getTreePath();
        this.parentId = area.getParentId();
        this.lat = area.getLat();
        this.lng = area.getLng();
    }

}
