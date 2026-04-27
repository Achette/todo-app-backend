package com.learning.todo.services;

import com.learning.todo.entities.User;
import com.learning.todo.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Service
public class UserService implements UserDetailsService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    // Ao criar o construtor, o Spring entende:
    // "Para criar o UserService, eu PRECISO buscar o encoder que está lá no SecurityConfig"
    public UserService(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // ─── Registrar novo usuário ──────────────────────────────
    @Transactional
    public User register(String username, String password) {
        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));  // NUNCA salvar senha em texto puro!
        user.setCreated_at(Instant.now());
        return userRepository.save(user);
    }

    // ─── Obrigatório do UserDetailsService ──────────────────
    // O Spring Security chama esse método quando alguém tenta fazer login.
    // Você busca o usuário pelo username e retorna. O Security cuida do resto.
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);

        if (user == null) {
            logger.error("User not found with username: " + username);
            throw new UsernameNotFoundException("User not found with username: " + username);
        }

        logger.info("Found user: " + user.toString());
        return user;
    }


}
