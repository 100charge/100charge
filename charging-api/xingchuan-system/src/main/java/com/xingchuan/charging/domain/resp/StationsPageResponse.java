package com.xingchuan.charging.domain.resp;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;

/**
 * 场站对象分页返回对象
 *
 * @author ruoyi
 */
@Data
@ApiModel(value = "场站对象分页返回对象", description = "场站对象分页返回对象")
public class StationsPageResponse {

    /**
     * $column.columnComment
     */
    @ApiModelProperty(value = "id")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    /**
     * 场站名称
     */
    @ApiModelProperty(value = "场站名称")
    private String name;

    /**
     * 距离（米）
     */
    @ApiModelProperty(value = "距离（米）")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long distance;

    /**
     * 是否即插即冲（0：否；1：是）
     */
    @ApiModelProperty(value = "是否即插即冲（0：否；1：是）")
    private Integer plugCharge;

    /**
     * 星级
     */
    @ApiModelProperty(value = "星级")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long starLabel;

    /**
     * 最大功率
     */
    @JsonIgnore
    private Integer maxPower;

    /**
     * 当前纬度
     */
    @ApiModelProperty(value = "当前纬度")
    private BigDecimal lat;

    /**
     * 当前经度
     */
    @ApiModelProperty(value = "当前经度")
    private BigDecimal lng;

    /**
     * 地址
     */
    @ApiModelProperty(value = "地址")
    private String address;

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
     * 设施标签（最后返回的）
     */
    @ApiModelProperty(value = "设施标签（最后返回的）")
    private String labelName;

    /**
     * 停车费描述
     */
    @ApiModelProperty(value = "停车费描述")
    private String parkingFeeTip;

    /**
     * 是否优选（0：否,1：优选）
     */
    @ApiModelProperty(value = "是否优选（0：否,1：优选）")
    private String recommend;

    /**
     * 当前时段费用
     */
    @ApiModelProperty(value = "当前时段费用")
    @JsonSerialize(using = ToStringSerializer.class)
    private BigDecimal fee;

    /**
     * 充电类型（快充、慢充）
     */
    @ApiModelProperty(value = "快充、慢充")
    private String chargingType;

    /**
     * 快充数量
     */
    @ApiModelProperty(value = "快充数量")
    private Integer fastCharging;

    /**
     * 慢充数量
     */
    @ApiModelProperty(value = "慢充数量")
    private Integer slowCharging;


    /**
     * 闲置快充数量
     */
    @ApiModelProperty(value = "闲置快充数量")
    private Integer idleFastChargeCount;

    /**
     * 闲置慢充数量
     */
    @ApiModelProperty(value = "闲置慢充数量")
    private Integer idleSlowChargeCount;

    /**
     * 场站运营商id
     */
    @JsonIgnore
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
