package com.xingchuan.charging.domain.resp;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.xingchuan.charging.domain.entity.RuleTime;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * 计费规则对象 rules
 *
 * @author ruoyi
 */
@Data
@ApiModel(value = "计费规则详情查询返回实体类")
public class RuleTimeDetailQueryResponse {

    /**
     * 计费规则时段id
     */
    @ApiModelProperty(value = "计费规则时段id")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    /**
     * 开始时间
     */
    @ApiModelProperty(value = "开始时间")
    @JsonFormat(pattern = "MM-dd")
    private Date beginTime;

    /**
     * 截止时间
     */
    @ApiModelProperty(value = "截止时间")
    @JsonFormat(pattern = "MM-dd")
    private Date endTime;

    /**
     * 备注
     */
    @ApiModelProperty(value = "备注")
    private String remark;

    /**
     * 计费策略详情列表
     */
    @ApiModelProperty(value = "计费策略时间段详情列表")
    private List<RuleDetailsResponse> ruleDetailList;


    public RuleTimeDetailQueryResponse() {
    }

    public RuleTimeDetailQueryResponse(RuleTime ruleTime) {
        this.id = ruleTime.getId();
        this.beginTime = ruleTime.getBeginTime();
        this.endTime = ruleTime.getEndTime();
        this.remark = ruleTime.getRemark();
    }
}
