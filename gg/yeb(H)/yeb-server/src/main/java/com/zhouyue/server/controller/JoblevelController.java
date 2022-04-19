package com.zhouyue.server.controller;


import com.zhouyue.server.pojo.Joblevel;
import com.zhouyue.server.pojo.RespBean;
import com.zhouyue.server.service.IJoblevelService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Arrays;
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
@RequestMapping("/system/basic/joblevel")
public class JoblevelController {
    @Autowired
    private IJoblevelService joblevelService;

    @ApiOperation(value = "查询所有职称信息")
    @GetMapping("/")
    public List<Joblevel> getAllJoblevels(){
        return joblevelService.list();
    }
    @ApiOperation(value = "添加职称")
    @PostMapping("/addJoblevel")
    public RespBean addJoblevel(@RequestBody Joblevel joblevel){
        joblevel.setCreateDate(LocalDateTime.now());
        if (joblevelService.save(joblevel)) {
            return RespBean.success("添加职称成功");
        }
        return RespBean.error("添加职称失败");
    }
    @ApiOperation(value = "更新职称信息")
    @PutMapping("/updateJoblevel")
    public RespBean updateJoblevel(@RequestBody Joblevel joblevel){
        if (joblevelService.updateById(joblevel)){
            return RespBean.success("修改职称信息成功");
        }
        return RespBean.error("修改职称信息失败");
    }
    @ApiOperation(value = "删除职称")
    @DeleteMapping("/deleteJoblevel/{id}")
    public RespBean deleteJoblevel(@PathVariable Integer id){
        if (joblevelService.removeById(id)){
            return RespBean.success("删除成功");
        }
        return RespBean.error("删除失败");
    }
    @ApiOperation(value = "批量删除职称")
    @DeleteMapping("/deleteJoblevels")
    public RespBean deleteJoblevels(Integer[] ids){
        if (joblevelService.removeByIds(Arrays.asList(ids))) {
            return RespBean.success("批量删除成功");
        }
        return RespBean.error("批量删除失败");
    }


}
