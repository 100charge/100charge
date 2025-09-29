package com.xingchuan.charging.domain.req;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 场站对象 charging_stations
 *
 * @author ruoyi
 */
@Data
public class ChargingStationsAddRequest {

    /**
     * 场站id
     */
    @ApiModelProperty("场站id")
    private Long id;

    /**
     * 运营商id
     */
    @ApiModelProperty("运营商id")
    @NotNull(message = "请选择所属运营商")
    private Long tenantId;

    /**
     * 场站名称
     */
    @NotBlank(message = "场站名称不能为空")
    @ApiModelProperty("场站名称")
    private String name;

    /**
     * 省
     */
    @NotNull(message = "省不能为空")
    @ApiModelProperty("省")
    private String province;

    /**
     * 市
     */
    @NotNull(message = "市不能为空")
    @ApiModelProperty("市")
    private String city;

    /**
     * 区
     */
    @NotNull(message = "区不能为空")
    @ApiModelProperty("区")
    private String region;

    /**
     * 场站详细地址
     */
    @NotBlank(message = "场站详细地址不能为空")
    @ApiModelProperty("场站详细地址")
    private String address;

    /**
     * 纬度
     */
    @NotNull(message = "纬度不能为空")
    @Min(value = -90, message = "纬度最小取值范围-90")
    @Max(value = 90, message = "纬度最大取值范围90")
    @ApiModelProperty("纬度")
    private BigDecimal lat;

    /**
     * 经度
     */
    @NotNull(message = "经度不能为空")
    @Min(value = -180, message = "经度最小取值范围-180")
    @Max(value = 180, message = "经度最大取值范围180")
    @ApiModelProperty("经度")
    private BigDecimal lng;

    /**
     * 计费规则id
     */
    @NotNull(message = "计费规则不能为空")
    @ApiModelProperty("计费规则id")
    private Long ruleId;

    /**
     * 营业开始时间
     */
    @ApiModelProperty("营业开始时间")
    @JsonFormat(pattern = "HH:mm:ss")
    @NotNull(message = "营业开始时间必填!")
    private Date perationBeginTime;

    /**
     * 营业截止时间
     */
    @ApiModelProperty("营业截止时间")
    @JsonFormat(pattern = "HH:mm:ss")
    @NotNull(message = "营业截止时间必填!")
    private Date perationEndTime;

    /**
     * 是否即插即冲（0：否；1：是）
     */
    @ApiModelProperty("是否即插即冲（0：否；1：是）")
    @NotNull(message = "是否即插即冲必填!")
    private Integer plugCharge;

    /**
     * 最大功率KW（>=180超充；30-179快充；<30慢充）
     */
    @ApiModelProperty("最大功率KW（>=180超充；30-179快充；<30慢充）")
    private Integer maxPower;

    /**
     * 星级
     */
    @ApiModelProperty("星级")
    @NotNull(message = "星级必选")
    @Min(value = 3, message = "星级最低为3星")
    @Max(value = 5, message = "星级最高为5星")
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
    @NotNull(message = "是否优选不能为空！")
    private Integer recommend;

    /**
     * 是否显示（0否 1显示）
     */
    @ApiModelProperty("是否显示（0否 1显示）")
    @NotNull(message = "是否显示不能为空!")
    private Integer showStatus;

    /**
     * 场站状态（0:关闭；1:开放；2:维护中）
     */
    @ApiModelProperty("场站状态（0:关闭；1:开放；2:维护中）")
    @NotNull(message = "场站状态不能为空！")
    private Integer status;

    /**
     * 企业名称（服务提供）
     */
    @ApiModelProperty("企业名称（服务提供）")
    private String companyName;

    /**
     * logo地址
     */
    @ApiModelProperty("logo地址")
    private String logoImage;

    /**
     * 营业执照地址
     */
    @ApiModelProperty("营业执照地址")
    private String licenseImage;

    /**
     * 建设场所
     */
    @ApiModelProperty("建设场所")
    private Integer construction;

    /**
     * 道闸品牌编号
     */
    @ApiModelProperty("道闸品牌id")
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

    /**
     * 场站图片集合
     */
    @ApiModelProperty("场站图片集合")
    private List<String> stationImageList;

}
