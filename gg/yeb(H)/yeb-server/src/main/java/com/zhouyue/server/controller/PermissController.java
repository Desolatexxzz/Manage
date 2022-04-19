package com.zhouyue.server.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zhouyue.server.pojo.Menu;
import com.zhouyue.server.pojo.MenuRole;
import com.zhouyue.server.pojo.RespBean;
import com.zhouyue.server.pojo.Role;
import com.zhouyue.server.service.IMenuRoleService;
import com.zhouyue.server.service.IMenuService;
import com.zhouyue.server.service.IRoleService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 权限组
 */
@RestController
@RequestMapping("/system/basic/permiss")
public class PermissController {
    @Autowired
    private IRoleService roleService;
    @Autowired
    private IMenuService menuService;
    @Autowired
    private IMenuRoleService menuRoleService;

    @ApiOperation(value = "获取所有角色")
    @GetMapping("/role")
    public List<Role> getAllRole(){
        return roleService.list();
    }
    @ApiOperation(value = "添加角色")
    @PostMapping("/role/addRole")
    public RespBean addRole(@RequestBody Role role){
        if (!role.getName().startsWith("ROLE_")){
            role.setName("ROLE_" + role.getName());
        }
        if (roleService.save(role)){
            return RespBean.success("添加成功");
        }
        return RespBean.error("添加失败");
    }
    @ApiOperation(value = "删除角色")
    @DeleteMapping("/role/deleteRole/{id}")
    public RespBean deleteRole(@PathVariable Integer id){
        if (roleService.removeById(id)){
            return RespBean.success("删除成功");
        }
        return RespBean.error("删除失败");
    }
    @ApiOperation(value = "查询所有菜单")
    @GetMapping("/menu")
    public List<Menu> getAllMenus(){
        return menuService.getAllMenus();
    }
    @ApiOperation(value = "根据角色id查询菜单id")
    @GetMapping("/menu/mid/{rid}")
    public List<Integer> getMidByRid(@PathVariable Integer rid){
        return menuRoleService.list(new QueryWrapper<MenuRole>().eq("rid",rid)).stream().map(MenuRole::getMid).collect(Collectors.toList());
    }
    @ApiOperation(value = "更新角色菜单")
    @PutMapping("/updateRoleMenu")
    public RespBean updateRoleMenu(Integer rid, Integer[] mids){
        return menuRoleService.updateRoleMenu(rid, mids);
    }
}
