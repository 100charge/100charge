package com.xingchuan.charging.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xingchuan.charging.domain.entity.Rules;
import com.xingchuan.charging.domain.req.*;
import com.xingchuan.charging.domain.resp.RuleTimeDetailQueryResponse;
import com.xingchuan.charging.domain.resp.RulesDetailQueryResponse;
import com.xingchuan.charging.domain.resp.RulesPageListResponse;
import com.xingchuan.charging.domain.resp.RulesTimeListResponse;

import java.util.List;

/**
 * 计费规则Service接口
 *
 * @author ruoyi
 */
public interface IRulesService extends IService<Rules> {

    /**
     * 查询计费规则列表
     */
    Page<RulesPageListResponse> selectPageList(String name);

    /**
     * 获取计费规则详情
     */
    RulesDetailQueryResponse getRuleDetail(Long id);

    /**
     * 修改计费策略
     */
    void updateRule(RulesUpdateRequest request);

    /**
     * 新增计费策略
     */
    void addRule(RulesAddRequest request);

    /**
     * 删除计费策略
     */
    void delRule(Long id);

    /**
     * 查询计费规则时段列表
     *
     * @param ruleId 规则id
     * @return 结果
     */
    List<RulesTimeListResponse> listRuleTimeByRuleId(Long ruleId);

    /**
     * 获取计费规则时段详情
     *
     * @param id 规则时段id
     * @return 结果
     */
    RuleTimeDetailQueryResponse getRuleTimeDetail(Long id);

    /**
     * 新增计费规则时段
     *
     * @param request 请求参数
     */
    void addRuleTime(RuleTimeAddRequest request);

    /**
     * 修改计费规则时段
     *
     * @param request 请求参数
     */
    void updateRuleTime(RuleTimeUpdateRequest request);

    /**
     * 删除计费规则时段
     *
     * @param id 规则时段id
     */
    void delRuleTime(Long id);

    /**
     * 计费规则下拉列表
     */
    List<RuleDropDownListResponse> dropDownList(Long tenantId, String name);
}
