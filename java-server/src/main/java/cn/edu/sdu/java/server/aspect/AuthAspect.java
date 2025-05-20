package cn.edu.sdu.java.server.aspect;

import cn.edu.sdu.java.server.configs.JwtAuthenticationFilter;
import cn.edu.sdu.java.server.payload.response.DataResponse;
import cn.edu.sdu.java.server.services.UserDetailsServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Objects;

@Aspect
@Component
public class AuthAspect {

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    /**
     * 环绕通知：拦截带有 @Auth 注解的方法
     */
    @Around("@annotation(cn.edu.sdu.java.server.annotation.Auth)")
    public Object verifyToken(ProceedingJoinPoint joinPoint) throws Throwable {
        try {
            // 获取当前请求对象
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            HttpServletRequest request = Objects.requireNonNull(attributes).getRequest();

            // 从请求头中提取 JWT Token（与 JwtAuthenticationFilter 保持一致）
            String authHeader = request.getHeader("Authorization");
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return buildErrorResponse("缺少有效的 Token");
            }

            String token = authHeader.substring(7); // 去除 Bearer 前缀

            // 解析用户名
            String username = jwtAuthenticationFilter.jwtService.extractUsername(token);
            if (username == null) {
                return buildErrorResponse("Token 不合法");
            }

            // 检查用户是否已认证（避免重复认证）
            if (jwtAuthenticationFilter.jwtService.isTokenValid(token, jwtAuthenticationFilter.userDetailsService.loadUserByUsername(username))) {
                request.setAttribute("username", username);
            } else {
                return buildErrorResponse("Token 已失效");
            }


        } catch (Exception e) {
            return buildErrorResponse("权限验证失败: " + e.getMessage());
        }

        // 继续执行目标方法
        return joinPoint.proceed();
    }

    /**
     * 构建统一错误响应
     */
    private Object buildErrorResponse(String message) {
        return DataResponse.getReturnMessageError(message);
    }
}
