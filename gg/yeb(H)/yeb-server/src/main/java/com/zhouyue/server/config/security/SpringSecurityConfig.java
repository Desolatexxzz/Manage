package com.zhouyue.server.config.security;

import com.zhouyue.server.service.impl.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 *  Security 配置类
 */
@Configuration
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private RestAuthorizationEntryPoint authorizationEntryPoint;
    @Autowired
    private RestfulAccessDeniedHandler accessDeniedHandler;
    @Autowired
    private UserDetailsServiceImpl userDetailsService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private CustomFilter customFilter;
    @Autowired CustomUrlDecisionManager customUrlDecisionManager;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        //配置 security 使用自定义的UserDetailsService，并且密码匹配用的 BCryptPasswordEncoder
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);
    }

    //SpringSecurity 的完整配置
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //由于使用的 jwt ，因此关闭 csrf
        http.csrf()
                .disable()
                //基于 token，不需要 session
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        //配置哪些请求允许访问，哪些请求需要认证
        http.authorizeRequests()
                //除了上面的，其他所有请求都要求认证
                .anyRequest()
                .authenticated()
                //动态权限配置，即将之前所写的 customUrlDecisionManager、customFilter 配置进 security
                .withObjectPostProcessor(new ObjectPostProcessor<FilterSecurityInterceptor>() {
                    @Override
                    public <O extends FilterSecurityInterceptor> O postProcess(O o) {
                        //设置认证管理器，将我们用于判断用户是否拥有访问某个 URL 的权限的管理器放入
                        o.setAccessDecisionManager(customUrlDecisionManager);
                        //设置认证数据源，这里就是可以访问某个 URL 的所有角色名
                        o.setSecurityMetadataSource(customFilter);
                        return o;
                    }
                });
        //禁用缓存
        http.headers().cacheControl();

        //添加 JWT 登录授权过滤器，请求会被 security 一层层拦截授权，UsernamePasswordAuthenticationFilter 是第一层过滤器
        //因此我们自定义的过滤器应在他的前面
        http.addFilterBefore(jwtAuthencationTokenFilter(), UsernamePasswordAuthenticationFilter.class);
        //自定义未授权或未登录的结果返回
        http.exceptionHandling()
                .accessDeniedHandler(accessDeniedHandler)
                .authenticationEntryPoint(authorizationEntryPoint);

    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers(
                "/login",
                "/logout",
                "/css/**",
                "/js/**",
                "/index.html",
                "favicon.ico",
                "/doc.html",
                "/webjars/**",
                "/swagger-resources/**",
                "/v2/api-docs/**",
                "/captcha"
        );
    }

    //自定义PasswordEncoder
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    //将自定义的过滤器注入 Spring
    @Bean
    public JwtAuthencationTokenFilter jwtAuthencationTokenFilter(){
        return new JwtAuthencationTokenFilter();
    }
}
