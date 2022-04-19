package com.zhouyue.server.config.security;

import com.zhouyue.server.service.impl.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * JWT 登录授权过滤器
 */
public class JwtAuthencationTokenFilter extends OncePerRequestFilter {
    @Value("${jwt.tokenHeader}")
    private String tokenHeader;
    @Value("${jwt.tokenHead}")
    private String tokenHead;
    @Autowired
    private JwtTokenUtils jwtTokenUtils;
    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        //根据 tokenHeader 从请求的请求头中拿到 value
        String authHeader = request.getHeader(tokenHeader);
        //判断是否存在 token，并且以 tokenHead (Bearer) 作为开头
        if (authHeader != null && authHeader.startsWith(tokenHead)){
            //将 tokenHead 截掉，获得token
            String authToken = authHeader.substring(tokenHead.length());
            //从token中获取用户名
            String username = jwtTokenUtils.getUserNameFromToken(authToken);
            //token存在用户名
            if (username != null){
                //从数据库中取出和 token 中用户名一样的用户信息
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                //验证token是否有效(即判断token是否过期，以及 userDetails 中的 username 和 token 中的 username 是否一致)
                if (jwtTokenUtils.validateToken(authToken,userDetails)){
                    //将用户信息存入 security 全文中
                    UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                }
            }
        }
        //放行
        filterChain.doFilter(request,response);
    }
}
