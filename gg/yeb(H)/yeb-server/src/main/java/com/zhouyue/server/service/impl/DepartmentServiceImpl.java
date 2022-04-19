package com.zhouyue.server.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhouyue.server.mapper.DepartmentMapper;
import com.zhouyue.server.pojo.Department;
import com.zhouyue.server.pojo.RespBean;
import com.zhouyue.server.service.IDepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
public class DepartmentServiceImpl extends ServiceImpl<DepartmentMapper, Department> implements IDepartmentService {

    @Autowired
    private DepartmentMapper departmentMapper;
    /**
     * 获取所有部门
     * @return
     */
    @Override
    public List<Department> getAllDepartment() {
        return departmentMapper.getAllDepartment(-1);
    }

    /**
     * 添加部门
     * @param department
     * @return
     */
    @Override
    public RespBean addDepartment(Department department) {
        departmentMapper.addDepartment(department);
        //查询返回的结果会赋给这里的 department
        //查询成功，并将返回结果返回给前端
        if (department.getResult() == 1){
            return RespBean.success("添加成功", department);
        }
        return RespBean.error("添加失败");
    }

    /**
     * 删除部门
     * @param id
     * @return
     */
    @Override
    public RespBean deleteDepartment(Integer id) {
        //由于数据库的存储过程有一个 result 返回值，他的含义是数据库改变的行数，因此我们将 id 使用对象进行传入，那么 result 也会通过该对象进行返回
        Department department = new Department();
        department.setId(id);
        departmentMapper.deleteDepartment(department);
        if (department.getResult() == -2){
            return RespBean.error("该部门下还有子部门，删除失败");
        }
        if (department.getResult() == -1){
            return RespBean.error("该部门下还有员工，删除失败");
        }
        if (department.getResult() == 1){
            return RespBean.success("删除成功");
        }
        return RespBean.error("删除失败");
    }
}
