package com.learning.todo.controllers;

import com.learning.todo.config.JwtUtil;
import com.learning.todo.dto.LoginRequestDTO;
import com.learning.todo.dto.LoginResponseDTO;
import com.learning.todo.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authManager;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserService userService;

    // POST /auth/login → retorna o token JWT
    // Recebemos um REQUEST DTO
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginRequestDTO dto) {
        // 1. Autentica: o AuthenticationManager usa o DaoAuthenticationProvider
        //    que chama loadUserByUsername + verifica BCrypt automaticamente
        Authentication authentication = authManager.authenticate(new UsernamePasswordAuthenticationToken(dto.username(), dto.password()));

        // 2. Pega o UserDetails do resultado da autenticação
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        // 3. Gera o token JWT
        String token = jwtUtil.generateToken(userDetails);

        // 4. Extrai as authorities e converte para uma lista de strings
        List<String> roles = userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList();

        // 5. Retorna o token para o cliente --> Devolvemos um RESPONSE DTO (O token vai para o campo 'token')
        return ResponseEntity.ok(new LoginResponseDTO(token, userDetails.getUsername(), roles));

    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody LoginRequestDTO dto) {
        userService.register(dto.username(), dto.password());
        return ResponseEntity.ok("Usuário criado com sucesso!");
    }
}
