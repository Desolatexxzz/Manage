package com.zhouyue.server.service;

import com.zhouyue.server.pojo.RespBean;

import javax.servlet.http.HttpServletRequest;

public interface LoginService {
    /**
     * 登录之后返回token
     * @param username
     * @param password
     * @param code
     * @param request
     * @return
     */
    RespBean login(String username, String password, String code, HttpServletRequest request);
}
