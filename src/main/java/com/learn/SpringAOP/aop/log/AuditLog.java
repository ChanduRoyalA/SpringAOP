package com.learn.SpringAOP.aop.log;


import jakarta.servlet.http.HttpServletRequest;
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

@Component
@Aspect
public class AuditLog {

    @Pointcut("execution(* com.learn.SpringAOP.aop.service.MyUserDetailsService.loadUserByUsername(..))")
    public void logAuth() {
    }

    @Around("logAuth()")
    public Object LogMethod(ProceedingJoinPoint joinPoint) throws Throwable {
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = servletRequestAttributes.getRequest();
        String authType = request.getAuthType();
        String authHeader = request.getHeader("Authorization");
        System.out.println("User Login Request");
        System.out.println("Auth: ");
        if (authHeader != null && authHeader.startsWith("Basic ")) {
            String base64Credentials = authHeader.substring("Basic ".length());
            byte[] credDecoded = Base64.getDecoder().decode(base64Credentials);
            String credentials = new String(credDecoded, StandardCharsets.UTF_8);
            String[] values = credentials.split(":", 2); // username:password
            String usernameFromHeader = values[0];
            System.out.println("    Username from Header: " + usernameFromHeader);
        }
        Object res = joinPoint.proceed();
        UserDetails userDetails = ((UserDetails) res);
        String username = userDetails.getUsername();
        System.out.println("User: " + username + " Logged in");
        return res;
    }
}
