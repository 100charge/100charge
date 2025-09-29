package com.xingchuan.charging.domain.resp;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 场站详情对象
 *
 * @author ruoyi
 */
@Data
public class MiniStationsDetailResponse {

    @ApiModelProperty("id")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    /**
     * 场站名称
     */
    @ApiModelProperty("场站名称")
    private String name;

    /**
     * 场站详细地址
     */
    @ApiModelProperty("场站详细地址")
    private String address;

    /**
     * 纬度
     */
    @ApiModelProperty("纬度")
    private BigDecimal lat;

    /**
     * 经度
     */
    @ApiModelProperty("经度")
    private BigDecimal lng;

    /**
     * 距离（米）
     */
    @ApiModelProperty(value = "距离（米）")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long distance;

    /**
     * 营业开始时间
     */
    @ApiModelProperty("营业开始时间")
    @JsonFormat(pattern = "HH:mm:ss")
    private Date perationBeginTime;

    /**
     * 营业截止时间
     */
    @ApiModelProperty("营业截止时间")
    @JsonFormat(pattern = "HH:mm:ss")
    private Date perationEndTime;

    /**
     * 计费规则id
     */
    @ApiModelProperty("计费规则id")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long ruleId;

    /**
     * 最大功率KW（>=180超充；30-179快充；<30慢充）
     */
    @ApiModelProperty("最大功率KW（>=180超充；30-179快充；<30慢充）")
    @JsonSerialize(using = ToStringSerializer.class)
    private Integer maxPower;

    /**
     * 星级
     */
    @ApiModelProperty("星级")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long starLabel;

    /**
     * 场站服务（卫生间、休息室、便利店）
     */
    @JsonIgnore
    private String serviceLabel;

    /**
     * 设施标签（地上、地下、洗车）
     */
    @JsonIgnore
    private String facilityLabel;

    /**
     * 场站标签（地上、地下、洗车。。。。。。）
     */
    @ApiModelProperty("场站标签（地上、地下、洗车。。。。。。）")
    private String labelName;

    /**
     * 停车费标签
     */
    @ApiModelProperty("停车费标签")
    private String parkingFeeLabel;

    /**
     * 停车费描述
     */
    @ApiModelProperty("停车费标签")
    private String parkingFeeTip;

    /**
     * 企业名称（服务提供）
     */
    @ApiModelProperty("企业名称（服务提供）")
    private String companyName;

    /**
     * 快充桩总数量
     */
    @ApiModelProperty("快充桩总数量")
    private Integer fastCharging;

    /**
     * 快充桩闲置数量
     */
    @ApiModelProperty("快充桩闲置数量")
    private Integer fastChargingIdle;

    /**
     * 慢充桩总数量
     */
    @ApiModelProperty("慢充桩总数量")
    private Integer slowCharging;

    /**
     * 慢充桩闲置数量
     */
    @ApiModelProperty("慢充桩闲置数量")
    private Integer slowChargingIdle;

    /**
     * 当前时间段
     */
    @ApiModelProperty("当前时间段")
    private String currentTime;

    /**
     * 当前时段充电费用
     */
    @ApiModelProperty("当前时段充电费用")
    @JsonSerialize(using = ToStringSerializer.class)
    private BigDecimal chargeFee;

    /**
     * logo
     */
    @ApiModelProperty("logo")
    private String logo;

    /**
     * 营业执照地址
     */
    @ApiModelProperty("营业执照地址")
    private String licenseImage;

    /**
     * 场站图片集合
     */
    @ApiModelProperty("场站图片集合")
    private List<String> stationImageList;

    /**
     * 互联互通-运营商Id
     */
    @JsonIgnore
    @ApiModelProperty(name = "互联互通-运营商Id")
    private String operatorId;

    public String getLabelName() {

        if (StringUtils.isNotBlank(serviceLabel) && StringUtils.isNotEmpty(facilityLabel)) {
            labelName = serviceLabel + "," + facilityLabel;
        } else if (StringUtils.isNotBlank(serviceLabel)) {
            labelName = serviceLabel;
        } else if (StringUtils.isNotBlank(facilityLabel)) {
            labelName = facilityLabel;
        }

        return labelName;
    }
}
