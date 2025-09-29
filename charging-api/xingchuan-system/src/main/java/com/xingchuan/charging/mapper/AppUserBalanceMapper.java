package com.xingchuan.charging.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xingchuan.charging.domain.entity.AppUserBalance;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;

/**
 * 客户余额Mapper接口
 *
 * @author ruoyi
 */
public interface AppUserBalanceMapper extends BaseMapper<AppUserBalance> {


    /**
     * 根据用户id更新用户余额
     *
     * @param openId 用户openId
     * @param amount 金额
     * @return 结果
     */
    int updateBalanceByOpenId(@Param("openId") String openId, @Param("amount") BigDecimal amount);

}
