package com.xingchuan.charging.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xingchuan.charging.domain.resp.AreaOpenCitiesResponse;
import com.xingchuan.common.core.domain.entity.Area;
import com.xingchuan.common.core.domain.model.AreaModel;

import java.util.List;


/**
 * 省市区Service接口
 *
 * @author ruoyi
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

}
