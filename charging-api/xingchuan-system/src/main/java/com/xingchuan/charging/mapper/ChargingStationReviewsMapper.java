package com.xingchuan.charging.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xingchuan.charging.domain.entity.ChargingStationReviews;
import com.xingchuan.charging.domain.model.EvaluationModel;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 场站评价Mapper接口
 *
 * @author ruoyi
 */
public interface ChargingStationReviewsMapper extends BaseMapper<ChargingStationReviews> {

    /**
     * 根据标签查询各标签数量
     *
     * @param stationId     场站id
     * @param dictValueList 标签集合
     * @return 结果
     */
    List<EvaluationModel> getEvaluation(@Param("stationId") Long stationId,
                                        @Param("dictValueList") List<String> dictValueList);

}