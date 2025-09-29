package com.xingchuan.charging.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xingchuan.charging.domain.entity.ChargingStations;
import com.xingchuan.charging.domain.entity.RuleDetails;
import com.xingchuan.charging.domain.entity.RuleTime;
import com.xingchuan.charging.domain.entity.Rules;
import com.xingchuan.charging.domain.req.*;
import com.xingchuan.charging.domain.resp.*;
import com.xingchuan.charging.enums.RuleTypeEnum;
import com.xingchuan.charging.mapper.ChargingStationsMapper;
import com.xingchuan.charging.mapper.RuleDetailsMapper;
import com.xingchuan.charging.mapper.RuleTimeMapper;
import com.xingchuan.charging.mapper.RulesMapper;
import com.xingchuan.charging.service.IRulesService;
import com.xingchuan.common.constant.MessageConstants;
import com.xingchuan.common.core.domain.entity.SysDept;
import com.xingchuan.common.exception.ServiceException;
import com.xingchuan.common.utils.DateUtils;
import com.xingchuan.common.utils.PageUtils;
import com.xingchuan.system.mapper.SysDeptMapper;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 计费规则Service业务层处理
 *
 * @author ruoyi
 */
@Service
public class RulesServiceImpl extends ServiceImpl<RulesMapper, Rules> implements IRulesService {

    @Resource
    private SysDeptMapper deptMapper;
    @Resource
    private RuleTimeMapper ruleTimeMapper;
    @Resource
    private RuleDetailsMapper ruleDetailsMapper;
    @Resource
    private ChargingStationsMapper stationsMapper;


    /**
     * 查询计费规则列表
     */
    @Override
    public Page<RulesPageListResponse> selectPageList(String name) {
        Page<Rules> pageInfo = PageUtils.getPageInfo(Rules.class);
        Page<RulesPageListResponse> responsePage = new Page<>(pageInfo.getCurrent(), pageInfo.getSize());
        Page<Rules> rulesPage = baseMapper.selectPage(pageInfo, Wrappers.<Rules>lambdaQuery()
                .select(Rules::getId, Rules::getName, Rules::getCreateBy,
                        Rules::getRemark, Rules::getCreateTime, Rules::getDeptId)
                .like(ObjectUtils.isNotEmpty(name), Rules::getName, name)
                .orderByDesc(Rules::getCreateTime)
        );
        List<Rules> records = rulesPage.getRecords();
        if (ObjectUtils.isNotEmpty(records)) {
            // 批量查询租户信息
            List<RulesPageListResponse> rulesPageList = records.stream().map(item -> {
                RulesPageListResponse response = new RulesPageListResponse();
                BeanUtils.copyProperties(item, response);
                return response;
            }).collect(Collectors.toList());
            responsePage.setTotal(rulesPage.getTotal());
            responsePage.setRecords(rulesPageList);
        }
        return responsePage;
    }

    /**
     * 获取计费规则详情
     */
    @Override
    public RulesDetailQueryResponse getRuleDetail(Long id) {
        RulesDetailQueryResponse rulesDetailQueryResponse = new RulesDetailQueryResponse();

        Rules rules = baseMapper.selectById(id);
        if (ObjectUtils.isEmpty(rules)) {
            return rulesDetailQueryResponse;
        }
        BeanUtils.copyProperties(rules, rulesDetailQueryResponse);
        return rulesDetailQueryResponse;
    }

    /**
     * 修改计费策略
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateRule(RulesUpdateRequest request) {


        Rules rules = baseMapper.selectById(request.getId());
        if (ObjectUtils.isEmpty(rules)) {
            throw new ServiceException(MessageConstants.RULE_NOT_EXIST);
        }
        // 查询运营商的部门ID
        Optional<SysDept> firstDept = deptMapper.selectList(Wrappers.<SysDept>lambdaQuery()
                .eq(SysDept::getTenantId, request.getTenantId())
                .eq(SysDept::getParentId, 0L)).stream().findFirst();
        if (!firstDept.isPresent()) {
            throw new ServiceException(MessageConstants.DEPT_NOT_EXIST);
        }
        rules.setDeptId(firstDept.get().getDeptId());
        BeanUtils.copyProperties(request, rules);
        baseMapper.updateById(rules);
    }

    /**
     * 新增计费策略
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addRule(RulesAddRequest request) {
        // 查询运营商的部门ID
        Optional<SysDept> firstDept = deptMapper.selectList(Wrappers.<SysDept>lambdaQuery()
                .eq(SysDept::getTenantId, request.getTenantId())
                .eq(SysDept::getParentId, 0L)).stream().findFirst();
        if (!firstDept.isPresent()) {
            throw new ServiceException(MessageConstants.DEPT_NOT_EXIST);
        }
        Rules rules = new Rules();
        rules.setDeptId(firstDept.get().getDeptId());
        BeanUtils.copyProperties(request, rules);
        baseMapper.insert(rules);
    }

    /**
     * 删除计费策略
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delRule(Long id) {
        Rules rules = baseMapper.selectById(id);
        if (ObjectUtils.isEmpty(rules)) {
            throw new ServiceException(MessageConstants.RULE_NOT_EXIST);
        }
        boolean exists = stationsMapper.exists(Wrappers.<ChargingStations>lambdaQuery()
                .eq(ChargingStations::getRuleId, rules.getId()));
        if (exists) {
            throw new ServiceException(MessageConstants.RULE_ALREADY_BOUND);
        }
        baseMapper.delete(Wrappers.<Rules>lambdaUpdate()
                .eq(Rules::getId, id));

        List<RuleTime> ruleTimes = ruleTimeMapper.selectList(Wrappers.<RuleTime>lambdaQuery()
                .select(RuleTime::getId).eq(RuleTime::getRuleId, id)
        );
        if (CollectionUtils.isNotEmpty(ruleTimes)) {
            List<Long> ruleTimeIds = ruleTimes.stream().map(RuleTime::getId).collect(Collectors.toList());
            ruleDetailsMapper.delete(Wrappers.<RuleDetails>lambdaUpdate()
                    .in(RuleDetails::getRuleTimeId, ruleTimeIds));
        }
        ruleTimeMapper.delete(Wrappers.<RuleTime>lambdaUpdate()
                .eq(RuleTime::getRuleId, id));
    }

    /**
     * 查询计费规则时段列表
     *
     * @param ruleId 规则id
     * @return 结果
     */
    @Override
    public List<RulesTimeListResponse> listRuleTimeByRuleId(Long ruleId) {

        List<RuleTime> ruleTimes = ruleTimeMapper.selectList(Wrappers.<RuleTime>lambdaQuery()
                .select(RuleTime::getId, RuleTime::getBeginTime, RuleTime::getEndTime, RuleTime::getRemark)
                .eq(RuleTime::getRuleId, ruleId).orderByAsc(RuleTime::getCreateTime)
        );

        if (CollectionUtils.isEmpty(ruleTimes)) {
            return Collections.emptyList();
        }

        return ruleTimes.stream().map(RulesTimeListResponse::new).collect(Collectors.toList());
    }

    /**
     * 获取计费规则时段详情
     *
     * @param id 规则时段id
     * @return 结果
     */
    @Override
    public RuleTimeDetailQueryResponse getRuleTimeDetail(Long id) {

        RuleTime ruleTime = ruleTimeMapper.selectById(id);

        if (ObjectUtils.isEmpty(ruleTime)) {
            return new RuleTimeDetailQueryResponse();
        }

        RuleTimeDetailQueryResponse response = new RuleTimeDetailQueryResponse(ruleTime);

        List<RuleDetails> detailsList = ruleDetailsMapper.selectList(Wrappers.<RuleDetails>lambdaQuery()
                .eq(RuleDetails::getRuleTimeId, id).orderByAsc(RuleDetails::getStartTime));
        if (ObjectUtils.isNotEmpty(detailsList)) {
            List<RuleDetailsResponse> ruleDetailList = new ArrayList<>();
            for (RuleDetails ruleDetails : detailsList) {
                RuleDetailsResponse ruleDetailsResponse = new RuleDetailsResponse();
                BeanUtils.copyProperties(ruleDetails, ruleDetailsResponse);
                ruleDetailsResponse.setTypeDesc(RuleTypeEnum.getDescByCode(ruleDetails.getType()));
                ruleDetailList.add(ruleDetailsResponse);
            }
            response.setRuleDetailList(ruleDetailList);
        }
        return response;
    }

    /**
     * 新增计费规则时段
     *
     * @param request 请求参数
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addRuleTime(RuleTimeAddRequest request) {
        // 生效时间范围
        Date beginTime = request.getBeginTime();
        Date endTime = request.getEndTime();

        Long ruleId = request.getRuleId();

        // 校验时间是否已经存在
        boolean exists = ruleTimeMapper.exists(Wrappers.<RuleTime>lambdaQuery()
                .eq(RuleTime::getRuleId, ruleId)
                .and(wrapper -> wrapper.le(RuleTime::getBeginTime, beginTime)
                        .or().le(RuleTime::getBeginTime, endTime)
                ).and(wrapper -> wrapper.ge(RuleTime::getEndTime, beginTime)
                        .or().ge(RuleTime::getEndTime, endTime)
                )
        );
        if (exists) {
            throw new ServiceException(MessageConstants.RULE_TIME_EXISTS);
        }

        // 查询运营商id
        Rules rules = baseMapper.selectOne(Wrappers.<Rules>lambdaQuery().eq(Rules::getId, request.getRuleId())
                .select(Rules::getDeptId).eq(Rules::getId, ruleId)
        );
        if (ObjectUtils.isEmpty(rules)) {
            throw new ServiceException(MessageConstants.RULE_NOT_EXIST);
        }

        Long deptId = rules.getDeptId();

        RuleTime ruleTime = new RuleTime(ruleId, beginTime, endTime, deptId, request.getRemark());
        ruleTimeMapper.insert(ruleTime);

        List<RuleDetailsAddRequest> ruleDetailList = request.getRuleDetailList();

        ruleDetailList.sort(Comparator.comparing(RuleDetailsAddRequest::getStartTime));
        for (int i = 0; i < ruleDetailList.size(); i++) {
            RuleDetailsAddRequest current = ruleDetailList.get(i);

            if (current.getStartTime().after(current.getEndTime())
                    || current.getStartTime().equals(current.getEndTime())) {
                throw new ServiceException(MessageConstants.START_TIME_LATER_THAN_END_TIME);
            }
            // 检查下一个元素的开始时间是否大于等于当前元素的结束时间
            if (i > 0) {
                RuleDetailsAddRequest previous = ruleDetailList.get(i - 1);

                if (!current.getStartTime().equals(previous.getEndTime())) {
                    throw new ServiceException(MessageConstants.RULE_TIME_PERIOD_ERROR);
                }
            }
            RuleDetails ruleDetails = new RuleDetails();
            ruleDetails.setRuleTimeId(ruleTime.getId());

            if (ObjectUtils.isNotEmpty(rules)) {
                ruleDetails.setDeptId(deptId);
            }
            Calendar instance = Calendar.getInstance();
            instance.setTime(current.getEndTime());
            instance.add(Calendar.MINUTE, -10);
            Integer startValue = DateUtils.billingRuleTimePeriod(current.getStartTime());
            Integer endValue = DateUtils.billingRuleTimePeriod(instance.getTime());

            ruleDetails.setStartValue(startValue);
            ruleDetails.setEndValue(endValue);
            ruleDetails.setOriginalServiceFee(current.getServiceFee());
            BeanUtils.copyProperties(current, ruleDetails);
            ruleDetailsMapper.insert(ruleDetails);
        }
    }

    /**
     * 修改计费规则时段
     *
     * @param request 请求参数
     */
    @Override
    public void updateRuleTime(RuleTimeUpdateRequest request) {
        // 生效时间范围
        Date beginTime = request.getBeginTime();
        Date endTime = request.getEndTime();

        Long id = request.getId();
        Long ruleId = request.getRuleId();

        // 校验时间是否已经存在
        boolean exists = ruleTimeMapper.exists(Wrappers.<RuleTime>lambdaQuery()
                .ne(RuleTime::getId, id).eq(RuleTime::getRuleId, ruleId)
                .and(wrapper -> wrapper.le(RuleTime::getBeginTime, beginTime)
                        .or().le(RuleTime::getBeginTime, endTime)
                ).and(wrapper -> wrapper.ge(RuleTime::getEndTime, beginTime)
                        .or().ge(RuleTime::getEndTime, endTime)
                )
        );
        if (exists) {
            throw new ServiceException(MessageConstants.RULE_TIME_EXISTS);
        }

        RuleTime ruleTime = ruleTimeMapper.selectById(id);
        if (ObjectUtils.isEmpty(ruleTime)) {
            throw new ServiceException(MessageConstants.RULE_TIME_NOT_EXIST);
        }
        // 更新
        ruleTime.setEndTime(endTime);
        ruleTime.setBeginTime(beginTime);
        ruleTime.setRemark(request.getRemark());
        ruleTimeMapper.updateById(ruleTime);

        Long deptId = ruleTime.getDeptId();

        // 删除原计费策略详情
        ruleDetailsMapper.delete(Wrappers.<RuleDetails>lambdaQuery().eq(RuleDetails::getRuleTimeId, request.getId()));

        List<RuleDetailsUpdateRequest> ruleDetailList = request.getRuleDetailList();
        ruleDetailList.sort(Comparator.comparing(RuleDetailsUpdateRequest::getStartTime));
        for (int i = 0; i < ruleDetailList.size(); i++) {
            RuleDetailsUpdateRequest current = ruleDetailList.get(i);

            if (current.getStartTime().after(current.getEndTime())
                    || current.getStartTime().equals(current.getEndTime())) {
                throw new ServiceException(MessageConstants.START_TIME_LATER_THAN_END_TIME);
            }
            // 检查下一个元素的开始时间是否大于等于当前元素的结束时间
            if (i > 0) {
                RuleDetailsUpdateRequest previous = ruleDetailList.get(i - 1);

                if (!current.getStartTime().equals(previous.getEndTime())) {
                    throw new ServiceException(MessageConstants.RULE_TIME_PERIOD_ERROR);
                }
            }
            Calendar instance = Calendar.getInstance();
            instance.setTime(current.getEndTime());
            instance.add(Calendar.MINUTE, -10);
            Integer startValue = DateUtils.billingRuleTimePeriod(current.getStartTime());
            Integer endValue = DateUtils.billingRuleTimePeriod(instance.getTime());

            RuleDetails ruleDetails = new RuleDetails();
            ruleDetails.setRuleTimeId(request.getId());
            ruleDetails.setType(current.getType());
            ruleDetails.setStartTime(current.getStartTime());
            ruleDetails.setEndTime(current.getEndTime());
            ruleDetails.setStartValue(startValue);
            ruleDetails.setEndValue(endValue);
            ruleDetails.setDeptId(deptId);
            ruleDetails.setChargeFee(current.getChargeFee());
            ruleDetails.setServiceFee(current.getServiceFee());
            ruleDetails.setParkingFee(current.getParkingFee());
            ruleDetails.setOccupancyFee(current.getOccupancyFee());
            ruleDetails.setRemark(current.getRemark());
            ruleDetails.setOriginalServiceFee(current.getServiceFee());
            ruleDetailsMapper.insert(ruleDetails);
        }
    }

    /**
     * 删除计费规则时段
     *
     * @param id 规则时段id
     */
    @Override
    public void delRuleTime(Long id) {
        if (stationsMapper.exists(Wrappers.<ChargingStations>lambdaQuery()
                .eq(ChargingStations::getRuleId, id))) {
            throw new ServiceException(MessageConstants.RULE_ALREADY_BOUND);
        }

        if (ruleTimeMapper.deleteById(id) == 0) {
            return;
        }

        ruleDetailsMapper.delete(Wrappers.<RuleDetails>lambdaUpdate()
                .eq(RuleDetails::getRuleTimeId, id));
    }

    /**
     * 计费规则下拉列表
     */
    @Override
    public List<RuleDropDownListResponse> dropDownList(Long tenantId, String name) {
        // 查询条件
        LambdaQueryWrapper<Rules> wrapper = Wrappers.<Rules>lambdaQuery()
                .select(Rules::getId, Rules::getName)
                .like(ObjectUtils.isNotEmpty(name), Rules::getName, name);
        // 查询租户下的组织架构id
        if (ObjectUtils.isNotEmpty(tenantId)) {
            List<Long> deptIdList = deptMapper.selectList(Wrappers.<SysDept>lambdaQuery()
                            .select(SysDept::getDeptId)
                            .eq(SysDept::getTenantId, tenantId))
                    .stream().map(SysDept::getDeptId).collect(Collectors.toList());
            wrapper.in(Rules::getDeptId, deptIdList);
        }
        // 查询
        List<Rules> rulesList = baseMapper.selectList(wrapper);
        // 返回
        if (ObjectUtils.isNotEmpty(rulesList)) {
            return rulesList.stream().map(rules -> {
                RuleDropDownListResponse ruleDropDownListResponse = new RuleDropDownListResponse();
                ruleDropDownListResponse.setId(rules.getId());
                ruleDropDownListResponse.setName(rules.getName());
                return ruleDropDownListResponse;
            }).collect(Collectors.toList());
        }
        return Collections.emptyList();
    }
}
