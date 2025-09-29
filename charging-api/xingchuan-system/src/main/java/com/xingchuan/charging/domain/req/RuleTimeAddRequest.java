package com.xingchuan.charging.domain.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

/**
 * 计费规则时段对象 rules
 *
 * @author ruoyi
 */
@Data
@ApiModel(value = "计费规则新增请求实体类")
public class RuleTimeAddRequest {

    /**
     * 所属运营商
     */
    @ApiModelProperty(value = "所属运营商")
    @NotNull(message = "所属运营商不能为空!")
    private Long ruleId;

    /**
     * 生效开始时间
     */
    @DateTimeFormat(pattern = "MM-dd")
    @ApiModelProperty(value = "生效开始时间")
    @NotNull(message = "生效开始时间不能为空!")
    private Date beginTime;

    /**
     * 生效结束时间
     */
    @DateTimeFormat(pattern = "MM-dd")
    @ApiModelProperty(value = "生效结束时间")
    @NotNull(message = "生效结束时间不能为空!")
    private Date endTime;

    /**
     * 生效结束时间
     */
    @ApiModelProperty(value = "生效结束时间")
    private String remark;

    /**
     * 计费策略详情列表
     */
    @ApiModelProperty(value = "计费策略详情列表")
    @NotNull(message = "尖峰平谷时间段不能为空!")
    private List<RuleDetailsAddRequest> ruleDetailList;
}
