package com.zhouyue.server.controller;


import com.zhouyue.server.pojo.Department;
import com.zhouyue.server.pojo.RespBean;
import com.zhouyue.server.service.IDepartmentService;
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
@RequestMapping("/system/basic/department")
public class DepartmentController {
    @Autowired
    private IDepartmentService departmentService;

    @ApiOperation(value = "获取所有部门")
    @GetMapping("/")
    public List<Department> getAllDepartment(){
        return departmentService.getAllDepartment();
    }

    @ApiOperation(value = "添加部门")
    @PostMapping("/addDepartment")
    public RespBean addDepartment(@RequestBody Department department){
        department.setEnabled(true);
        return departmentService.addDepartment(department);
    }

    @ApiOperation(value = "删除部门")
    @DeleteMapping("/deleteDepartment/{id}")
    public RespBean deleteDepartment(@PathVariable Integer id){
        return departmentService.deleteDepartment(id);
    }

}
