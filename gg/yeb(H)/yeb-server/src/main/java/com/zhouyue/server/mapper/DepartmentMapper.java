package com.zhouyue.server.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zhouyue.server.pojo.Department;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author zhou
 * @since 2022-02-18
 */
public interface DepartmentMapper extends BaseMapper<Department> {

    /**
     * 获取所有部门
     * @return
     */
    List<Department> getAllDepartment(@Param("parentId") Integer id);

    /**
     * 添加部门
     * @param department
     */
    void addDepartment(Department department);

    /**
     * 删除部门
     * @param department
     */
    void deleteDepartment(Department department);
}
