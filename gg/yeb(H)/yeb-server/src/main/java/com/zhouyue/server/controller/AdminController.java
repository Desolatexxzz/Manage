package com.zhouyue.server.controller;


import com.zhouyue.server.pojo.Admin;
import com.zhouyue.server.pojo.RespBean;
import com.zhouyue.server.pojo.Role;
import com.zhouyue.server.service.IAdminService;
import com.zhouyue.server.service.IRoleService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author zhou
 * @since 2022-02-18
 */
@RestController
@RequestMapping("/system/admin")
public class AdminController {

    @Autowired
    private IAdminService adminService;
    @Autowired
    private IRoleService roleService;

    @ApiOperation(value = "获取所有操作员")
    @GetMapping("/")
    public List<Admin> getAllAdmins(String keyWords){
        return adminService.getAllAdmins(keyWords);
    }

    @ApiOperation(value = "更新操作员信息")
    @PutMapping("/updateAdmin")
    public RespBean updateAdmin(@RequestBody Admin admin){
        if (adminService.updateById(admin)){
            return RespBean.success("更新成功");
        }
        return RespBean.error("更新失败");
    }

    @ApiOperation(value = "删除操作员")
    @DeleteMapping("/deleteAdmin/{id}")
    public RespBean deleteAdmin(@PathVariable Integer id){
        if (adminService.removeById(id)){
            return RespBean.success("删除成功");
        }
        return RespBean.error("删除失败");
    }

    @ApiOperation(value = "获取所有角色")
    @GetMapping("/roles")
    public List<Role> getAllRoles(){
        return roleService.list();
    }

    @ApiOperation(value = "更新操作员角色")
    @PutMapping("/updateRoles")
    public RespBean updateAdminRoles(Integer adminId, Integer[] rids){
        return adminService.updateAdminRoles(adminId,rids);
    }
}
