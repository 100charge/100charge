package com.xingchuan.charging.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xingchuan.charging.domain.entity.ChargingPile;
import com.xingchuan.charging.domain.model.GunInfoModel;
import com.xingchuan.charging.domain.req.ChargingPilePageListRequest;
import com.xingchuan.charging.domain.resp.ChargingPilePageListResponse;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 充电桩Mapper接口
 *
 * @author ruoyi
 */
public interface ChargingPileMapper extends BaseMapper<ChargingPile> {

    /**
     * 查询充电桩列表
     */
    Page<ChargingPilePageListResponse> selectPageList(Page<ChargingPilePageListResponse> page, @Param("request") ChargingPilePageListRequest request);

    /**
     * 根据场站id查询桩编码和枪编码
     */
    List<GunInfoModel> getGunNoByStationId(Long id);
}
