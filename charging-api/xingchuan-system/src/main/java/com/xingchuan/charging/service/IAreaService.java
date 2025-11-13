package com.xingchuan.charging.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xingchuan.charging.domain.resp.AreaListResponse;
import com.xingchuan.charging.domain.resp.AreaOpenCitiesResponse;
import com.xingchuan.common.core.domain.TreeSelect;
import com.xingchuan.common.core.domain.entity.Area;
import com.xingchuan.common.core.domain.model.AreaModel;

import java.util.List;


/**
 * 省市区Service接口
 */
public interface IAreaService extends IService<Area> {

    /**
     * 从redis获取省市区Map
     *
     * @return 结果
     */
    List<AreaModel> getAreaList();

    /**
     * 获取开放的城市
     *
     * @return 结果
     */
    List<AreaOpenCitiesResponse> getOpenCities();

    /**
     * 获取省市区树
     *
     * @return 结果
     */
    List<TreeSelect> selectAreaTreeList();

    /**
     * 获取省级下拉列表
     */
    List<AreaListResponse> provinceList();

    /**
     * 根据省级id获取市级下拉列表
     */
    List<AreaListResponse> getCityListByProvinceId(String provinceId);

    /**
     * 根据市级id获取区级下拉列表
     */
    List<AreaListResponse> getDistrictListByCityId(String cityId);

}
