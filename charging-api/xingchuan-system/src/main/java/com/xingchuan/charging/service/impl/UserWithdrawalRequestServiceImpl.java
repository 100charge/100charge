package com.xingchuan.charging.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xingchuan.charging.domain.entity.AppUserBalanceRecord;
import com.xingchuan.charging.domain.entity.UserWithdrawalDetail;
import com.xingchuan.charging.domain.entity.UserWithdrawalRequest;
import com.xingchuan.charging.domain.resp.UserWithdrawalListResponse;
import com.xingchuan.charging.domain.resp.WithdrawalDetailResponse;
import com.xingchuan.charging.enums.AppUserBalanceRecordEnum;
import com.xingchuan.charging.enums.BalanceRecordStatusEnum;
import com.xingchuan.charging.enums.RefundStatusEnum;
import com.xingchuan.charging.mapper.AppUserBalanceRecordMapper;
import com.xingchuan.charging.mapper.AppUserMapper;
import com.xingchuan.charging.mapper.UserWithdrawalDetailMapper;
import com.xingchuan.charging.mapper.UserWithdrawalRequestMapper;
import com.xingchuan.charging.service.IUserWithdrawalRequestService;
import com.xingchuan.common.core.domain.entity.AppUser;
import com.xingchuan.common.utils.PageUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * 用户提现审核表(UserWithdrawalRequest)表服务实现类
 *
 * @author makejava
 * @since 2024-07-06 17:11:44
 */
@Service
public class UserWithdrawalRequestServiceImpl extends ServiceImpl<UserWithdrawalRequestMapper, UserWithdrawalRequest>
        implements IUserWithdrawalRequestService {

    @Resource
    private UserWithdrawalDetailMapper userWithdrawalDetailMapper;

    @Resource
    private AppUserBalanceRecordMapper appUserBalanceRecordMapper;

    @Resource
    private AppUserMapper appUserMapper;

    /**
     * 分页查询
     */
    @Override
    public Page<UserWithdrawalListResponse> queryByPage(String openId) {
        Page<Object> pageInfo = PageUtils.getPageInfo();
        Page<UserWithdrawalListResponse> responsePage = new Page<>(pageInfo.getCurrent(), pageInfo.getSize());

        LambdaQueryWrapper<UserWithdrawalRequest> wrapper = Wrappers.<UserWithdrawalRequest>lambdaQuery()
                .eq(UserWithdrawalRequest::getOpenId, openId)
                .orderByDesc(UserWithdrawalRequest::getCreateTime);

        Page<UserWithdrawalRequest> selectPage = baseMapper
                .selectPage(new Page<>(pageInfo.getCurrent(), pageInfo.getSize()), wrapper);

        List<UserWithdrawalRequest> records = selectPage.getRecords();
        if (ObjectUtils.isNotEmpty(records)) {
            List<UserWithdrawalListResponse> responseList = new ArrayList<>();
            for (UserWithdrawalRequest record : records) {
                UserWithdrawalListResponse response = new UserWithdrawalListResponse();

                BeanUtils.copyProperties(record, response);

                // 查询提现明细
                List<UserWithdrawalDetail> detailList = userWithdrawalDetailMapper
                        .selectList(Wrappers.<UserWithdrawalDetail>lambdaQuery()
                                .eq(UserWithdrawalDetail::getParentId, record.getId()));
                if (ObjectUtils.isNotEmpty(detailList)) {
                    List<WithdrawalDetailResponse> detailResponseList = new ArrayList<>();
                    for (UserWithdrawalDetail detail : detailList) {
                        WithdrawalDetailResponse responseDetail = new WithdrawalDetailResponse();
                        BeanUtils.copyProperties(detail, responseDetail);
                        detailResponseList.add(responseDetail);
                    }
                    response.setDetailResponseList(detailResponseList);
                }

                responseList.add(response);
            }
            responsePage.setRecords(responseList);
            responsePage.setTotal(selectPage.getTotal());
        }

        return responsePage;
    }

    @Override
    public Page<UserWithdrawalListResponse> queryByPageMini(String openId) {
        Page<Object> pageInfo = PageUtils.getPageInfo();

        Page<UserWithdrawalListResponse> responsePage = new Page<>(pageInfo.getCurrent(), pageInfo.getSize());

        LambdaQueryWrapper<AppUserBalanceRecord> wrapper = Wrappers.<AppUserBalanceRecord>lambdaQuery()
                .eq(AppUserBalanceRecord::getOpenId, openId)
                .eq(AppUserBalanceRecord::getType, AppUserBalanceRecordEnum.WITHDRAW.getCode())
                .orderByDesc(AppUserBalanceRecord::getCreateTime);

        Page<AppUserBalanceRecord> selectPage = appUserBalanceRecordMapper
                .selectPage(new Page<>(pageInfo.getCurrent(), pageInfo.getSize()), wrapper);

        AppUser appuser = appUserMapper.selectOne(Wrappers.<AppUser>lambdaQuery()
                .eq(AppUser::getOpenId, openId));

        List<AppUserBalanceRecord> records = selectPage.getRecords();

        if (ObjectUtils.isNotEmpty(records)) {
            List<UserWithdrawalListResponse> responseList = new ArrayList<>();
            for (AppUserBalanceRecord record : records) {

                UserWithdrawalListResponse response = new UserWithdrawalListResponse();

                response.setId(record.getId());
                response.setOpenId(record.getOpenId());

                response.setPhone(appuser.getPhoneNumber());
                response.setTradeNo(record.getTradeNo());
                response.setType(0);
                response.setTypeDesc("小程序用户");
                response.setAmount(record.getAmount());
                response.setStatus(record.getStatus());
                response.setStatusDesc(BalanceRecordStatusEnum.getStatusDesc(record.getStatus()));

                if (record.getStatus() == BalanceRecordStatusEnum.INVALID.getCode()) {
                    response.setRefundStatus(RefundStatusEnum.PENDING_REFUND.getCode());
                    response.setRefundStatusDesc(
                            RefundStatusEnum.getDescByCode(RefundStatusEnum.PENDING_REFUND.getCode()));
                }

                if (record.getStatus() == BalanceRecordStatusEnum.PROCESSING.getCode()) {
                    response.setRefundStatus(RefundStatusEnum.REFUND_IN_PROGRESS.getCode());
                    response.setRefundStatusDesc(
                            RefundStatusEnum.getDescByCode(RefundStatusEnum.REFUND_IN_PROGRESS.getCode()));
                }
                if (record.getStatus() == BalanceRecordStatusEnum.SUCCESS.getCode()) {
                    response.setRefundStatus(RefundStatusEnum.REFUND_COMPLETED.getCode());
                    response.setRefundStatusDesc(
                            RefundStatusEnum.getDescByCode(RefundStatusEnum.REFUND_COMPLETED.getCode()));
                }
                if (record.getStatus() == BalanceRecordStatusEnum.FAILURE.getCode()) {
                    // response.setRefundStatus(RefundStatusEnum.PENDING_REFUND.getCode());
                    // response.setRefundStatusDesc(
                    //         RefundStatusEnum.getDescByCode(RefundStatusEnum.PENDING_REFUND.getCode()));
                }

                response.setCreateTime(record.getCreateTime());

                List<WithdrawalDetailResponse> detailResponseList = new ArrayList<>();
                response.setDetailResponseList(detailResponseList);
                responseList.add(response);
            }
            responsePage.setRecords(responseList);
            responsePage.setTotal(selectPage.getTotal());
        }

        return responsePage;
    }
}
