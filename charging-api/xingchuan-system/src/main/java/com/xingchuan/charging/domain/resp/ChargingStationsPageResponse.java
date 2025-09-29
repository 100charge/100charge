package com.xingchuan.charging.domain.resp;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 场站对象分页返回对象
 *
 * @author ruoyi
 */
@Data
public class ChargingStationsPageResponse {

    /**
     * $column.columnComment
     */
    @ApiModelProperty("id")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    /**
     * 场站名称
     */
    @ApiModelProperty("场站名称")
    private String name;

    /**
     * 星级
     */
    @ApiModelProperty("星级")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long starLabel;

    /**
     * 场站服务（卫生间、休息室、便利店）
     */
    @ApiModelProperty("场站服务")
    private String serviceLabel;

    /**
     * 设施标签（地上、地下、洗车）
     */
    @ApiModelProperty("设施标签")
    private String facilityLabel;

    /**
     * 是否显示（0否 1显示）
     */
    @ApiModelProperty("是否显示（0否 1显示）")
    private Integer showStatus;

    /**
     * 运营商id
     */
    @ApiModelProperty("运营商id")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long tenantId;

    /**
     * 运营商名称
     */
    @ApiModelProperty("运营商名称")
    private String tenantName;

    /**
     * 计费规则名称
     */
    @ApiModelProperty("计费规则名称")
    private String ruleName;

    /**
     * 是否互联互通场站
     */
    private boolean operateStation;
}
