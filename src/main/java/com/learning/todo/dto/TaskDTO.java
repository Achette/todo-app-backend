package com.learning.todo.dto;

import com.learning.todo.entities.Task;
import com.learning.todo.entities.User;
import com.learning.todo.enums.TaskEnum;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.Instant;


public class TaskDTO {

    private Long id;

    @Size(min = 3, max = 60, message = "Titulo deve ter de 3 a 60 caracteres")
    private String title;

    @Size(min = 10, message = "Descrição precisa ter no mínimo 10 caracteres")
    private String description;

    private Boolean completed;

    private Instant createdAt;

    private Instant dueDate;

    private TaskEnum priority;

    @NotNull
    private Long userId;

    public TaskDTO() {
    }

    public TaskDTO(Task entity) {
        id = entity.getId();
        title = entity.getTitle();
        description = entity.getDescription();
        completed = entity.getCompleted();
        createdAt = entity.getCreatedAt();
        dueDate = entity.getDueDate();
        priority = entity.getPriority();
        userId = entity.getUser().getId();
    }

    public TaskDTO(Long id, String title, String description, Boolean completed, Instant createdAt, Instant dueDate, TaskEnum priority, Long userId) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.completed = completed;
        this.createdAt = createdAt;
        this.dueDate = dueDate;
        this.priority = priority;
        this.userId = userId;
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public Boolean getCompleted() {
        return completed;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getDueDate() { return dueDate; }

    public TaskEnum getPriority() {
        return priority;
    }

    public Long getUserId() {
        return userId;
    }
}
