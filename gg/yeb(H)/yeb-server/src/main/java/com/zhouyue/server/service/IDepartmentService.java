package com.zhouyue.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zhouyue.server.pojo.Department;
import com.zhouyue.server.pojo.RespBean;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author zhou
 * @since 2022-02-18
 */
public interface IDepartmentService extends IService<Department> {

    /**
     * 获取所有部门
     * @return
     */
    List<Department> getAllDepartment();

    /**
     * 添加部门
     * @param department
     * @return
     */
    RespBean addDepartment(Department department);

    /**
     * 删除部门
     * @param id
     * @return
     */
    RespBean deleteDepartment(Integer id);
}
