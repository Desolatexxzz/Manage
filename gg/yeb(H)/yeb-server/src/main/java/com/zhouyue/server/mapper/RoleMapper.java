package com.zhouyue.server.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zhouyue.server.pojo.Role;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author zhou
 * @since 2022-02-18
 */
public interface RoleMapper extends BaseMapper<Role> {

    /**
     * 根据用户 id 查询角色列表
     * @param adminId
     * @return
     */
    List<Role> getRoles(Integer adminId);
}
