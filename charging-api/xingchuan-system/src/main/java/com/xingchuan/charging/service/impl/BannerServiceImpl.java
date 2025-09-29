package com.xingchuan.charging.service.impl;

import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xingchuan.charging.domain.entity.Banner;
import com.xingchuan.charging.domain.req.BannerAddRequest;
import com.xingchuan.charging.domain.req.BannerOpenRequest;
import com.xingchuan.charging.domain.req.BannerUpdateRequest;
import com.xingchuan.charging.domain.resp.BannerListMiniResponse;
import com.xingchuan.charging.domain.resp.BannerListResponse;
import com.xingchuan.charging.enums.BannerEnum;
import com.xingchuan.charging.mapper.BannerMapper;
import com.xingchuan.charging.service.IBannerService;
import com.xingchuan.common.constant.MessageConstants;
import com.xingchuan.common.core.text.Convert;
import com.xingchuan.common.utils.DateUtils;
import com.xingchuan.common.utils.PageUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * bannerService业务层处理
 *
 * @author ruoyi
 */
@Service
public class BannerServiceImpl extends ServiceImpl<BannerMapper, Banner> implements IBannerService {
    @Resource
    private BannerMapper bannerMapper;

    /**
     * 查询banner
     *
     * @param id banner主键
     * @return banner
     */
    @Override
    public Banner selectBannerById(Long id) {
        return bannerMapper.selectBannerById(id);
    }

    /**
     * 查询banner列表
     *
     * @param banner banner
     * @return banner
     */
    @Override
    public List<Banner> selectBannerList(Banner banner) {
        return bannerMapper.selectBannerList(banner);
    }

    /**
     * 新增banner
     *
     * @param request
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insertBanner(BannerAddRequest request) {
        Banner banner = new Banner();
        if (ObjectUtils.isEmpty(request.getOpen())) {
            request.setOpen(BannerEnum.NO.getCode());
        }
        BeanUtils.copyProperties(request, banner);
        banner.setCreateTime(DateUtils.getNowDate());
        return bannerMapper.insert(banner);
    }

    /**
     * 修改banner
     *
     * @param request
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateBanner(BannerUpdateRequest request) {
        Banner banner = bannerMapper.selectBannerById(request.getId());
        if (ObjectUtils.isEmpty(banner)) {
            throw new RuntimeException(MessageConstants.BANNER_NOT_EXIST);
        }
        banner.setName(request.getName());
        banner.setLink(request.getLink());
        banner.setImageUrl(request.getImageUrl());
        banner.setUpdateTime(DateUtils.getNowDate());
        return bannerMapper.updateById(banner);
    }

    /**
     * 启用/禁用banner
     *
     * @param request
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int openBanner(BannerOpenRequest request) {
        Banner banner = bannerMapper.selectBannerById(request.getId());
        if (ObjectUtils.isEmpty(banner)) {
            throw new RuntimeException(MessageConstants.BANNER_NOT_EXIST);
        }
        banner.setOpen(request.getOpen());
        banner.setUpdateTime(DateUtils.getNowDate());
        return bannerMapper.updateById(banner);
    }

    /**
     * 批量删除banner
     *
     * @param ids 需要删除的banner主键
     * @return 结果
     */
    @Override
    public int deleteBannerByIds(String ids) {
        return bannerMapper.deleteBannerByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除banner信息
     *
     * @param id banner主键
     * @return 结果
     */
    @Override
    public int deleteBannerById(Long id) {
        return bannerMapper.deleteBannerById(id);
    }

    /**
     * 查询小程序banner列表
     *
     * @return
     */
    @Override
    public List<BannerListMiniResponse> selectBannerListMini() {
        List<BannerListMiniResponse> result = new ArrayList<>();
        List<Banner> bannerList = bannerMapper.selectList(Wrappers.<Banner>lambdaQuery().eq(Banner::getOpen, BannerEnum.YES.getCode()).orderByDesc(Banner::getCreateTime));
        for (Banner banner : bannerList) {
            BannerListMiniResponse bannerInfo = new BannerListMiniResponse();
            BeanUtils.copyProperties(banner, bannerInfo);
            result.add(bannerInfo);
        }
        return result;
    }

    /**
     * 管理后台-分页获取banner列表
     *
     * @param name
     * @param openStatus
     * @return
     */
    @Override
    public Page<BannerListResponse> selectPageList(String name, Integer openStatus) {
        Page<Object> pageInfo = PageUtils.getPageInfo();
        Page<Banner> page = new Page<>(pageInfo.getCurrent(), pageInfo.getSize());
        Page<Banner> bannerPage = baseMapper.selectPage(page,
                Wrappers.<Banner>lambdaQuery()
                        .like(ObjectUtils.isNotEmpty(name), Banner::getName, name)
                        .eq(ObjectUtils.isNotEmpty(openStatus), Banner::getOpen, openStatus)
                        .orderByDesc(Banner::getCreateTime));

        Page<BannerListResponse> responsePage = new Page<>();
        if (bannerPage.getTotal() <= 0) {
            return responsePage;
        }

        responsePage.setTotal(bannerPage.getTotal());

        List<BannerListResponse> responseList = new ArrayList<>();
        for (Banner record : bannerPage.getRecords()) {
            BannerListResponse info = new BannerListResponse();
            BeanUtils.copyProperties(record, info);
            responseList.add(info);
        }
        responsePage.setRecords(responseList);
        return responsePage;
    }


}
