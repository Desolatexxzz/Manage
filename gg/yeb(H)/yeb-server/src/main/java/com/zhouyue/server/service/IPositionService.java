package com.zhouyue.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zhouyue.server.pojo.Position;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author zhou
 * @since 2022-02-18
 */
public interface IPositionService extends IService<Position> {

    /**
     * 获取所有职位信息
     * @return
     */
    List<Position> getAllPosition();
}
