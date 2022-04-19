package com.zhouyue.server.service.impl;

import com.zhouyue.server.pojo.Admin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private AdminServiceImpl adminService;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Admin admin = adminService.getAdminByUserName(username);
        if (admin != null){
            admin.setRoles(adminService.getRoles(admin.getId()));
            return admin;
        }
        throw new UsernameNotFoundException("用户名错误");
    }
}
