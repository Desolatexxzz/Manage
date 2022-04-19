package com.zhouyue.server.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zhouyue.server.pojo.Position;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author zhou
 * @since 2022-02-18
 */
public interface PositionMapper extends BaseMapper<Position> {

    /**
     * 获取所有职位信息
     * @return
     */
    List<Position> getAllPosition();
}
