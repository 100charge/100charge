package com.xingchuan.charging.domain.resp;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 计费规则对象 rules
 *
 * @author ruoyi
 */
@Data
@ApiModel(value = "计费规则详情查询返回实体类")
public class RulesDetailQueryResponse {

    /**
     * 所属运营商
     */
    @ApiModelProperty(value = "所属运营商")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long tenantId;

    /**
     * 所属运营商
     */
    @ApiModelProperty(value = "所属运营商名称")
    private String tenantName;

    /**
     * 主键
     */
    @ApiModelProperty(value = "计费规则id")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    /**
     * 规则名称
     */
    @ApiModelProperty(value = "规则名称")
    private String name;

    /**
     * 备注
     */
    @ApiModelProperty(value = "备注")
    private String remark;

}
