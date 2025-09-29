package com.xingchuan.charging.domain.resp;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * 计费规则对象 rules
 *
 * @author ruoyi
 */
@Data
@ApiModel(value = "计费规则分页查询返回实体类")
public class RulesPageListResponse {

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
     * 创建者
     */
    @ApiModelProperty(value = "创建者")
    private String createBy;

    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
     * 备注
     */
    @ApiModelProperty(value = "备注")
    private String remark;

    /**
     * 所属运营商
     */
    @ApiModelProperty(value = "所属运营商")
    private String tenantName;
}
