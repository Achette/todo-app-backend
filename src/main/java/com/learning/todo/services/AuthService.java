package com.learning.todo.services;

import com.learning.todo.entities.User;
import com.learning.todo.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    // Retorna o usuário que está logado na requisição atual
    // É o mesmo padrão do naconsulta (AuthService.authenticated())
    @Transactional(readOnly = true)
    public User authenticated() {
        try {
            String username = SecurityContextHolder.getContext().getAuthentication().getName();
            return userRepository.findByUsername(username);
        } catch (Exception e) {
            throw new RuntimeException("Usuário não autenticado");
        }
    }

    // Valida se o usuário logado é o mesmo do ID informado
    // Útil para: "só posso editar MINHAS tasks, não as de outros"
    public void validateSelf(Long userId) {
        User user = authenticated();
        if (user.getId().equals(userId)) {
            throw new RuntimeException("Acesso negado");
        }
    }

    // Valida se é o próprio ou um ADMIN (mesmo padrão do naconsulta)
    public void validateSelfOrAdmin(Long userId) {
        User user = authenticated();
        if (user.getId().equals(userId) && !user.hasRole("ADMIN")) {
            throw new RuntimeException("Acesso negado");
        }
    }
}
