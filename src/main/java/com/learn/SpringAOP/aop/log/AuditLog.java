package com.learn.SpringAOP.aop.log;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Optional;

@Component
@Aspect
public class AuditLog {

    @Pointcut("execution(* com.learn.SpringAOP.aop.service.MyUserDetailsService.loadUserByUsername(..))")
    public void logAuth() {
    }

    @Around("logAuth()")
    public Object LogMethod(ProceedingJoinPoint joinPoint) throws Throwable {
        logAuthRequest();
        Object res = null;
        String resInfo;
        try {
            res = joinPoint.proceed();
            UserDetails userDetails = ((UserDetails) res);
            String username = userDetails.getUsername();
            resInfo = "User (" + username + ") Logged In";
        } catch (Exception e) {
            resInfo = "Response: " + e.getMessage();
        }
        logAuthResponse(resInfo);
        return res;
    }

    private void logAuthResponse(String resInfo) {
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (servletRequestAttributes != null) {
            HttpServletResponse response = servletRequestAttributes.getResponse();
            System.out.println(resInfo);
            return;
        }
        System.out.println(Optional.ofNullable(null));
    }

    private void logAuthRequest() {
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (servletRequestAttributes != null) {
            HttpServletRequest request = servletRequestAttributes.getRequest();
            String authHeader = request.getHeader("Authorization");
            String reqUrl = (request.getRequestURL()).toString();
            String userNameFromRequest = null;
            if (authHeader != null && authHeader.startsWith("Basic ")) {
                String base64Credentials = authHeader.substring("Basic ".length());
                byte[] credDecoded = Base64.getDecoder().decode(base64Credentials);
                String credentials = new String(credDecoded, StandardCharsets.UTF_8);
                String[] values = credentials.split(":", 2); // username:password
                userNameFromRequest = values[0];
            }

            String reqInfo = """
                    User Login Request -> %s, User Name in the Request -> %s
                    """.formatted(reqUrl, userNameFromRequest);

            System.out.println(reqInfo);
        }

    }
}
