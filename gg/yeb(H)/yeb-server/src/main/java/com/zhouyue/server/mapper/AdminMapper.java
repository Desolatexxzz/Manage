package com.zhouyue.server.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zhouyue.server.pojo.Admin;
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
public interface AdminMapper extends BaseMapper<Admin> {

    /**
     * 获取所有操作员
     * @param id
     * @param keyWords
     * @return
     */
    List<Admin> getAllAdmins(@Param("id") Integer id, @Param("keyWords") String keyWords);
}
