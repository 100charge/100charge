package com.xingchuan.charging.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xingchuan.charging.domain.resp.UserWithdrawalListResponse;

/**
 * 用户提现审核表(UserWithdrawalRequest)表服务接口
 */
public interface IUserWithdrawalRequestService {

    /**
     * 分页查询
     */
    Page<UserWithdrawalListResponse> queryByPage(String openId);
}
