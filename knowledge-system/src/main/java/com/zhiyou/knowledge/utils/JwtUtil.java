package com.zhiyou.knowledge.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtUtil {
    @Value("${jwt.secret:knowledge-ai-2026}")
    private String secret;

    @Value("${jwt.expire:86400}")
    private long expire;

    // 生成密钥（新版必须用SecretKey）
    private SecretKey getSecretKey() {
        // 密钥长度至少256位，不足则补全
        byte[] keyBytes = secret.getBytes();
        if (keyBytes.length < 32) {
            byte[] newKey = new byte[32];
            System.arraycopy(keyBytes, 0, newKey, 0, keyBytes.length);
            keyBytes = newKey;
        }
        return Keys.hmacShaKeyFor(keyBytes);
    }

    /**
     * 生成token
     */
    public String generateToken(Long userId) {
        Date now = new Date();
        Date expireDate = new Date(now.getTime() + expire * 1000);
        return Jwts.builder()
                .setSubject(userId.toString())
                .setIssuedAt(now)
                .setExpiration(expireDate)
                .signWith(getSecretKey()) // 新版签名方式
                .compact();
    }

    /**
     * 解析token获取用户ID
     */
    public Long getUserIdFromToken(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(getSecretKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
        return Long.parseLong(claims.getSubject());
    }

    /**
     * 验证token
     */
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(getSecretKey())
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}