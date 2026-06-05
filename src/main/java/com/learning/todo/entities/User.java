package com.learning.todo.entities;

import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.Instant;
import java.util.*;

@Entity
@Table(name = "tb_user")
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(unique = true, nullable = false)
    private String username;

    private String password;

    @Column(columnDefinition = "TIMESTAMP WITHOUT TIME ZONE")
    private Instant created_at;

    @OneToMany(mappedBy = "user")
    private List<Task> tasks = new ArrayList<>();

    // Roles: ManyToMany pois um usuário pode ter vários papéis
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "tb_user_role",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private final Set<Role> roles = new HashSet<>();

    public User() {
    }

    public User(Long id,String name, String username, String password, Instant created_at, List<Task> tasks) {
        this.id = id;
        this.name = name;
        this.username = username;
        this.password = password;
        this.created_at = created_at;
        this.tasks = tasks;
    }

    // ─── Métodos obrigatórios de UserDetails ───────────────

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles; // Role já implementa GrantedAuthority
    }

    // Esses 4 métodos controlam se a conta está ativa.
    // Por enquanto retornem true (conta sempre ativa).
    @Override public boolean isAccountNonExpired() { return true; }
    @Override public boolean isAccountNonLocked() { return true; }
    @Override public boolean isCredentialsNonExpired() { return true; }
    @Override public boolean isEnabled() { return true; }

    // ─── Método auxiliar (igual ao naconsulta) ──────────────
    public boolean hasRole(String roleName) {
        return roles.stream().anyMatch(r -> r.getAuthority().equals(roleName));
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Instant getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Instant created_at) {
        this.created_at = created_at;
    }

    public List<Task> getTasks() {
        return tasks;
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }

    public Set<Role> getRoles() {
        return roles;
    }
}
