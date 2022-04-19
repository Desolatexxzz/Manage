package com.zhouyue.server.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhouyue.server.mapper.PositionMapper;
import com.zhouyue.server.pojo.Position;
import com.zhouyue.server.service.IPositionService;
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
public class PositionServiceImpl extends ServiceImpl<PositionMapper, Position> implements IPositionService {

    @Autowired
    private PositionMapper positionMapper;

    /**
     * 获取所有职位信息
     * @return
     */
    @Override
    public List<Position> getAllPosition() {
        List<Position> list = positionMapper.getAllPosition();
        return list;
    }
}
