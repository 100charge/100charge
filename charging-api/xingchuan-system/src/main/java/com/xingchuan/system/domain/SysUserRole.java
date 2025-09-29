package com.xingchuan.system.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

/**
 * 用户和角色关联 sys_user_role
 *
 * @author xingchuan
 */
@Data
public class SysUserRole {
    /**
     * 主键
     */
    @TableId
    private Long id;
    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 角色ID
     */
    private Long roleId;
}
