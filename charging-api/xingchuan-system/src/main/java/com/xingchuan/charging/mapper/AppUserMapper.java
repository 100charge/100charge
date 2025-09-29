package com.xingchuan.charging.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xingchuan.common.core.domain.entity.AppUser;

/**
 * 微信-用户Mapper接口
 *
 * @author ruoyi
 */
public interface AppUserMapper extends BaseMapper<AppUser> {

    /**
     * 根据openId查询用户信息
     *
     * @param openId openId
     * @return 结果
     */
    AppUser getInfoByOpenId(String openId);
}
