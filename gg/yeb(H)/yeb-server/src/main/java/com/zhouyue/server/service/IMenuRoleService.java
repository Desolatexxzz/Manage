package com.zhouyue.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zhouyue.server.pojo.MenuRole;
import com.zhouyue.server.pojo.RespBean;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author zhou
 * @since 2022-02-18
 */
public interface IMenuRoleService extends IService<MenuRole> {

    /**
     * 更新角色菜单
     * @param rid
     * @param mids
     * @return
     */
    RespBean updateRoleMenu(Integer rid, Integer[] mids);
}
