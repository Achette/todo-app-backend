package com.learning.todo.config;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.Instant;

@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException)
            throws IOException, ServletException {

        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);

        String json = String.format(
                "{\"timestamp\": \"%s\", \"status\": %d, \"error\": \"Acesso Negado\", \"message\": \"Você não tem permissão para acessar este recurso.\", \"path\": \"%s\"}",
                Instant.now(), HttpServletResponse.SC_FORBIDDEN, request.getRequestURI()
        );

        response.getWriter().write(json);
    }
}