package com.zhouyue.server.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zhouyue.server.pojo.AdminRole;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author zhou
 * @since 2022-02-18
 */
public interface AdminRoleMapper extends BaseMapper<AdminRole> {

    /**
     * 更新操作员角色
     * @param adminId
     * @param rids
     * @return
     */
    Integer updateAdminRoles(@Param("adminId") Integer adminId, @Param("rids") Integer[] rids);
}
