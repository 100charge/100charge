package com.xingchuan.charging.domain.resp;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 场站详情对象
 *
 * @author ruoyi
 */
@Data
public class ChargingStationsDetailResponse {

    @ApiModelProperty("用户ID")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    /**
     * 场站名称
     */
    @ApiModelProperty("场站名称")
    private String name;

    /**
     * 省
     */
    @ApiModelProperty("省")
    @JsonSerialize(using = ToStringSerializer.class)
    private String province;

    /**
     * 市
     */
    @ApiModelProperty("市")
    @JsonSerialize(using = ToStringSerializer.class)
    private String city;

    /**
     * 区
     */
    @ApiModelProperty("区")
    @JsonSerialize(using = ToStringSerializer.class)
    private String region;

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
     * 运营商id
     */
    @ApiModelProperty("运营商id")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long tenantId;

    /**
     * 计费规则id
     */
    @ApiModelProperty("计费规则id")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long ruleId;

    /**
     * 计费规则名称
     */
    @ApiModelProperty("计费规则名称")
    private String ruleName;

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
     * 是否即插即冲（0：否；1：是）
     */
    @ApiModelProperty("是否即插即冲（0：否；1：是）")
    private Integer plugCharge;

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
    @ApiModelProperty("场站服务（卫生间、休息室、便利店）")
    private String serviceLabel;

    /**
     * 设施标签（地上、地下、洗车）
     */
    @ApiModelProperty("设施标签（地上、地下、洗车）")
    private String facilityLabel;

    /**
     * 停车费标签
     */
    @ApiModelProperty("停车费标签")
    private String parkingFeeLabel;

    /**
     * 停车费描述
     */
    @ApiModelProperty("停车费描述")
    private String parkingFeeTip;

    /**
     * 是否优选（0：否,1：优选）
     */
    @ApiModelProperty("是否优选（0：否,1：优选）")
    private Integer recommend;

    /**
     * 是否显示（0否 1显示）
     */
    @ApiModelProperty("是否显示（0否 1显示）")
    private Integer showStatus;

    /**
     * 场站状态（0:关闭；1:开放；2:维护中）
     */
    @ApiModelProperty("场站状态（0:关闭；1:开放；2:维护中）")
    private Integer status;

    /**
     * 企业名称（服务提供）
     */
    @ApiModelProperty("企业名称（服务提供）")
    private String companyName;

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
     * 建设场所
     */
    @ApiModelProperty("建设场所")
    private Integer construction;

    /**
     * 道闸品牌编号
     */
    @ApiModelProperty("道闸品牌id")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long gateBrandId;

    /**
     * 道闸场站编号
     */
    @ApiModelProperty("道闸场站编号")
    private String barrierGateStationNo;

    /**
     * 停车减免时间（分钟）
     */
    @ApiModelProperty("停车减免时间（分钟）")
    private Integer parkingDiscountTime;
}
