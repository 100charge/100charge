package com.xingchuan.charging.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xingchuan.charging.domain.entity.ChargingStationReviews;
import com.xingchuan.charging.domain.model.EvaluationModel;
import com.xingchuan.charging.domain.req.EvaluationRequest;
import com.xingchuan.charging.domain.resp.EvaluationListResponse;
import com.xingchuan.charging.domain.resp.UserEvaluationResponse;
import com.xingchuan.charging.mapper.ChargingStationReviewsMapper;
import com.xingchuan.charging.service.IChargingStationReviewsService;
import com.xingchuan.common.constant.MessageConstants;
import com.xingchuan.common.core.domain.entity.SysDictData;
import com.xingchuan.common.exception.ServiceException;
import com.xingchuan.common.utils.SecurityUtils;
import com.xingchuan.system.mapper.SysDictDataMapper;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
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

        //查询场站评价字典值
        List<SysDictData> dictDataList = getSysDictData();
        if (dictDataList.isEmpty()) {
            return responseList;
        }

        //查询场站评价数量
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
        //查询场站评价字典值
        List<SysDictData> dictDataList = getSysDictData();

        //查询用户评价记录
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
}
