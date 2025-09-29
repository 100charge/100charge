package com.xingchuan.charging.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xingchuan.charging.domain.entity.Banner;
import com.xingchuan.charging.domain.req.BannerAddRequest;
import com.xingchuan.charging.domain.req.BannerOpenRequest;
import com.xingchuan.charging.domain.req.BannerUpdateRequest;
import com.xingchuan.charging.domain.resp.BannerListMiniResponse;
import com.xingchuan.charging.domain.resp.BannerListResponse;

import java.util.List;


/**
 * bannerService接口
 *
 * @author ruoyi
 */
public interface IBannerService extends IService<Banner> {
    /**
     * 查询banner
     *
     * @param id banner主键
     * @return banner
     */
    Banner selectBannerById(Long id);

    /**
     * 查询banner列表
     *
     * @param banner banner
     * @return banner集合
     */
    List<Banner> selectBannerList(Banner banner);


    /**
     * 新增banner
     *
     * @param request
     * @return 结果
     */
    int insertBanner(BannerAddRequest request);

    /**
     * 修改banner
     *
     * @param request
     * @return 结果
     */
    int updateBanner(BannerUpdateRequest request);

    /**
     * 开启/关闭banner
     *
     * @param request
     * @return
     */
    int openBanner(BannerOpenRequest request);

    /**
     * 批量删除banner
     *
     * @param ids 需要删除的banner主键集合
     * @return 结果
     */
    int deleteBannerByIds(String ids);

    /**
     * 删除banner信息
     *
     * @param id banner主键
     * @return 结果
     */
    int deleteBannerById(Long id);

    /**
     * 查询小程序banner列表
     *
     * @return
     */
    List<BannerListMiniResponse> selectBannerListMini();

    /**
     * 管理后台-分页查询banner列表
     */
    Page<BannerListResponse> selectPageList(String name, Integer openStatus);
}
