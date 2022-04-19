package com.zhouyue.server.exception;

import com.zhouyue.server.pojo.RespBean;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;

/**
 * 全局异常处理
 */

//Controller增强类，可以对在 controller 层出现的，并且符合 ExceptionHandler 中定义的异常进行拦截
@RestControllerAdvice
public class GlobalException {
    //所有关于 sql 语句的异常
    @ExceptionHandler(SQLException.class)
    public RespBean mySqlException(SQLException e){
        //可以具体到某个异常来处理，这里用这个异常来演示
        if (e instanceof SQLIntegrityConstraintViolationException){
            return RespBean.error("该数据有关联数据，操作失败");
        }
        return RespBean.error("数据库异常, 操作失败");
    }

}
