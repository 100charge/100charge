package com.xingchuan.charging.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xingchuan.charging.domain.entity.ChargingStations;
import com.xingchuan.charging.domain.model.ChargingPileModel;
import com.xingchuan.charging.domain.req.MapChargingStationsPageRequest;
import com.xingchuan.charging.domain.req.StationsPageRequest;
import com.xingchuan.charging.domain.resp.MiniStationsDetailResponse;
import com.xingchuan.charging.domain.resp.ReadyChargingResponse;
import com.xingchuan.charging.domain.resp.StationsPageResponse;
import com.xingchuan.charging.enums.ChargePileEnum;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;

/**
 * 场站Mapper接口
 *
 * @author ruoyi
 */
public interface ChargingStationsMapper extends BaseMapper<ChargingStations> {

    /**
     * 查询场站列表数量
     *
     * @param request  查询参数
     * @param pageNum  分页页数
     * @param pageSize 分页条数
     * @return 结果
     */
    Long listChargingStationsCount(@Param("request") StationsPageRequest request,
                                   @Param("pageNum") Integer pageNum, @Param("pageSize") Integer pageSize);

    /**
     * 查询场站列表
     *
     * @param request  查询参数
     * @param pageNum  分页页数
     * @param pageSize 分页条数
     * @return 结果
     */
    List<StationsPageResponse> listChargingStations(@Param("request") StationsPageRequest request,
                                                    @Param("pageNum") Integer pageNum, @Param("pageSize") Integer pageSize);

    /**
     * 根据场站id查询电桩信息
     *
     * @param stationIds 场站ids
     * @return 结果
     */
    List<ChargingPileModel> listStationDeviceByStationIds(@Param("stationIds") List<Long> stationIds);

    /**
     * 查询场站列表数量
     *
     * @param request 查询参数
     * @return 结果
     */
    List<StationsPageResponse> listMapStation(MapChargingStationsPageRequest request);

    /**
     * 根据id查询详情
     *
     * @param id  id
     * @param lat
     * @param lng
     * @return 结果
     */
    MiniStationsDetailResponse getMiniStationInfoById(@Param("id") Long id, @Param("lat") BigDecimal lat, @Param("lng") BigDecimal lng);

    /**
     * 根据场站id查询电桩信息
     *
     * @param stationId 场站id
     * @return 结果
     */
    List<ChargingPileModel> listStationDeviceByStationId(Long stationId);

    /**
     * 根据场站id + 桩类型 查询充电准备信息
     *
     * @param stationId 场站id
     * @param type      桩类型
     * @return 结果
     */
    List<ChargingPileModel> listStationDeviceByStationIdAndType(@Param("stationId") Long stationId, @Param("type") ChargePileEnum type);

    /**
     * 根据桩编号 + 枪编号 查询充电准备信息
     *
     * @param deviceNo 桩编号
     * @param gunsNo   枪编号
     * @return 结果
     */
    ReadyChargingResponse getChargePreparationInfo(@Param("deviceNo") String deviceNo, @Param("gunsNo") String gunsNo);

    /**
     * 根据桩编号 + 枪编号 查询充电准备信息
     *
     * @param gunsNo 枪编号
     * @return 结果
     */
    ReadyChargingResponse getChargePreparationInfoByDeviceNoAndGunsNo(@Param("gunsNo") String gunsNo);
}
