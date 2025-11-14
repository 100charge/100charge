package com.xingchuan.charging.service.impl;

import com.alibaba.fastjson2.JSONArray;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xingchuan.charging.domain.resp.AreaListResponse;
import com.xingchuan.charging.domain.resp.AreaOpenCitiesResponse;
import com.xingchuan.charging.enums.AreaEnum;
import com.xingchuan.charging.mapper.AreaMapper;
import com.xingchuan.charging.service.IAreaService;
import com.xingchuan.common.constant.CacheConstants;
import com.xingchuan.common.core.domain.TreeSelect;
import com.xingchuan.common.core.domain.entity.Area;
import com.xingchuan.common.core.domain.model.AreaModel;
import com.xingchuan.common.core.redis.RedisCache;
import com.xingchuan.common.utils.StringUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * 省市区Service业务层处理
 *
 * @author ruoyi
 */
@Service
public class AreaServiceImpl extends ServiceImpl<AreaMapper, Area> implements IAreaService {

    @Resource
    private RedisCache redisCache;

    /**
     * 获取开放的城市
     *
     * @return 结果
     */
    @Override
    public List<AreaOpenCitiesResponse> getOpenCities() {
        List<AreaOpenCitiesResponse> responseList = new ArrayList<>();
        List<Area> areas = baseMapper.selectList(Wrappers.<Area>lambdaQuery()
                .select(Area::getName, Area::getCode, Area::getLat, Area::getLng)
                .eq(Area::getOpen, AreaEnum.YES.getCode())
                .isNotNull(Area::getLat).isNotNull(Area::getLng)
                .orderByDesc(Area::getCreateTime)
        );
        if (CollectionUtils.isEmpty(areas)) {
            return responseList;
        }

        areas.forEach(area -> {
            AreaOpenCitiesResponse response = new AreaOpenCitiesResponse();
            response.setName(area.getName());
            response.setCode(area.getCode());
            response.setLat(area.getLat());
            response.setLng(area.getLng());
            responseList.add(response);
        });
        return responseList;
    }

    /**
     * 从redis获取省市区Map
     *
     * @return 结果
     */
    @Override
    public List<AreaModel> getAreaList() {
        String key = CacheConstants.REGION_KEY + "AREA_LIST";

        String json = redisCache.getCacheObject(key);

        if (ObjectUtils.isNotEmpty(json)) {
            List<AreaModel> areaModels = JSONArray.parseArray(json, AreaModel.class);
            areaModels.sort(Comparator.comparing(AreaModel::getCode));
            return areaModels;
        }

        List<Area> areaList = baseMapper.selectList(Wrappers.<Area>lambdaQuery()
                .select(Area::getId, Area::getName, Area::getCode, Area::getFullName,
                        Area::getTreePath, Area::getParentId, Area::getLat, Area::getLng, Area::getCode)
                .eq(Area::getOpen, 1)
        );
        if (ObjectUtils.isEmpty(areaList)) {
            return Collections.emptyList();
        }

        List<AreaModel> areaModelList = areaList.stream().map(AreaModel::new).collect(Collectors.toList());


        // 缓存
        redisCache.setCacheObject(key, JSONArray.toJSONString(areaModelList), 360, TimeUnit.SECONDS);

        // 排序
        areaModelList.sort(Comparator.comparing(AreaModel::getCode));

        return areaModelList;
    }

    /**
     * 获取省市区树
     *
     * @return 结果
     */
    @Override
    public List<TreeSelect> selectAreaTreeList() {

        List<AreaModel> areaList = getAreaList();
        List<AreaModel> areaTrees = buildAreaTree(areaList);
        return areaTrees.stream().map(TreeSelect::new).collect(Collectors.toList());
    }

    /**
     * 获取省级下拉列表
     */
    @Override
    public List<AreaListResponse> provinceList() {
        List<AreaListResponse> responseList = new ArrayList<>();
        List<Area> provinceList = baseMapper.selectList(Wrappers.<Area>lambdaQuery()
                .isNull(Area::getParentId).orderByDesc(Area::getCreateTime));
        if (ObjectUtils.isNotEmpty(provinceList)) {
            for (Area area : provinceList) {
                AreaListResponse response = new AreaListResponse();
                response.setId(Long.valueOf(area.getCode()));
                response.setName(area.getName());
                responseList.add(response);
            }
        }
        return responseList;
    }

    /**
     * 根据省级id获取市级下拉列表
     */
    @Override
    public List<AreaListResponse> getCityListByProvinceId(String provinceId) {
        Area province = baseMapper.selectOne(Wrappers.<Area>lambdaQuery()
                .select(Area::getId)
                .eq(Area::getCode, provinceId).last("limit 1"));
        if (ObjectUtils.isEmpty(province)) {
            return Collections.emptyList();
        }
        List<AreaListResponse> responseList = new ArrayList<>();
        List<Area> provinceList = baseMapper.selectList(Wrappers.<Area>lambdaQuery()
                .eq(Area::getParentId, province.getId()).orderByDesc(Area::getCreateTime));
        if (ObjectUtils.isNotEmpty(provinceList)) {
            for (Area area : provinceList) {
                AreaListResponse response = new AreaListResponse();
                response.setId(Long.valueOf(area.getCode()));
                response.setName(area.getName());
                responseList.add(response);
            }
        }
        return responseList;
    }

    /**
     * 根据市级id获取区级下拉列表
     */
    @Override
    public List<AreaListResponse> getDistrictListByCityId(String cityId) {
        Area city = baseMapper.selectOne(Wrappers.<Area>lambdaQuery()
                .select(Area::getId)
                .eq(Area::getCode, cityId).last("limit 1"));
        if (ObjectUtils.isEmpty(city)) {
            return Collections.emptyList();
        }
        List<AreaListResponse> responseList = new ArrayList<>();
        List<Area> provinceList = baseMapper.selectList(Wrappers.<Area>lambdaQuery()
                .eq(Area::getParentId, city.getId()).orderByDesc(Area::getCreateTime));
        if (ObjectUtils.isNotEmpty(provinceList)) {
            for (Area area : provinceList) {
                AreaListResponse response = new AreaListResponse();
                response.setId(Long.valueOf(area.getCode()));
                response.setName(area.getName());
                responseList.add(response);
            }
        }
        return responseList;
    }

    public List<AreaModel> buildAreaTree(List<AreaModel> areas) {
        List<AreaModel> returnList = new ArrayList<>();
        List<Long> tempList = areas.stream().map(AreaModel::getId).collect(Collectors.toList());
        for (AreaModel area : areas) {
            // 如果是顶级节点, 遍历该父节点的所有子节点
            if (!tempList.contains(area.getParentId())) {
                recursionFn(areas, area);
                returnList.add(area);
            }
        }
        if (returnList.isEmpty()) {
            returnList = areas;
        }
        return returnList;
    }

    /**
     * 递归列表
     */
    private void recursionFn(List<AreaModel> list, AreaModel t) {
        // 得到子节点列表
        List<AreaModel> childList = getChildList(list, t);
        t.setChildren(childList);
        for (AreaModel tChild : childList) {
            if (hasChild(list, tChild)) {
                recursionFn(list, tChild);
            }
        }
    }

    /**
     * 得到子节点列表
     */
    private List<AreaModel> getChildList(List<AreaModel> list, AreaModel t) {
        List<AreaModel> tlist = new ArrayList<>();
        for (AreaModel n : list) {
            if (StringUtils.isNotNull(n.getParentId()) && n.getParentId().longValue() == t.getId().longValue()) {
                tlist.add(n);
            }
        }
        return tlist;
    }

    /**
     * 判断是否有子节点
     */
    private boolean hasChild(List<AreaModel> list, AreaModel t) {
        return !getChildList(list, t).isEmpty();
    }
}
