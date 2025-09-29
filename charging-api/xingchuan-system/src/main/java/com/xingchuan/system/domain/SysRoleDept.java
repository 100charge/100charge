package com.xingchuan.system.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

/**
 * 角色和部门关联 sys_role_dept
 *
 * @author xingchuan
 */
@Data
public class SysRoleDept {

    /**
     * 主键
     */
    @TableId
    private Long id;

    /**
     * 角色ID
     */
    private Long roleId;

    /**
     * 部门ID
     */
    private Long deptId;
}
