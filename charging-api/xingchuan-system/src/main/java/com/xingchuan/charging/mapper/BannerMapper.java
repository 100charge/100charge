package com.xingchuan.charging.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xingchuan.charging.domain.entity.Banner;

import java.util.List;

/**
 * bannerMapper接口
 *
 * @author ruoyi
 */
public interface BannerMapper extends BaseMapper<Banner> {
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
     * @param banner banner
     * @return 结果
     */
    int insertBanner(Banner banner);

    /**
     * 修改banner
     *
     * @param banner banner
     * @return 结果
     */
    int updateBanner(Banner banner);

    /**
     * 删除banner
     *
     * @param id banner主键
     * @return 结果
     */
    int deleteBannerById(Long id);

    /**
     * 批量删除banner
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    int deleteBannerByIds(String[] ids);
}
