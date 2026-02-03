package com.xingchuan.charging.service.impl;


import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xingchuan.charging.domain.entity.AppUserBalance;
import com.xingchuan.charging.domain.entity.AppUserBalanceRecord;
import com.xingchuan.charging.domain.excel.AppUserExportExcel;
import com.xingchuan.charging.domain.req.AppUserDisableOrEnableRequest;
import com.xingchuan.charging.domain.req.AppUserQueryRequest;
import com.xingchuan.charging.domain.req.CompanyUserQueryRequest;
import com.xingchuan.charging.domain.resp.*;
import com.xingchuan.charging.enums.AppUserBalanceRecordEnum;
import com.xingchuan.charging.enums.AppUserEnum;
import com.xingchuan.charging.enums.AppUserStatusEnum;
import com.xingchuan.charging.enums.BalanceRecordStatusEnum;
import com.xingchuan.charging.mapper.AppUserBalanceMapper;
import com.xingchuan.charging.mapper.AppUserBalanceRecordMapper;
import com.xingchuan.charging.mapper.AppUserMapper;
import com.xingchuan.charging.service.IAppUserService;
import com.xingchuan.common.constant.Constants;
import com.xingchuan.common.constant.MessageConstants;
import com.xingchuan.common.core.domain.entity.AppUser;
import com.xingchuan.common.core.domain.entity.SysDept;
import com.xingchuan.common.core.page.PageDomain;
import com.xingchuan.common.core.page.TableSupport;
import com.xingchuan.common.exception.ServiceException;
import com.xingchuan.common.utils.bean.BeanUtils;
import com.xingchuan.system.mapper.SysDeptMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 微信-用户Service业务层处理
 *
 * @author ruoyi
 */
@Slf4j
@Service
public class AppUserServiceImpl extends ServiceImpl<AppUserMapper, AppUser> implements IAppUserService {

    @Resource
    private SysDeptMapper deptMapper;
    @Resource
    private AppUserBalanceMapper userBalanceMapper;
    @Resource
    private AppUserBalanceRecordMapper userBalanceRecordMapper;


    /**
     * 平台端-查询企业用户列表
     */
    @Override
    public Page<AppEnterpriseUserListResponse> platformCompanyUserList(CompanyUserQueryRequest request) {
        Page<AppEnterpriseUserListResponse> responsePage = new Page<>();
        // 分页
        PageDomain pageDomain = TableSupport.buildPageRequest();
        Integer pageNum = pageDomain.getPageNum();
        Integer pageSize = pageDomain.getPageSize();

        Page<AppUser> appUserPage = baseMapper.selectPage(new Page<>(pageNum, pageSize), Wrappers.<AppUser>lambdaQuery()
                .select(AppUser::getId, AppUser::getUserName, AppUser::getNickName, AppUser::getPhoneNumber, AppUser::getDeptId,
                        AppUser::getAvatar, AppUser::getStatus, AppUser::getType, AppUser::getCreateTime, AppUser::getUpdateTime,
                        AppUser::getFleetId)
                .like(StringUtils.isNotBlank(request.getUserName()), AppUser::getUserName, request.getUserName())
                .like(StringUtils.isNotBlank(request.getNickName()), AppUser::getNickName, request.getNickName())
                .like(StringUtils.isNotBlank(request.getPhoneNumber()), AppUser::getPhoneNumber, request.getPhoneNumber())
                .eq(ObjectUtils.isNotEmpty(request.getStatus()), AppUser::getStatus, request.getStatus() != null ? request.getStatus().getCode() : null)
                .orderByDesc(AppUser::getCreateTime));
        List<AppUser> records = appUserPage.getRecords();
        if (CollectionUtils.isEmpty(records)) {
            return responsePage;
        }
        responsePage.setTotal(appUserPage.getTotal());
        List<AppEnterpriseUserListResponse> responseList = new ArrayList<>();
        responsePage.setRecords(responseList);
        // 封装返回
        for (AppUser appUser : appUserPage.getRecords()) {
            AppEnterpriseUserListResponse response = new AppEnterpriseUserListResponse();
            BeanUtils.copyBeanProp(response, appUser);

            response.setCompanyId(appUser.getDeptId());
            response.setStatusDesc(AppUserStatusEnum.getName(appUser.getStatus()));
            if (ObjectUtils.isNotEmpty(appUser.getDeptId())) {
                SysDept dept = deptMapper.selectById(appUser.getDeptId());
                if (ObjectUtils.isNotEmpty(dept)) {
                    response.setEnterpriseName(dept.getDeptName());
                }
            }
            responseList.add(response);
        }
        return responsePage;
    }

    /**
     * 查询分页
     *
     * @param request 请求参数
     * @return 结果
     */
    @Override
    public Page<AppUserListResponse> selectAppUserList(AppUserQueryRequest request) {
        Page<AppUserListResponse> responsePage = new Page<>();
        // 分页
        PageDomain pageDomain = TableSupport.buildPageRequest();
        Integer pageNum = pageDomain.getPageNum();
        Integer pageSize = pageDomain.getPageSize();

        Page<AppUser> appUserPage = baseMapper.selectPage(new Page<>(pageNum, pageSize), Wrappers.<AppUser>lambdaQuery()
                .select(AppUser::getId, AppUser::getOpenId, AppUser::getNickName, AppUser::getPhoneNumber, AppUser::getAvatar,
                        AppUser::getStatus, AppUser::getType, AppUser::getCreateTime, AppUser::getUpdateTime)
                .like(StringUtils.isNotBlank(request.getUserName()), AppUser::getUserName, request.getUserName())
                .like(StringUtils.isNotBlank(request.getNickName()), AppUser::getNickName, request.getNickName())
                .like(StringUtils.isNotBlank(request.getPhoneNumber()), AppUser::getPhoneNumber, request.getPhoneNumber())
                .eq(ObjectUtils.isNotEmpty(request.getType()), AppUser::getType, request.getType() != null ? request.getType().getCode() : null)
                .eq(ObjectUtils.isNotEmpty(request.getStatus()), AppUser::getStatus, request.getStatus() != null ? request.getStatus().getCode() : null)
                .orderByDesc(AppUser::getCreateTime)
        );
        List<AppUser> records = appUserPage.getRecords();
        if (CollectionUtils.isEmpty(records)) {
            return responsePage;
        }
        responsePage.setTotal(appUserPage.getTotal());

        // 获取用户余额
        Map<String, BigDecimal> userBalanMap = new HashMap<>();
        List<String> openIds = records.stream().map(AppUser::getOpenId).filter(ObjectUtils::isNotEmpty).distinct().collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(openIds)) {
            userBalanMap = userBalanceMapper.selectList(Wrappers.<AppUserBalance>lambdaQuery()
                            .select(AppUserBalance::getOpenId, AppUserBalance::getBalance)
                            .in(AppUserBalance::getOpenId, openIds))
                    .stream().collect(Collectors.toMap(AppUserBalance::getOpenId, AppUserBalance::getBalance));
        }

        List<AppUserListResponse> responseList = new ArrayList<>();
        responsePage.setRecords(responseList);
        // 封装返回
        for (AppUser appUser : appUserPage.getRecords()) {
            AppUserListResponse response = new AppUserListResponse();
            BeanUtils.copyBeanProp(response, appUser);

            BigDecimal userBalance = userBalanMap.get(appUser.getOpenId());
            response.setUserBalance(userBalance);

            response.setTypeDesc(AppUserEnum.getName(appUser.getType()));
            response.setStatusDesc(AppUserStatusEnum.getName(appUser.getStatus()));

            responseList.add(response);
        }
        return responsePage;
    }

    /**
     * 导出小程序用户列表
     */
    @Override
    public List<AppUserExportExcel> miniProgramUserExport(AppUserQueryRequest request) {
        // 查询小程序用户列表
        List<AppUser> appUserList = baseMapper.selectList(Wrappers.<AppUser>lambdaQuery()
                .select(AppUser::getId, AppUser::getOpenId, AppUser::getNickName, AppUser::getPhoneNumber,
                        AppUser::getStatus, AppUser::getType, AppUser::getCreateTime)
                .like(StringUtils.isNotBlank(request.getUserName()), AppUser::getUserName, request.getUserName())
                .like(StringUtils.isNotBlank(request.getNickName()), AppUser::getNickName, request.getNickName())
                .like(StringUtils.isNotBlank(request.getPhoneNumber()), AppUser::getPhoneNumber, request.getPhoneNumber())
                .eq(ObjectUtils.isNotEmpty(request.getType()), AppUser::getType, request.getType() != null ? request.getType().getCode() : null)
                .eq(ObjectUtils.isNotEmpty(request.getStatus()), AppUser::getStatus, request.getStatus() != null ? request.getStatus().getCode() : null));
        List<AppUserExportExcel> exportExcelList = new ArrayList<>();
        if (ObjectUtils.isNotEmpty(appUserList)) {
            // 获取用户余额
            Map<String, BigDecimal> userBalanMap = new HashMap<>();
            List<String> openIds = appUserList.stream().map(AppUser::getOpenId).filter(ObjectUtils::isNotEmpty).distinct().collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(openIds)) {
                userBalanMap = userBalanceMapper.selectList(Wrappers.<AppUserBalance>lambdaQuery()
                                .select(AppUserBalance::getOpenId, AppUserBalance::getBalance)
                                .in(AppUserBalance::getOpenId, openIds))
                        .stream().collect(Collectors.toMap(AppUserBalance::getOpenId, AppUserBalance::getBalance));
            }
            for (AppUser appUser : appUserList) {
                AppUserExportExcel exportExcel = new AppUserExportExcel();
                BeanUtils.copyBeanProp(exportExcel, appUser);
                exportExcel.setUserBalance(userBalanMap.get(appUser.getOpenId()));
                exportExcelList.add(exportExcel);
            }
        }
        return exportExcelList;
    }

    /**
     * 小程序用户-启用禁用
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void appUserDisableOrEnable(AppUserDisableOrEnableRequest request) {
        //判断用户是否存在
        AppUser appUser = baseMapper.selectById(request.getId());
        if (ObjectUtils.isEmpty(appUser)) {
            throw new ServiceException(MessageConstants.USER_NOT_EXIST);
        }
        //判断用户当前状态是否正常,如果是正常状态,则当前操作为禁用,如果是禁用状态,则当前操作为启用
        if (AppUserStatusEnum.NORMAL.getCode() == appUser.getStatus()) {
            appUser.setStatus(AppUserStatusEnum.DISABLED.getCode());
        } else {
            appUser.setStatus(AppUserStatusEnum.NORMAL.getCode());
        }
        baseMapper.updateById(appUser);
    }

    /**
     * 用户基础信息
     *
     * @param openId openid
     * @return 结果
     */
    @Override
    public AppUserInfoResponse getUserInfo(String openId) {
        AppUserInfoResponse response = new AppUserInfoResponse();
        AppUser appUser = baseMapper.selectOne(Wrappers.<AppUser>lambdaQuery().eq(AppUser::getOpenId, openId));
        // 用户余额
        BigDecimal balance = BigDecimal.ZERO;
        AppUserBalance userBalance = userBalanceMapper.selectOne(
                Wrappers.<AppUserBalance>lambdaQuery().select(AppUserBalance::getBalance)
                        .eq(AppUserBalance::getOpenId, openId)
        );
        if (ObjectUtils.isNotEmpty(userBalance)) {
            balance = userBalance.getBalance();
        }

        response.setType(appUser.getType());
        response.setAvatar(appUser.getAvatar());
        response.setUserName(appUser.getUserName());
        response.setPhoneNumber(appUser.getPhoneNumber());
        response.setBalance(balance);

        return response;
    }

    /**
     * 获取用户余额
     *
     * @param queryDate  查询事件
     * @param userOpenId openId
     * @return 结果
     */
    @Override
    public AppUserBalanceResponse getUserBalance(Date queryDate, String userOpenId,AppUserBalanceRecordEnum recordEnum) {
        AppUserBalanceResponse response = new AppUserBalanceResponse();
        // 用户余额
        AppUserBalance userBalance = userBalanceMapper.selectOne(Wrappers.<AppUserBalance>lambdaQuery().eq(AppUserBalance::getOpenId, userOpenId));
        response.setBalance(userBalance.getBalance());
        // 时间范围
        Date beginTime = DateUtil.beginOfMonth(queryDate);
        Date endTime = DateUtil.endOfMonth(queryDate);
        // 余额明细
        List<AppUserBalanceRecord> balanceRecordList = userBalanceRecordMapper.selectList(Wrappers.<AppUserBalanceRecord>lambdaQuery()
                .select(AppUserBalanceRecord::getStationName, AppUserBalanceRecord::getType, AppUserBalanceRecord::getAmount, AppUserBalanceRecord::getCreateTime)
                .between(AppUserBalanceRecord::getCreateTime, beginTime, endTime)
                .eq(AppUserBalanceRecord::getOpenId, userOpenId)
                .eq(AppUserBalanceRecord::getStatus, BalanceRecordStatusEnum.SUCCESS.getCode())
                .eq(AppUserBalanceRecord::getType, recordEnum.getCode())
                .orderByDesc(AppUserBalanceRecord::getCreateTime));
        if (CollectionUtils.isEmpty(balanceRecordList)) {
            return response;
        }
        List<AppUserBalanceDetailsResponse> balanceDetailsResponseList = new ArrayList<>();

        BigDecimal rechargeTotal = BigDecimal.ZERO;
        BigDecimal consumptionTotal = BigDecimal.ZERO;

        for (AppUserBalanceRecord record : balanceRecordList) {
            AppUserBalanceDetailsResponse balanceDetailsResponse = new AppUserBalanceDetailsResponse();

            BigDecimal amount = record.getAmount();
            int type = record.getType();
            String amountStr;

            String name;
            if (Objects.equals(AppUserBalanceRecordEnum.CONSUME.getCode(), type)) {
                name = record.getStationName();
            } else {
                name = AppUserBalanceRecordEnum.getDesc(type);
            }
            if (Objects.equals(AppUserBalanceRecordEnum.CONSUME.getCode(), type) ||
                    Objects.equals(AppUserBalanceRecordEnum.WITHDRAW.getCode(), type)) {
                consumptionTotal = consumptionTotal.add(amount);
                amountStr = Constants.MINUS_SIGN + amount;
            } else {
                rechargeTotal = rechargeTotal.add(amount);
                amountStr = Constants.POSITIVE_SIGN + amount;
            }
            balanceDetailsResponse.setName(name);
            balanceDetailsResponse.setAmount(amountStr);
            balanceDetailsResponse.setTime(record.getCreateTime());
            balanceDetailsResponseList.add(balanceDetailsResponse);
        }
        response.setRechargeTotal(rechargeTotal);
        response.setConsumptionTotal(consumptionTotal);
        response.setDetailsResponseList(balanceDetailsResponseList);
        return response;
    }

    /**
     * 查询openId是否存在
     *
     * @param openId 用户openId
     * @return 结果
     */
    @Override
    public boolean existsOpenId(String openId) {
        return baseMapper.exists(Wrappers.<AppUser>lambdaQuery().eq(AppUser::getOpenId, openId));
    }

    /**
     * 创建app用户和余额
     *
     * @param appUser app用户
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveAndBalance(AppUser appUser) {
        baseMapper.insert(appUser);
        AppUserBalance userBalance = new AppUserBalance();

        userBalance.setOpenId(appUser.getOpenId());
        userBalance.setBalance(BigDecimal.ZERO);
        userBalanceMapper.insert(userBalance);
    }

    /**
     * 根据openId 查询用户信息
     *
     * @param openid 用户openId
     * @return 结果
     */
    @Override
    public AppUser getInfoByOpenId(String openid) {
        return baseMapper.getInfoByOpenId(openid);
    }


}
