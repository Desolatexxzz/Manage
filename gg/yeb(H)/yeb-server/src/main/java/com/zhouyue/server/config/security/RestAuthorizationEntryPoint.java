package com.zhouyue.server.config.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zhouyue.server.pojo.RespBean;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * 当未登录或者token失效访问接口时，自定义的返回结果
 */
@Component
public class RestAuthorizationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        //设置编码格式
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        PrintWriter output = response.getWriter();
        //设置返回结果
        RespBean bean = RespBean.error("尚未登录, 请先登录");
        bean.setCode(401);
        output.write(new ObjectMapper().writeValueAsString(bean));
        output.flush();
        output.close();
    }
}
