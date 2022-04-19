package com.zhouyue.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zhouyue.server.pojo.Admin;
import com.zhouyue.server.pojo.RespBean;
import com.zhouyue.server.pojo.Role;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author zhou
 * @since 2022-02-18
 */
public interface IAdminService extends IService<Admin> {

    /**
     * 根据用户名获取用户
     * @param username
     * @return
     */
    Admin getAdminByUserName(String username);

    /**
     * 根据用户 id 查询角色列表
     * @param adminId
     * @return
     */
    List<Role> getRoles(Integer adminId);

    /**
     * 获取所有操作员
     * @param keyWords
     * @return
     */
    List<Admin> getAllAdmins(String keyWords);

    /**
     * 更新操作员角色
     * @param adminId
     * @param rids
     * @return
     */
    RespBean updateAdminRoles(Integer adminId, Integer[] rids);

    /**
     * 更新用户密码
     * @param oldPass
     * @param pass
     * @param adminId
     * @return
     */
    RespBean updateAdminPassword(String oldPass, String pass, Integer adminId);
}
