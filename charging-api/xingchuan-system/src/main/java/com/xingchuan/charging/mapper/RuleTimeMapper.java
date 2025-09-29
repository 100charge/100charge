package com.xingchuan.charging.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xingchuan.charging.domain.entity.RuleDetails;
import com.xingchuan.charging.domain.entity.RuleTime;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * 计费策略时段Mapper接口
 *
 * @author ruoyi
 */
public interface RuleTimeMapper extends BaseMapper<RuleTime> {

    /**
     * 根据计费策略id获取当前时间段费用
     *
     * @param ruleId 计费策略id
     * @param date   查询时间
     * @return 结果
     */
    RuleDetails getCurrentPeriodCostByRuleId(@Param("ruleId") Long ruleId, @Param("date") Date date);

    /**
     * 根据计费策略id获取当前时间段费用列表
     *
     * @param ruleId 计费策略id
     * @return 结果
     */
    List<RuleDetails> listCurrentPeriodCostByRuleId(Long ruleId);


}
