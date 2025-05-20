package cn.edu.sdu.java.server.services;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class JwtService {

    @Value("${jwt.secret-key}")
    private String secretKey;

    // 从 Token 中提取用户名
    public String extractUsername(String token) {
        return decode(token).getSubject();
    }

    // 判断 Token 是否有效（签名有效且未过期）

    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }


    // 判断 Token 是否已过期
    private boolean isTokenExpired(String token) {
        Date expiration = decode(token).getExpiresAt();
        return expiration.before(new Date());
    }

    // 解码 JWT
    private DecodedJWT decode(String token) {
        return JWT.require(Algorithm.HMAC256(secretKey))
                .build()
                .verify(token);
    }

    // 生成 Token
// JwtService.java


    public String generateToken(UserDetails userDetails) {
        return generateToken(userDetails, 3600); // 默认 1 小时
    }

    public String generateToken(UserDetails userDetails, long expireTimeInSecond) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expireTimeInSecond * 1000L);

        return JWT.create()
                .withSubject(userDetails.getUsername())
                .withExpiresAt(expiryDate)
                .sign(Algorithm.HMAC256(secretKey));
    }



}