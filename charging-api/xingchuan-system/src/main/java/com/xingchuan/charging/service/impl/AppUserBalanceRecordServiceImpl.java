package com.xingchuan.charging.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xingchuan.charging.domain.entity.AppUserBalanceRecord;
import com.xingchuan.charging.domain.resp.UserBalanceRecordListResponse;
import com.xingchuan.charging.enums.AppUserBalanceRecordEnum;
import com.xingchuan.charging.mapper.AppUserBalanceRecordMapper;
import com.xingchuan.charging.mapper.AppUserMapper;
import com.xingchuan.charging.service.IAppUserBalanceRecordService;
import com.xingchuan.common.core.domain.entity.AppUser;
import com.xingchuan.common.utils.PageUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 用户余额变动记录Service业务层处理
 *
 * @author ruoyi
 * @date 2024-06-14
 */
@Service
public class AppUserBalanceRecordServiceImpl extends ServiceImpl<AppUserBalanceRecordMapper, AppUserBalanceRecord> implements IAppUserBalanceRecordService {

    @Resource
    private AppUserMapper appUserMapper;


    /**
     * 小程序用户余额变动列表
     */
    @Override
    public Page<UserBalanceRecordListResponse> pageList(Integer type, String phone) {
        Page<Object> pageInfo = PageUtils.getPageInfo();
        Page<UserBalanceRecordListResponse> responsePage = new Page<>(pageInfo.getCurrent(), pageInfo.getSize());

        LambdaQueryWrapper<AppUserBalanceRecord> wrapper = Wrappers.<AppUserBalanceRecord>lambdaQuery()
                .eq(ObjectUtils.isNotEmpty(type), AppUserBalanceRecord::getType, type)
                .orderByDesc(AppUserBalanceRecord::getCreateTime);

        if (ObjectUtils.isNotEmpty(phone)) {
            List<AppUser> appUserList = appUserMapper.selectList(Wrappers.<AppUser>lambdaQuery().like(AppUser::getPhoneNumber, phone)
                    .orderByDesc(AppUser::getCreateTime));
            List<String> openIdList = appUserList.stream().map(AppUser::getOpenId).collect(Collectors.toList());
            if (ObjectUtils.isEmpty(openIdList)) {
                return responsePage;
            }
            wrapper.in(AppUserBalanceRecord::getOpenId, openIdList);
        }

        Page<AppUserBalanceRecord> balanceRecordPage = baseMapper.selectPage(new Page<>(pageInfo.getCurrent(), pageInfo.getSize()), wrapper);
        List<AppUserBalanceRecord> balanceRecordList = balanceRecordPage.getRecords();

        if (ObjectUtils.isNotEmpty(balanceRecordList)) {
            List<UserBalanceRecordListResponse> responseList = new ArrayList<>();
            for (AppUserBalanceRecord record : balanceRecordList) {
                UserBalanceRecordListResponse response = new UserBalanceRecordListResponse();
                BeanUtils.copyProperties(record, response);
                AppUser appUser = appUserMapper.selectOne(Wrappers.<AppUser>lambdaQuery()
                        .eq(AppUser::getOpenId, record.getOpenId()));
                if (ObjectUtils.isNotEmpty(appUser)) {
                    response.setPhone(appUser.getPhoneNumber());
                    response.setUserType(appUser.getType());
                }
                responseList.add(response);
            }
            responsePage.setRecords(responseList);
            responsePage.setTotal(balanceRecordPage.getTotal());
        }
        return responsePage;
    }

    /**
     * 小程序用户余额充值记录列表
     */
    @Override
    public Page<UserBalanceRecordListResponse> rechargeRecordList(String openId) {
        Page<Object> pageInfo = PageUtils.getPageInfo();
        Page<UserBalanceRecordListResponse> responsePage = new Page<>(pageInfo.getCurrent(), pageInfo.getSize());

        LambdaQueryWrapper<AppUserBalanceRecord> wrapper = Wrappers.<AppUserBalanceRecord>lambdaQuery()
                .eq(AppUserBalanceRecord::getType, AppUserBalanceRecordEnum.RECHARGE.getCode())
                .eq(AppUserBalanceRecord::getOpenId, openId)
                .orderByDesc(AppUserBalanceRecord::getCreateTime);

        Page<AppUserBalanceRecord> balanceRecordPage = baseMapper.selectPage(new Page<>(pageInfo.getCurrent(), pageInfo.getSize()), wrapper);
        List<AppUserBalanceRecord> balanceRecordList = balanceRecordPage.getRecords();

        if (ObjectUtils.isNotEmpty(balanceRecordList)) {
            List<UserBalanceRecordListResponse> responseList = new ArrayList<>();
            for (AppUserBalanceRecord record : balanceRecordList) {
                UserBalanceRecordListResponse response = new UserBalanceRecordListResponse();
                BeanUtils.copyProperties(record, response);
                AppUser appUser = appUserMapper.selectOne(Wrappers.<AppUser>lambdaQuery()
                        .eq(AppUser::getOpenId, record.getOpenId()));
                if (ObjectUtils.isNotEmpty(appUser)) {
                    response.setPhone(appUser.getPhoneNumber());
                    response.setUserType(appUser.getType());
                }
                responseList.add(response);
            }
            responsePage.setRecords(responseList);
            responsePage.setTotal(balanceRecordPage.getTotal());
        }
        return responsePage;
    }
}
