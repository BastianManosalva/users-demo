package com.example.usersdemo.security;

import java.io.IOException;
import java.util.Collections;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import com.example.usersdemo.exception.ServiceException;
import com.example.usersdemo.utils.JwtUtil;

public class JwtFilter extends OncePerRequestFilter {

    @Autowired
    private HandlerExceptionResolver handlerExceptionResolver;

    private final JwtUtil jwtUtil;

    public JwtFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String path = request.getRequestURI();

        if (path.startsWith("/h2-console") ||
                (path.equals("/api/v1/users") && request.getMethod().equals("POST")) ||
                path.equals("/api/v1/auth/login") ||
                path.startsWith("/swagger-ui") ||
                path.startsWith("/v2/api-docs") ||
                path.startsWith("/swagger-resources") ||
                path.startsWith("/webjars")) {
            filterChain.doFilter(request, response);
            return;
        }

        String authHeader = request.getHeader("Authorization");

        try {

            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                response.setContentType("application/json");
                response.getWriter().write("{\"code\":401,\"message\":\"Missing or invalid Authorization header\"}");
                return;
            }

            String token = authHeader.substring(7);
            boolean valid = jwtUtil.validateToken(token);

            if (!valid) {

                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                response.setContentType("application/json");
                response.getWriter().write("{\"code\":401,\"message\":\"Invalid or expired JWT token\"}");
                return;
            }

            String email = jwtUtil.getEmailFromToken(token);
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(email, null,
                    Collections.emptyList());
            SecurityContextHolder.getContext().setAuthentication(authentication);

            filterChain.doFilter(request, response);
        } catch (ServiceException ex) {
            handlerExceptionResolver.resolveException(request, response, null, ex);
        }
    }
}
