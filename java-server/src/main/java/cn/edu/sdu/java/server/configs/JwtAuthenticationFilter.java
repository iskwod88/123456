package cn.edu.sdu.java.server.configs;

import cn.edu.sdu.java.server.payload.response.DataResponse;
import cn.edu.sdu.java.server.services.JwtService;
import cn.edu.sdu.java.server.services.UserDetailsServiceImpl;
import cn.edu.sdu.java.server.util.DateTimeTool;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.security.web.context.RequestAttributeSecurityContextRepository;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;
import java.util.Date;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final HandlerExceptionResolver handlerExceptionResolver;

    public final JwtService jwtService;
    public final UserDetailsServiceImpl userDetailsService;


    /**
     * 提供给 AOP 切面调用的 Token 验证方法
     */
    public Object verifyToken(ProceedingJoinPoint joinPoint) throws Throwable {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String authHeader = request.getHeader("Authorization");
        String username;

        try {
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return DataResponse.getReturnMessageError("缺少有效的 Token");
            }

            String jwt = authHeader.substring(7);
            username = jwtService.extractUsername(jwt);

            if (username == null) {
                return DataResponse.getReturnMessageError("无效的 Token");
            }

            if (SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                if (jwtService.isTokenValid(jwt, userDetails)) {
                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.getAuthorities()
                    );
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                } else {
                    return DataResponse.getReturnMessageError("Token 已失效");
                }
            }
        } catch (Exception e) {
            return DataResponse.getReturnMessageError("权限验证失败: " + e.getMessage());
        }

        // 继续执行目标方法
        return joinPoint.proceed();
    }


    @Autowired
    private RequestAttributeSecurityContextRepository repo;

    public JwtAuthenticationFilter(
        JwtService jwtService,
        UserDetailsServiceImpl userDetailsService,
        HandlerExceptionResolver handlerExceptionResolver
    ) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
        this.handlerExceptionResolver = handlerExceptionResolver;
    }

    @Override
    protected void doFilterInternal(
        @NonNull HttpServletRequest request,
        @NonNull HttpServletResponse response,
        @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        String username = "";
        String url = request.getRequestURI();
        Date startDate = new Date();
        final String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            SecurityContext context = SecurityContextHolder.getContext();
            repo.saveContext(context, request, response);
            filterChain.doFilter(request, response);
            return;
        }

        try {
            final String jwt = authHeader.substring(7);
            username = jwtService.extractUsername(jwt);

            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            if (username != null && authentication == null) {
                UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);

                if (jwtService.isTokenValid(jwt, userDetails)) {
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.getAuthorities()
                    );

                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }
            SecurityContext context = SecurityContextHolder.getContext();
            repo.saveContext(context, request, response);
            filterChain.doFilter(request, response);
            Date endDate = new Date();
            double requestTime = (int) (endDate.getTime() - startDate.getTime())/1000.;
            String startTime = DateTimeTool.parseDateTime(startDate);
            logger.info(url + "," +username+"," + startTime+ "," + requestTime);
        } catch (Exception exception) {
            handlerExceptionResolver.resolveException(request, response, null, exception);
        }
    }
}
