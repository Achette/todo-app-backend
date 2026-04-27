package com.learning.todo.repositories;

import com.learning.todo.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    // Spring Data JPA gera a query automaticamente pelo nome do método.
    // findBy + Username → SELECT * FROM tb_user WHERE username = ?
    User findByUsername(String username);
}