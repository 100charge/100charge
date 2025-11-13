package com.xingchuan.charging.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xingchuan.charging.domain.entity.AppUserCar;
import com.xingchuan.charging.domain.req.AppUserCarAddRequest;
import com.xingchuan.charging.domain.req.AppUserCarUpdateRequest;
import com.xingchuan.charging.domain.req.UserCarPageRequest;
import com.xingchuan.charging.domain.resp.AppUserCarResponse;
import com.xingchuan.charging.domain.resp.UserCarPageResponse;

import java.util.List;

/**
 * 用户认证车辆信息Service接口
 */
public interface IAppUserCarService extends IService<AppUserCar> {

    /**
     * 查询用户车辆列表
     */
    Page<UserCarPageResponse> userCarList(UserCarPageRequest request);

    /**
     * 用户车辆车辆列表
     */
    List<AppUserCarResponse> listAppUserCar();

    /**
     * 添加车辆
     */
    Integer addAppUserCar(AppUserCarAddRequest request);

    /**
     * 修改车牌号
     */
    Integer editPlateNo(AppUserCarUpdateRequest request);

    /**
     * 删除车辆
     */
    Integer deleteById(Long carId);
}
