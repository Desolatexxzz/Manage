package com.zhouyue.server.config.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zhouyue.server.pojo.RespBean;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * 访问接口没有权限时，自定义的返回结果
 */
@Component
public class RestfulAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        //设置编码格式
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        PrintWriter output = response.getWriter();
        //设置返回结果
        RespBean bean = RespBean.error("权限不足，请联系管理员");
        bean.setCode(403);
        output.write(new ObjectMapper().writeValueAsString(bean));
        output.flush();
        output.close();
    }
}
