package com.xingchuan.charging.service.impl;

import com.xingchuan.charging.domain.entity.*;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xingchuan.charging.domain.model.EvaluationModel;
import com.xingchuan.charging.domain.req.ChargingStationEvaluationPageRequest;
import com.xingchuan.charging.domain.req.EvaluationRequest;
import com.xingchuan.charging.domain.resp.ChargingStationEvaluationPageResponse;
import com.xingchuan.charging.domain.resp.ChargingStationsPageResponse;
import com.xingchuan.charging.domain.resp.EvaluationListResponse;
import com.xingchuan.charging.domain.resp.UserEvaluationResponse;
import com.xingchuan.charging.mapper.ChargingStationReviewsMapper;
import com.xingchuan.charging.mapper.ChargingStationsMapper;
import com.xingchuan.charging.mapper.AppUserMapper;
import com.xingchuan.charging.service.IChargingStationReviewsService;
import com.xingchuan.common.constant.MessageConstants;
import com.xingchuan.common.core.domain.entity.AppUser;
import com.xingchuan.common.core.domain.entity.SysDictData;
import com.xingchuan.common.exception.ServiceException;
import com.xingchuan.common.utils.PageUtils;
import com.xingchuan.common.utils.SecurityUtils;
import com.xingchuan.common.utils.bean.BeanUtils;
import com.xingchuan.system.mapper.SysDictDataMapper;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 场站评价Service业务层处理
 *
 * @author ruoyi
 */
@Service
public class ChargingStationReviewsServiceImpl extends ServiceImpl<ChargingStationReviewsMapper, ChargingStationReviews>
        implements IChargingStationReviewsService {

    @Resource
    private SysDictDataMapper dictDataMapper;

    @Resource
    private ChargingStationsMapper chargingStationsMapper;

    @Resource
    private AppUserMapper appUserMapper;

    /**
     * 订单评价
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void orderEvaluation(EvaluationRequest request) {
        Long userId = SecurityUtils.getUserId();
        if (ObjectUtils.isEmpty(userId)) {
            throw new ServiceException(MessageConstants.NOT_OBTAINED_LOGIN_INFO);
        }
        if (ObjectUtils.isEmpty(request.getStationId())) {
            throw new ServiceException(MessageConstants.CHARGING_STATIONS_NOT_EXIST);
        }
        if (ObjectUtils.isEmpty(request.getOrderId())) {
            throw new ServiceException(MessageConstants.ORDER_NOT_EXIST);
        }

        for (String label : request.getLabelList()) {
            ChargingStationReviews stationReviews = new ChargingStationReviews();
            stationReviews.setChargingStationId(request.getStationId());
            stationReviews.setOrderId(request.getOrderId());
            stationReviews.setAppUserId(userId);
            stationReviews.setRating(label);
            baseMapper.insert(stationReviews);
        }
    }

    /**
     * 查看场站评价
     */
    @Override
    public List<EvaluationListResponse> getEvaluation(Long stationId) {
        List<EvaluationListResponse> responseList = new ArrayList<>();

        // 查询场站评价字典值
        List<SysDictData> dictDataList = getSysDictData();
        if (dictDataList.isEmpty()) {
            return responseList;
        }

        // 查询场站评价数量
        List<String> dictValueList = dictDataList.stream().map(SysDictData::getDictValue).collect(Collectors.toList());

        Map<String, Long> evaluationMap = baseMapper.getEvaluation(stationId, dictValueList)
                .stream()
                .collect(Collectors.toMap(EvaluationModel::getValue, EvaluationModel::getQty));

        for (SysDictData dict : dictDataList) {
            Long qty = evaluationMap.getOrDefault(dict.getDictValue(), 0L);

            EvaluationListResponse response = new EvaluationListResponse();
            response.setLabel(dict.getDictLabel());
            response.setValue(dict.getDictValue());
            response.setQty(qty);
            responseList.add(response);
        }
        return responseList;
    }

    /**
     * 根据订单查看评价
     */
    @Override
    public List<UserEvaluationResponse> getEvaluationByOrder(Long stationId, Long orderId) {
        List<UserEvaluationResponse> responseList = new ArrayList<>();
        // 查询场站评价字典值
        List<SysDictData> dictDataList = getSysDictData();

        // 查询用户评价记录
        if (ObjectUtils.isNotEmpty(dictDataList)) {
            for (SysDictData dictData : dictDataList) {

                boolean exists = baseMapper.exists(Wrappers.<ChargingStationReviews>lambdaQuery()
                        .eq(ChargingStationReviews::getChargingStationId, stationId)
                        .eq(ChargingStationReviews::getOrderId, orderId)
                        .eq(ChargingStationReviews::getRating, dictData.getDictValue()));

                UserEvaluationResponse response = new UserEvaluationResponse();
                response.setLabel(dictData.getDictLabel());
                response.setValue(dictData.getDictValue());
                response.setSelected(exists);
                responseList.add(response);
            }
        }
        return responseList;
    }

    /**
     * 查询场站评价字典值
     */
    private List<SysDictData> getSysDictData() {
        return dictDataMapper.selectList(Wrappers.<SysDictData>lambdaQuery()
                .eq(SysDictData::getDictType, "station_evaluate")
                .orderByAsc(SysDictData::getDictSort));
    }

    /**
     * 查询所有场站数据
     */
    private List<ChargingStations> getStations() {
        return chargingStationsMapper.selectList(Wrappers.<ChargingStations>lambdaQuery()
                .orderByAsc(ChargingStations::getId));
    }

    /**
     * 场站评价分页列表
     */
    @Override
    public Page<ChargingStationEvaluationPageResponse> getEvaluationPageList(
            ChargingStationEvaluationPageRequest request) {
        // 查询场站评价字典值
        List<SysDictData> dictDataList = getSysDictData();
        if (dictDataList.isEmpty()) {
            return new Page<>();
        }
        // 获得所有场站
        List<ChargingStations> stationsList = getStations();
        Map<Long, String> stationMap = stationsList.stream()
                .collect(Collectors.toMap(ChargingStations::getId, ChargingStations::getName));

        // 构建分页对象
        Page<ChargingStationEvaluationPageResponse> responsePage = new Page<>();

        Page<ChargingStationReviews> reviewsPage = baseMapper.selectPage(
                PageUtils.getPageInfo(),
                Wrappers.<ChargingStationReviews>lambdaQuery()

                        .eq(ObjectUtils.isNotEmpty(request.getStationId()) && request.getStationId() != 0,
                                ChargingStationReviews::getChargingStationId, request.getStationId())
                        .ge(ObjectUtils.isNotEmpty(request.getStartTime()), ChargingStationReviews::getCreateTime, request.getStartTime())
                        .le(ObjectUtils.isNotEmpty(request.getEndTime()), ChargingStationReviews::getCreateTime, request.getEndTime())
                        .eq(ObjectUtils.isNotEmpty(request.getTradeNo()), ChargingStationReviews::getOrderId, request.getTradeNo())
                        .orderByDesc(ChargingStationReviews::getId));

        List<ChargingStationReviews> reviewsList = reviewsPage.getRecords();
        

        // 提取所有用户ID
        List<Long> userIds = reviewsList.stream()
                .map(ChargingStationReviews::getAppUserId)
                .distinct()
                .collect(Collectors.toList());
        
        // 批量查询用户信息，构建 Map
        Map<Long, String> userMap = new HashMap<>();
        if (!userIds.isEmpty()) {
            List<AppUser> userList = appUserMapper.selectList(
                    Wrappers.<AppUser>lambdaQuery()
                            .in(AppUser::getId, userIds));
            userMap = userList.stream()
                    .collect(Collectors.toMap(AppUser::getId, AppUser::getNickName));
        }

        // 设置分页数据
        responsePage.setTotal(reviewsPage.getTotal());
        final Map<Long, String> finalUserMap = userMap;
        responsePage.setRecords(reviewsList.stream().map(item -> {
            ChargingStationEvaluationPageResponse response = new ChargingStationEvaluationPageResponse();
            BeanUtils.copyBeanProp(response, item);

            // 设置场站名称
            String stationName = stationMap.getOrDefault(item.getChargingStationId(), "");
            response.setChargingStationName(stationName);

            // 设置评价标签_字符
            String ratingValue = item.getRating();
            String ratingString = dictDataList.stream()
                    .filter(dict -> dict.getDictValue().equals(ratingValue))
                    .map(SysDictData::getDictLabel)
                    .findFirst()
                    .orElse("");
            response.setRatingString(ratingString);

            // 设置评价用户
            String userName = finalUserMap.getOrDefault(item.getAppUserId(), "未知用户");
            response.setAppUser(userName);

            return response;
        }).collect(Collectors.toList()));

        return responsePage;
    }
}
