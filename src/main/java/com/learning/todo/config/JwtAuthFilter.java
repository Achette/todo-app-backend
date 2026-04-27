package com.learning.todo.config;

import com.learning.todo.services.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserService userService;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        // 1. Pega o header "Authorization" da requisição
        String authHeader = request.getHeader("Authorization");

        // 2. Se não tem header ou não começa com "Bearer ", ignora
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        // 3. Extrai o token (remove o "Bearer " do início)
        String token = authHeader.substring(7);
        String username = jwtUtil.extractUsername(token);

        // 4. Se tem username e o usuário ainda não foi autenticado nessa requisição
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            // 5. Carrega o usuário do banco
            UserDetails userDetails = userService.loadUserByUsername(username);

            // 6. Valida o token
            if (jwtUtil.isTokenValid(token, userDetails)) {

                // 7. Cria o objeto de autenticação e seta no contexto do Spring
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(
                                userDetails, null, userDetails.getAuthorities()
                        );
                authToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );
                SecurityContextHolder.getContext().setAuthentication(authToken);
                // ↑ A partir daqui, o Spring sabe quem está logado nessa requisição
            }
        }

        // 8. Continua a requisição para o próximo filtro/controller
        filterChain.doFilter(request, response);
    }
}