package com.zhouyue.server.controller;


import com.zhouyue.server.pojo.Position;
import com.zhouyue.server.pojo.RespBean;
import com.zhouyue.server.service.IPositionService;
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
@RequestMapping("/system/basic/pos")
public class PositionController {

    @Autowired
    private IPositionService positionService;

    @ApiOperation(value = "获取所有职位信息")
    @GetMapping("/")
    public List<Position> getAllPosition(){
        List<Position> list = positionService.getAllPosition();
        return list;
    }

    @ApiOperation(value = "添加职位信息")
    @PostMapping("/addPosition")
    public RespBean addPosition(@RequestBody Position position){
        position.setCreateDate(LocalDateTime.now());
        if (positionService.save(position)){
            return RespBean.success("添加成功");
        }
        return RespBean.error("添加失败");
    }

    @ApiOperation(value = "更新职位信息")
    @PutMapping("/updatePosition")
    public RespBean updatePosition(@RequestBody Position position){
        if (positionService.updateById(position)){
            return RespBean.success("更新成功");
        }
        return RespBean.error("更新失败");
    }

    /**
     * 按钮删除
     * @param id
     * @return
     */
    @ApiOperation(value = "删除职位信息")
    @DeleteMapping("/deletePosition/{id}")
    public RespBean deletePosition(@PathVariable Integer id){
        if (positionService.removeById(id)){
            return RespBean.success("删除成功");
        }
        return RespBean.error("删除失败");
    }

    /**
     * 批量删除
     * @param ids
     * @return
     */
    @ApiOperation(value = "批量删除所有信息")
    @DeleteMapping("/deletePositions")
    public RespBean deletePositions(Integer[] ids){
        if (positionService.removeByIds(Arrays.asList(ids))){
            return RespBean.success("批量删除成功");
        }
        return RespBean.error("批量删除失败");
    }
}
