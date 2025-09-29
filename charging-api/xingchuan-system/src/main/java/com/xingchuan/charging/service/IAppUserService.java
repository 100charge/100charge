package com.xingchuan.charging.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xingchuan.charging.domain.excel.AppUserExportExcel;
import com.xingchuan.charging.domain.req.AppUserDisableOrEnableRequest;
import com.xingchuan.charging.domain.req.AppUserQueryRequest;
import com.xingchuan.charging.domain.req.CompanyUserQueryRequest;
import com.xingchuan.charging.domain.resp.AppEnterpriseUserListResponse;
import com.xingchuan.charging.domain.resp.AppUserBalanceResponse;
import com.xingchuan.charging.domain.resp.AppUserInfoResponse;
import com.xingchuan.charging.domain.resp.AppUserListResponse;
import com.xingchuan.common.core.domain.entity.AppUser;

import java.util.Date;
import java.util.List;


/**
 * 微信-用户Service接口
 *
 * @author ruoyi
 */
public interface IAppUserService extends IService<AppUser> {

    /**
     * 平台端-查询企业用户列表
     */
    Page<AppEnterpriseUserListResponse> platformCompanyUserList(CompanyUserQueryRequest request);

    /**
     * 查询分页
     *
     * @param request 请求参数
     * @return 结果
     */
    Page<AppUserListResponse> selectAppUserList(AppUserQueryRequest request);

    /**
     * 导出小程序用户列表
     */
    List<AppUserExportExcel> miniProgramUserExport(AppUserQueryRequest request);

    /**
     * 小程序用户-启用禁用
     */
    void appUserDisableOrEnable(AppUserDisableOrEnableRequest request);

    /**
     * 用户基础信息
     *
     * @param openId openid
     * @return 结果
     */
    AppUserInfoResponse getUserInfo(String openId);

    /**
     * 获取用户余额
     *
     * @param queryDate  查询事件
     * @param userOpenId openId
     * @return 结果
     */
    AppUserBalanceResponse getUserBalance(Date queryDate, String userOpenId);

    /**
     * 查询openId是否存在
     *
     * @param openId 用户openId
     * @return 结果
     */
    boolean existsOpenId(String openId);

    /**
     * 创建app用户和余额
     *
     * @param appUser app用户
     */
    void saveAndBalance(AppUser appUser);

    /**
     * 根据openId 查询用户信息
     *
     * @param openid 用户openId
     * @return 结果
     */
    AppUser getInfoByOpenId(String openid);


}
