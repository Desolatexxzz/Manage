package com.zhouyue.server.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 公共返回对象
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RespBean {
    //状态码
    private long code;
    //提示信息
    private String message;
    //一个对象
    private Object object;

    /**
     * 成功返回结果
     * @param message
     * @return
     */
    public static RespBean success(String message){
        return new RespBean(200, message, null);
    }

    /**
     * 成功返回结果
     * @param message
     * @param object
     * @return
     */
    public static RespBean success(String message, Object object){
        return new RespBean(200, message, object);
    }

    /**
     * 失败返回结果
     * @param message
     * @return
     */
    public static RespBean error(String message){
        return new RespBean(500, message, null);
    }

    /**
     * 失败返回结果
     * @param message
     * @param object
     * @return
     */
    public static RespBean error(String message, Object object){
        return new RespBean(500, message, object);
    }
}
