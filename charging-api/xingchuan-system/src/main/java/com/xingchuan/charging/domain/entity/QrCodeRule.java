package com.xingchuan.charging.domain.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.util.regex.Pattern;

@Data
public class QrCodeRule {

    /**
     * ID
     */
    private Long id;

    /**
     * 规则描述
     */
    private String ruleDesc;

    /**
     * 正则表达式
     */
    private String regex;

    /**
     * 删除标志
     */
    private Integer delFlag;

    /**
     * 充电枪组
     */
    private Integer pileGroup;

    /**
     * 充电枪组
     */
    private Integer gunGroup;

    @TableField(exist = false)
    private Pattern pattern;

    public Pattern getPattern() {
        return pattern = Pattern.compile(regex);
    }
}
