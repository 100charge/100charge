package com.xingchuan.charging.domain.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.xingchuan.common.annotation.Excel;
import com.xingchuan.common.core.domain.BaseEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 场站对象 charging_stations
 *
 * @author ruoyi
 */
@Data
public class ChargingStations extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * $column.columnComment
     */
    private Long id;

    /**
     * 场站名称
     */
    @Excel(name = "场站名称")
    private String name;

    /**
     * 省
     */
    @Excel(name = "省")
    private String province;

    /**
     * 市
     */
    @Excel(name = "市")
    private String city;

    /**
     * 区
     */
    @Excel(name = "区")
    private String region;

    /**
     * 场站详细地址
     */
    @Excel(name = "场站详细地址")
    private String address;

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
     * 运营商id
     */
    @Excel(name = "运营商id")
    @TableField(fill = FieldFill.INSERT)
    private Long tenantId;

    /**
     * 计费规则id
     */
    @Excel(name = "计费规则id")
    private Long ruleId;

    /**
     * 营业开始时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "营业开始时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date perationBeginTime;

    /**
     * 营业截止时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "营业截止时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date perationEndTime;

    /**
     * 是否即插即冲（0：否；1：是）
     */
    @Excel(name = "是否即插即冲", readConverterExp = "0=：否；1：是")
    private Integer plugCharge;

    /**
     * 最大功率KW（>=180超充；30-179快充；<30慢充）
     */
    @Excel(name = "最大功率KW", readConverterExp = ">==180超充；30-179快充；<30慢充")
    private Integer maxPower;

    /**
     * 星级
     */
    @Excel(name = "星级")
    private Long starLabel;

    /**
     * 场站服务（卫生间、休息室、便利店）
     */
    @Excel(name = "场站服务", readConverterExp = "卫=生间、休息室、便利店")
    private String serviceLabel;

    /**
     * 设施标签（地上、地下、洗车）
     */
    @Excel(name = "设施标签", readConverterExp = "地=上、地下、洗车")
    private String facilityLabel;

    /**
     * 停车费标签
     */
    @Excel(name = "停车费标签")
    private String parkingFeeLabel;

    /**
     * 停车费描述
     */
    @Excel(name = "停车费描述")
    private String parkingFeeTip;

    /**
     * 是否优选（0：否,1：优选）
     */
    @Excel(name = "是否优选", readConverterExp = "0=：否,1：优选")
    private Integer recommend;

    /**
     * 是否显示（0否 1显示）
     */
    @Excel(name = "是否显示", readConverterExp = "0=否,1=显示")
    private Integer showStatus;

    /**
     * 场站状态（0:关闭；1:开放；2:维护中）
     */
    @Excel(name = "场站状态", readConverterExp = "0=:关闭；1:开放；2:维护中")
    private Integer status;

    /**
     * 企业名称（服务提供）
     */
    @Excel(name = "企业名称", readConverterExp = "服=务提供")
    private String companyName;

    /**
     * 组织架构ID
     */
    @Excel(name = "组织架构ID")
    @TableField(fill = FieldFill.INSERT)
    private Long deptId;

    /**
     * '0'删除标志（0代表存在 2代表删除）
     */
    private String delFlag;

    /**
     * 通联-终端号
     */
    private String allinPayTerminalNo;

    /**
     * 互联互通运营商id
     */
    private String operatorId;

    /**
     * 互联互通-运营商-场站id
     */
    private String operatorStationId;

    /**
     * 设备所属运营平台组织机构代码
     */
    private String equipmentOwnerId;

    /**
     * 建设场所
     */
    private Integer construction;

    /**
     * 道闸品牌编号
     */
    private Long gateBrandId;

    /**
     * 道闸场站编号
     */
    private String barrierGateStationNo;

    /**
     * 停车减免时间（分钟）
     */
    private Integer parkingDiscountTime;

    /**
     * 停止充电金额
     */
    @ApiModelProperty("停止充电金额")
    private BigDecimal chargingLimitAmount;

}
