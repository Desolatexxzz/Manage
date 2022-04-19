package com.zhouyue.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhouyue.server.mapper.MenuRoleMapper;
import com.zhouyue.server.pojo.MenuRole;
import com.zhouyue.server.pojo.RespBean;
import com.zhouyue.server.service.IMenuRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author zhou
 * @since 2022-02-18
 */
@Service
public class MenuRoleServiceImpl extends ServiceImpl<MenuRoleMapper, MenuRole> implements IMenuRoleService {

    @Autowired
    private  MenuRoleMapper menuRoleMapper;

    /**
     * 更新角色菜单
     * @param rid
     * @param mids
     * @return
     */
    @Override
    @Transactional //事务注解
    public RespBean updateRoleMenu(Integer rid, Integer[] mids) {
        //删除传入角色之前对应的菜单项，即在 t_menu_role 表中删除相关记录
        menuRoleMapper.delete(new QueryWrapper<MenuRole>().eq("rid", rid));
        if (mids == null || mids.length == 0){
            return RespBean.success("更新成功");
        }
        //将传入的 mids 对应的菜单项赋给 rid 对应的角色
        int i = menuRoleMapper.insertRecord(rid,mids);
        if (i == mids.length){
            return RespBean.success("更新成功");
        }
        return RespBean.error("更新失败");
    }
}
