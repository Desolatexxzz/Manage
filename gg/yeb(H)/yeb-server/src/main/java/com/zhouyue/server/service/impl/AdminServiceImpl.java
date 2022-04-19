package com.zhouyue.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhouyue.server.mapper.AdminMapper;
import com.zhouyue.server.mapper.AdminRoleMapper;
import com.zhouyue.server.mapper.RoleMapper;
import com.zhouyue.server.pojo.Admin;
import com.zhouyue.server.pojo.AdminRole;
import com.zhouyue.server.pojo.RespBean;
import com.zhouyue.server.pojo.Role;
import com.zhouyue.server.service.IAdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author zhou
 * @since 2022-02-18
 */
@Service
public class AdminServiceImpl extends ServiceImpl<AdminMapper, Admin> implements IAdminService {
    @Autowired
    private AdminMapper adminMapper;
    @Autowired
    private RoleMapper roleMapper;
    @Autowired
    private AdminRoleMapper adminRoleMapper;

    /**
     * 根据用户名获取用户
     * @param username
     * @return
     */
    @Override
    public Admin getAdminByUserName(String username) {
        //使用 mybatis-plus 操作
        return adminMapper.selectOne(new QueryWrapper<Admin>().eq("username",username).eq("enabled",true));
    }

    /**
     * 根据用户 id 查询角色列表
     * @param adminId
     * @return
     */
    @Override
    public List<Role> getRoles(Integer adminId) {
        return roleMapper.getRoles(adminId);

    }

    /**
     * 获取所有操作员
     * @param keyWords
     * @return
     */
    @Override
    public List<Admin> getAllAdmins(String keyWords) {
        return adminMapper.getAllAdmins(((Admin)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId(),keyWords);

    }

    /**
     * 更新操作员角色
     * @param adminId
     * @param rids
     * @return
     */
    @Override
    @Transactional
    public RespBean updateAdminRoles(Integer adminId, Integer[] rids) {
        //先清空该操作员的所有角色信息
        adminRoleMapper.delete(new QueryWrapper<AdminRole>().eq("adminId", adminId));
        Integer result = adminRoleMapper.updateAdminRoles(adminId, rids);
        if (rids.length == result){
            return RespBean.success("更新成功");
        }
        return RespBean.error("更新失败");
    }

    /**
     * 更新用户密码
     * @param oldPass
     * @param pass
     * @param adminId
     * @return
     */
    @Override
    public RespBean updateAdminPassword(String oldPass, String pass, Integer adminId) {
        Admin admin = adminMapper.selectById(adminId);
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        //判断旧密码是否正确
        if (bCryptPasswordEncoder.matches(oldPass, admin.getPassword())){
            admin.setPassword(bCryptPasswordEncoder.encode(pass));
            int result = adminMapper.updateById(admin);
            if (result == 1){
                return RespBean.success("更新密码成功");
            }
        }
        return RespBean.error("更新失败");
    }
}
