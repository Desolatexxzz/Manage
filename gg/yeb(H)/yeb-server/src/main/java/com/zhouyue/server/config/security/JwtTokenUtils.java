package com.zhouyue.server.config.security;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtTokenUtils {
    //设置用户名
    private static final String CLAIM_KEY_USERNAME = "sub";
    //设置创建时间
    private static final String CLAIM_KEY_CREATED = "created";
    //从配置中获取 JWT 的密钥和失效时间
    @Value("${jwt.secret}")
    private String secret;
    @Value("${jwt.expiration}")
    private Long expiration;

    /**
     * 根据用户信息生成token
     * @param userDetails
     * @return
     */
    public String generateToken(UserDetails userDetails){
        //创建一个负载 (Payload)
        Map<String, Object> claims = new HashMap<>();
        //将用户名赋值为 UserDetails 中保存的用户名，并存入负载
        claims.put(CLAIM_KEY_USERNAME, userDetails.getUsername());
        //将创建时间赋值为当前时间，并存入负载
        claims.put(CLAIM_KEY_CREATED, new Date());
        return generateToken(claims);
    }

    /**
     * 从token中取出用户名
     * @param token
     * @return
     */
    public String getUserNameFromToken(String token){
        String username;
        try{
            Claims claims = getClaimFromToken(token);
            username = claims.getSubject();
        }catch (Exception e){
            username = null;
        }
        return username;
    }

    /**
     * 判断 token 是否可以被刷新，过期就可以被刷新
     * @param token
     * @return
     */
    public boolean canRefresh(String token){
        return !isTokenExpired(token);
    }

    /**
     * 刷新token
     * @param token
     * @return
     */
    public String refreshToken(String token){
        Claims claims = getClaimFromToken(token);
        claims.put(CLAIM_KEY_CREATED, new Date());
        return generateToken(claims);
    }

    /**
     * 判断 token 是否有效，①：token是否过期；②：token 的用户名是否和 userDetails 中的用户名一致
     * @param token
     * @param userDetails
     * @return
     */
    public boolean validateToken(String token, UserDetails userDetails){
        String username = getUserNameFromToken(token);
        return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }

    /**
     * 判断token是否过期
     * @param token
     * @return
     */
    private boolean isTokenExpired(String token) {
        //获取失效时间
        Claims claims = getClaimFromToken(token);
        Date expiration = claims.getExpiration();
        //判断失效时间是否在当前时间的前面，如果在前面，那么 token 就过期了
        return expiration.before(new Date());
    }


    /**
     * 从 token 中获取负载
     * @param token
     * @return
     */
    private Claims getClaimFromToken(String token) {
        Claims claims = null;
        try{
            claims = Jwts.parser()
                    .setSigningKey(secret)
                    .parseClaimsJws(token)
                    .getBody();
        }catch (Exception e){
            e.printStackTrace();
        }
        return claims;
    }

    /**
     * 根据负载生成 JWT TOKEN
     * @param claims
     * @return
     */
    private String generateToken(Map<String, Object> claims){
        return Jwts.builder()
                .setClaims(claims)
                .setExpiration(generateExpirationDate())
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }

    /**
     * 生成token失效时间，用当前时间加上配置的失效时间
     * @return
     */
    private Date generateExpirationDate() {
        return new Date(System.currentTimeMillis() + expiration * 1000);

    }

}
