package com.zhouyue.server.controller;


import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import cn.afterturn.easypoi.excel.entity.enmus.ExcelType;
import com.zhouyue.server.pojo.*;
import com.zhouyue.server.service.*;
import io.swagger.annotations.ApiOperation;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.time.LocalDate;
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
@RequestMapping("/employee/basic")
public class EmployeeController {

    @Autowired
    private IEmployeeService employeeService;
    @Autowired
    private IPoliticsStatusService politicsStatusService;
    @Autowired
    private IJoblevelService joblevelService;
    @Autowired
    private INationService nationService;
    @Autowired
    private IPositionService positionService;
    @Autowired
    private IDepartmentService departmentService;

    @ApiOperation(value = "获取所有员工(分页)")
    @GetMapping("/")
    //三个参数的含义是：当前页号，每一页的数据量，传入的员工信息，入职日期（这里传入的是一个范围）
    public RespPageBean getEmployee(@RequestParam(defaultValue = "1") Integer currentPage,
                                    @RequestParam(defaultValue = "10") Integer size, Employee employee,
                                    LocalDate[] beginDateScope){
        return employeeService.getEmployeeByPage(currentPage,size,employee,beginDateScope);

    }

    @ApiOperation(value = "获取所有政治面貌")
    @GetMapping("/politicsstatus")
    public List<PoliticsStatus> getAllPoliticsStatus(){
        return politicsStatusService.list();
    }

    @ApiOperation(value = "获取所有职称")
    @GetMapping("/joblevels")
    public List<Joblevel> getAllJobLevels(){
        return joblevelService.list();
    }

    @ApiOperation(value = "获得所有民族")
    @GetMapping("/nations")
    public List<Nation> getAllNations(){
        return nationService.list();
    }

    @ApiOperation(value = "获得所有职位")
    @GetMapping("/positions")
    public List<Position> getAllPositions(){
        return positionService.list();
    }

    @ApiOperation(value = "获得所有部门")
    @GetMapping("/deps")
    public List<Department> getAllDepartments(){
        //由于存在子部门，因此不能使用 mybatis-plus，我们之前写过方法，直接使用即可
        return departmentService.getAllDepartment();
    }

    //查询当前最大工号，新增人员的工号就是最大工号加一
    @ApiOperation(value = "获取当前最大工号")
    @GetMapping("/maxWorkId")
    public RespBean getMaxWorkId(){
        return employeeService.getMaxWorkId();
    }

    @ApiOperation(value = "添加员工")
    @PostMapping("/addEmployee")
    public RespBean addEmployee(@RequestBody Employee employee){
        return employeeService.addEmployee(employee);
    }

    @ApiOperation(value = "更新员工")
    @PutMapping("/updateEmployee")
    public RespBean updateEmployee(@RequestBody Employee employee){
        if (employeeService.updateById(employee)){
            return RespBean.success("更新成功");
        }
        return RespBean.error("更新失败");
    }

    @ApiOperation(value = "删除员工")
    @DeleteMapping("/deleteEmployee/{id}")
    public RespBean deleteEmployee(@PathVariable Integer id){
        if (employeeService.removeById(id)){
            return RespBean.success("删除成功");
        }
        return RespBean.error("删除失败");
    }

    @ApiOperation(value = "导出员工数据")
    @GetMapping(value = "/export",produces = "application/octet-stream")
    public void exportEmployee(HttpServletResponse response){
        List<Employee> list = employeeService.getEmployee(null);
        ExportParams exportParams = new ExportParams("员工表","员工表", ExcelType.HSSF);
        Workbook workbook = ExcelExportUtil.exportExcel(exportParams, Employee.class, list);
        ServletOutputStream outputStream = null;
        try {
            //用流的形式传输
            response.setHeader("content-type", "application/octet-stream");
            //防止中文乱码
            response.setHeader("content-disposition","attachment;filename=" + URLEncoder.encode("员工表.xls","UTF-8"));
            outputStream = response.getOutputStream();
            workbook.write(outputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (outputStream != null){
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @ApiOperation(value = "导入员工数据")
    @PostMapping("/importEmployee")
    public RespBean importEmployee(MultipartFile file){
        ImportParams params = new ImportParams();
        //去除table的第一行，因为第一行是标题
        params.setTitleRows(1);
        //获取所有 nation
        List<Nation> nationList = nationService.list();
        List<PoliticsStatus> politicsStatusList = politicsStatusService.list();
        List<Department> departmentList = departmentService.list();
        List<Joblevel> joblevelList = joblevelService.list();
        List<Position> positionList = positionService.list();
        try {
            //导入文件
            List<Employee> list = ExcelImportUtil.importExcel(file.getInputStream(), Employee.class, params);
            for (Employee employee : list) {
                /**
                 * ①：使用有参构造，构造只有 name 赋值的对象
                 * ②：由于重写了 equals 和 hashcode 方法，因此我们可以使用 indexof 获取以 name 为判断，和上面创建的对象相同的那个对象在 list 中的下标
                 * ③：使用这个下标，获取 name 对应的完整的 nation 对象
                 * ④：得到 id 并赋给employee
                 */
                Integer naId = nationList.get(nationList.indexOf(new Nation(employee.getNation().getName()))).getId();
                employee.setNationId(naId);

                Integer polId = politicsStatusList.get(politicsStatusList.indexOf(new PoliticsStatus(employee.getPoliticsStatus().getName()))).getId();
                employee.setPoliticId(polId);

                Integer depId = departmentList.get(departmentList.indexOf(new Department(employee.getDepartment().getName()))).getId();
                employee.setDepartmentId(depId);

                Integer jobId = joblevelList.get(joblevelList.indexOf(new Joblevel(employee.getJoblevel().getName()))).getId();
                employee.setJobLevelId(jobId);

                Integer posId = positionList.get(positionList.indexOf(new Position(employee.getPosition().getName()))).getId();
                employee.setPoliticId(posId);

            }
            if (employeeService.saveBatch(list)){
                return RespBean.success("导入成功");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return RespBean.error("导入失败");
    }

}
