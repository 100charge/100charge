package com.xingchuan.charging.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xingchuan.charging.domain.entity.ChargingStations;
import com.xingchuan.charging.domain.req.*;
import com.xingchuan.charging.domain.resp.*;
import com.xingchuan.charging.enums.ChargePileEnum;

import java.util.List;

/**
 * 场站Service接口
 *
 * @author ruoyi
 */
public interface IChargingStationsService extends IService<ChargingStations> {

    /**
     * 查询场站列表
     *
     * @param request 场站
     * @return 场站集合
     */
    Page<ChargingStationsPageResponse> selectChargingStationsList(ChargingStationsPageRequest request);

    /**
     * 根据id查询场站详情对象
     *
     * @param id 场站id
     * @return 结果
     */
    ChargingStationsDetailResponse selectChargingStationsById(Long id);

    /**
     * 新增场站
     *
     * @param request 新增场站
     * @return 结果
     */
    int insertChargingStations(ChargingStationsAddRequest request);

    /**
     * 修改场站信息
     *
     * @param request 修改场站
     * @return 结果
     */
    int updateChargingStations(ChargingStationsAddRequest request);

    /**
     * 删除
     *
     * @param id 场站id
     */
    void deleteChargingStations(Long id);

    /**
     * 场站管理-是否展示状态切换
     */
    void updateShowStatus(Long id);

    /**
     * 查询场站下拉列表
     */
    List<StationsResponse> getStationList();

    /**
     * 根据运营商查询场站下拉列表
     */
    List<StationsResponse> getListByTenantId();

    /**
     * 小程序管理-查询场站列表-分页
     */
    Page<StationsPageResponse> selectMiniStationList(StationsPageRequest request);

    /**
     * 查询地图场站列表
     *
     * @param request 请求参数
     * @return 结果
     */
    List<StationsPageResponse> listMapStation(MapChargingStationsPageRequest request);

    /**
     * 小程序管理-查询场站详情
     */
    MiniStationsDetailResponse selectMiniStationDetailById(ChargingStationsDetailRequest request);

    /**
     * 根据场站id查询计费费用列表
     *
     * @param id 场站id
     * @return 结果
     */
    List<MiniRuleDetailsResponse> getStationRuleById(Long id);

    /**
     * 根据场站id查询场站设备信息
     *
     * @param stationId 场站id
     * @param type      快充、慢充
     * @return 结果
     */
    List<ChargingStationDeviceResponse> getStationDevice(Long stationId, ChargePileEnum type);

    /**
     * 根据传入枪号查询充电确认信息
     *
     * @param code 二维码参数
     * @return 结果
     */
    ReadyChargingResponse readyCharge(String code);


}
