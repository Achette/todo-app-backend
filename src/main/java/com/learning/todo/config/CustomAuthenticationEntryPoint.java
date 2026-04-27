package com.learning.todo.config;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.Instant;

@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException)
            throws IOException, ServletException {

        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        String json = String.format(
                "{\"timestamp\": \"%s\", \"status\": %d, \"error\": \"Não autorizado\", \"message\": \"Você precisa estar logado para acessar este recurso.\", \"path\": \"%s\"}",
                Instant.now(), HttpServletResponse.SC_UNAUTHORIZED, request.getRequestURI()
        );

        response.getWriter().write(json);
    }
}