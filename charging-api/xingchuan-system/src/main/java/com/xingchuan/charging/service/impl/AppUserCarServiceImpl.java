package com.xingchuan.charging.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xingchuan.charging.domain.entity.AppUserCar;
import com.xingchuan.charging.domain.req.AppUserCarAddRequest;
import com.xingchuan.charging.domain.req.AppUserCarUpdateRequest;
import com.xingchuan.charging.domain.req.UserCarPageRequest;
import com.xingchuan.charging.domain.resp.AppUserCarResponse;
import com.xingchuan.charging.domain.resp.UserCarPageResponse;
import com.xingchuan.charging.enums.AppUserCarAuditVerifiedEnum;
import com.xingchuan.charging.enums.AppUserCarEnum;
import com.xingchuan.charging.enums.AuditStatusEnum;
import com.xingchuan.charging.mapper.AppUserCarMapper;
import com.xingchuan.charging.mapper.AppUserMapper;
import com.xingchuan.charging.service.IAppUserCarService;
import com.xingchuan.common.constant.MessageConstants;
import com.xingchuan.common.core.domain.entity.AppUser;
import com.xingchuan.common.exception.ServiceException;
import com.xingchuan.common.utils.PageUtils;
import com.xingchuan.common.utils.SecurityUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 用户认证车辆信息Service业务层处理
 */
@Service
public class AppUserCarServiceImpl extends ServiceImpl<AppUserCarMapper, AppUserCar> implements IAppUserCarService {

    @Resource
    private AppUserMapper appUserMapper;

    /**
     * 查询用户车辆列表
     */
    @Override
    public Page<UserCarPageResponse> userCarList(UserCarPageRequest request) {
        Page<Object> pageInfo = PageUtils.getPageInfo();
        Page<UserCarPageResponse> responsePage = new Page<>(pageInfo.getCurrent(), pageInfo.getSize());

        Page<AppUserCar> appUserCarPage = baseMapper.selectPage(new Page<>(pageInfo.getCurrent(), pageInfo.getSize()), Wrappers.<AppUserCar>lambdaQuery()
                .like(ObjectUtils.isNotEmpty(request.getOpenId()), AppUserCar::getOpenId, request.getOpenId())
                .like(ObjectUtils.isNotEmpty(request.getPlateNo()), AppUserCar::getPlateNo, request.getPlateNo())
                .eq(ObjectUtils.isNotEmpty(request.getVerified()), AppUserCar::getVerified, request.getVerified())
                .orderByDesc(AppUserCar::getCreateTime));

        List<AppUserCar> records = appUserCarPage.getRecords();
        if (records.isEmpty()) {
            return responsePage;
        }
        Set<String> openIds = records.stream().map(AppUserCar::getOpenId).filter(ObjectUtils::isNotEmpty).collect(Collectors.toSet());
        List<AppUser> appUserList = appUserMapper.selectList(Wrappers.<AppUser>lambdaQuery()
                .select(AppUser::getOpenId, AppUser::getPhoneNumber)
                .in(AppUser::getOpenId, openIds));

        Map<String, String> userMap = new HashMap<>();
        if (!appUserList.isEmpty()) {
            userMap = appUserList.stream().collect(Collectors.toMap(AppUser::getOpenId, AppUser::getPhoneNumber));
        }
        List<UserCarPageResponse> responseList = new ArrayList<>();
        for (AppUserCar record : records) {
            UserCarPageResponse response = new UserCarPageResponse();
            BeanUtils.copyProperties(record, response);
            response.setUsage(AppUserCarEnum.getDesc(record.getUsage()));
            response.setDefaultVehicle(AuditStatusEnum.getDesc(record.getDefaultVehicle()));
            response.setPhone(userMap.get(record.getOpenId()));
            responseList.add(response);
        }
        responsePage.setRecords(responseList);
        responsePage.setTotal(appUserCarPage.getTotal());
        return responsePage;
    }

    /**
     * 查询车辆列表
     */
    @Override
    public List<AppUserCarResponse> listAppUserCar() {
        AppUser appUser = SecurityUtils.getAppUser();
        String userOpenId = appUser.getOpenId();
        List<AppUserCar> appUserCarList = baseMapper.selectList(Wrappers.<AppUserCar>lambdaQuery()
                .select(AppUserCar::getId, AppUserCar::getPlateNo, AppUserCar::getVin, AppUserCar::getUsage,
                        AppUserCar::getVerified, AppUserCar::getDefaultVehicle, AppUserCar::getRemark)
                .eq(AppUserCar::getOpenId, userOpenId)
                .orderByDesc(AppUserCar::getCreateTime, AppUserCar::getDefaultVehicle)
        );
        if (CollectionUtils.isEmpty(appUserCarList)) {
            return Collections.emptyList();
        }
        List<AppUserCarResponse> responseList = new ArrayList<>();

        for (AppUserCar appUserCar : appUserCarList) {
            AppUserCarResponse response = new AppUserCarResponse();
            response.setAppCarId(appUserCar.getId());
            response.setVin(appUserCar.getVin());
            response.setPlateNo(appUserCar.getPlateNo());
            response.setVerified(appUserCar.getVerified());
            response.setIsDefault(appUserCar.getDefaultVehicle());
            response.setCarType(AppUserCarEnum.getDesc(appUserCar.getUsage()));
            response.setRemark(appUserCar.getRemark());
            responseList.add(response);
        }
        return responseList;
    }

    /**
     * 添加车辆
     *
     * @param request 请求参数
     * @return 结果
     */
    @Override
    public Integer addAppUserCar(AppUserCarAddRequest request) {
        // 校验车牌
        String plateNo = request.getPlateNo();
        String userOpenId = SecurityUtils.getUserOpenId();
        boolean exists = baseMapper.exists(Wrappers.<AppUserCar>lambdaQuery().eq(AppUserCar::getOpenId, userOpenId).eq(AppUserCar::getPlateNo, plateNo));
        if (exists) {
            throw new RuntimeException(MessageConstants.CAR_PLATE_EXIST);
        }

        AppUserCar appUserCar = new AppUserCar();
        appUserCar.setPlateNo(plateNo);
        appUserCar.setOpenId(userOpenId);
        appUserCar.setUsage(request.getCarType().getCode());
        appUserCar.setVerified(AppUserCarAuditVerifiedEnum.NOT_CERTIFIED.getCode());

        int defaultVehicle = request.getIsDefault() ? AuditStatusEnum.YES.getCode() : AuditStatusEnum.NO.getCode();

        if (request.getIsDefault()) {
            // 如果设置为默认,将其他车辆设置为非默认
            baseMapper.update(Wrappers.<AppUserCar>lambdaUpdate().eq(AppUserCar::getOpenId, userOpenId)
                    .set(AppUserCar::getDefaultVehicle, AuditStatusEnum.NO.getCode()));
            appUserCar.setDefaultVehicle(defaultVehicle);
        } else {
            // 判断是否已经存在默认车辆,如果不存在存在默认车辆，则将当前车辆设置为默认车辆
            boolean existsDefaultVehicle = baseMapper.exists(Wrappers.<AppUserCar>lambdaUpdate().eq(AppUserCar::getOpenId, userOpenId)
                    .eq(AppUserCar::getDefaultVehicle, AuditStatusEnum.YES.getCode()));
            if (!existsDefaultVehicle) {
                appUserCar.setDefaultVehicle(AuditStatusEnum.YES.getCode());
            } else {
                appUserCar.setDefaultVehicle(defaultVehicle);
            }
        }
        return baseMapper.insert(appUserCar);
    }

    @Override
    public Integer editPlateNo(AppUserCarUpdateRequest request) {
        String plateNo = request.getPlateNo();
        Long appCarId = request.getAppCarId();
        AppUserCar appUserCar = baseMapper.selectById(appCarId);
        if (ObjectUtils.isEmpty(appUserCar)) {
            throw new RuntimeException(MessageConstants.CAR_NOT_EXIST);
        }
        if (!appUserCar.getPlateNo().equals(plateNo)) {
            if (AppUserCarAuditVerifiedEnum.CERTIFIED.getCode().equals(appUserCar.getVerified())) {
                throw new ServiceException(MessageConstants.VEHICLE_CERTIFIED);
            }
        }
        String userOpenId = SecurityUtils.getUserOpenId();
        // 如果设置为默认判断是否已经存在默认了
        if (request.getIsDefault()) {
            baseMapper.update(Wrappers.<AppUserCar>lambdaUpdate().eq(AppUserCar::getOpenId, userOpenId)
                    .set(AppUserCar::getDefaultVehicle, AuditStatusEnum.NO.getCode()));
        }
        appUserCar.setPlateNo(plateNo);
        int defaultVehicle = request.getIsDefault() ? AuditStatusEnum.YES.getCode() : AuditStatusEnum.NO.getCode();
        appUserCar.setDefaultVehicle(defaultVehicle);
        return baseMapper.updateById(appUserCar);
    }

    @Override
    public Integer deleteById(Long carId) {
        return baseMapper.deleteById(carId);
    }
}
