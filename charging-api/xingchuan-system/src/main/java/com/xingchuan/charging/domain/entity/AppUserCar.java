package com.xingchuan.charging.domain.entity;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.xingchuan.common.annotation.Excel;
import com.xingchuan.common.core.domain.BaseEntity;
import lombok.Data;

/**
 * 用户认证车辆信息对象 app_user_car
 */
@Data
public class AppUserCar extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    /**
     * 用户openId
     */
    @Excel(name = "用户openId")
    private String openId;

    /**
     * 车牌号（保证唯一）
     */
    @Excel(name = "车牌号", readConverterExp = "保=证唯一")
    private String plateNo;

    /**
     * 车辆用途（0:私家车、1:网约车、2:出租车、3:物流车）
     */
    @Excel(name = "车辆用途", readConverterExp = "0=:私家车、1:网约车、2:出租车、3:物流车")
    private Integer usage;

    /**
     * VIN车架号（保证唯一）
     */
    @Excel(name = "VIN车架号", readConverterExp = "保=证唯一")
    private String vin;

    /**
     * 品牌型号
     */
    @Excel(name = "品牌型号")
    private String model;

    /**
     * 行驶证照片车辆信息页图片
     */
    @Excel(name = "行驶证照片车辆信息页图片")
    private String image;

    /**
     * 是否默认车辆（0：否；1：是）
     */
    @Excel(name = "是否默认车辆", readConverterExp = "0=：否；1：是")
    private Integer defaultVehicle;

    /**
     * 是否已认证（0:未认证，1：审核中，2：已认证，3：认证失败）
     */
    @Excel(name = "是否已认证(0:未认证，1：审核中，2：已认证，3：认证失败)")
    private Integer verified;

    /**
     * 组织架构ID
     */
    @Excel(name = "组织架构ID")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long deptId;

    /**
     * 删除标志（0代表存在 2代表删除）
     */
    private String delFlag;

}
