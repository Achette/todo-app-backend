package com.learning.todo.dto;

import java.util.List;

public record LoginResponseDTO(String token, String username, List<String> roles, String name) {
}
