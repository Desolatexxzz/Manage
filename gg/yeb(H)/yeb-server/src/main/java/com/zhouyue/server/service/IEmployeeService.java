package com.zhouyue.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zhouyue.server.pojo.Employee;
import com.zhouyue.server.pojo.RespBean;
import com.zhouyue.server.pojo.RespPageBean;

import java.time.LocalDate;
import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author zhou
 * @since 2022-02-18
 */
public interface IEmployeeService extends IService<Employee> {

    /**
     * 获取所有员工(分页)
     * @param currentPage
     * @param size
     * @param employee
     * @param beginDateScope
     * @return
     */
    RespPageBean getEmployeeByPage(Integer currentPage, Integer size, Employee employee, LocalDate[] beginDateScope);

    /**
     * 获取当前最大工号
     * @return
     */
    RespBean getMaxWorkId();

    /**
     * 添加员工
     * @param employee
     * @return
     */
    RespBean addEmployee(Employee employee);

    /**
     * 查询员工,传了id就根据id查，不传就表示查询所有
     * @param id
     */
    List<Employee> getEmployee(Integer id);

    /**
     * 获取所有员工的工资账套
     * @param currentPage
     * @param size
     * @return
     */
    RespPageBean getEmployeeWithSalary(Integer currentPage, Integer size);
}
