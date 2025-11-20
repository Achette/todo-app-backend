package com.learning.todo.services;

import com.learning.todo.dto.TaskDTO;
import com.learning.todo.entities.Task;
import com.learning.todo.entities.User;
import com.learning.todo.repositories.TaskRepository;
import com.learning.todo.repositories.UserRepository;
import com.learning.todo.services.exceptions.DatabaseException;
import com.learning.todo.services.exceptions.ResourceNotFoundException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class TaskService {

    @Autowired
    private TaskRepository repository;

    @Autowired
    private UserRepository userRepository;

    @Transactional(readOnly = true)
    public TaskDTO findById(Long id) {
        Optional<Task> result = repository.findById(id);

        Task task = result.orElseThrow(() -> new ResourceNotFoundException("Task não encontrada."));

        TaskDTO dto = new TaskDTO(task);

        return dto;
    }

    @Transactional(readOnly = true)
    public Page<TaskDTO> findAll(Pageable pageable) {
        Page<Task> result = repository.findAll(pageable);
        return result.map(task -> new TaskDTO((task)));
    }

    // Muito importante o relacionamento entre as entidades, no caso abaixo relacionamento um para muitos
    // um usuário pode ter várias tasks, mas uma task tem um usuário --> Muita task para um user [* - 1]
    @Transactional
    public TaskDTO insert(TaskDTO dto) {
        Task entity = new Task();
        entity.setTitle(dto.getTitle());
        entity.setDescription(dto.getDescription());
        entity.setCompleted(dto.getCompleted());
        entity.setCreatedAt(dto.getCreatedAt());
        entity.setPriority(dto.getPriority());

        User user = new User();
        user.setId(dto.getUserId());

        entity.setUser(user);

        entity = repository.save(entity);

        return new TaskDTO(entity);
    }

    @Transactional
    public TaskDTO update(Long id, TaskDTO dto) {
        try {
            Task entity = repository.getReferenceById(id);

            applyPartialUpdate(dto, entity);

            entity = repository.save(entity);

            return new TaskDTO(entity);
        } catch (EntityNotFoundException e) {
            throw new ResourceNotFoundException("Task não encontrada");
        }
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Task não encontrada.");
        }
        try {
            repository.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            throw new DatabaseException("Falha de integridade referencial");
        }
    }

    private void copyCreateDtoToEntity(TaskDTO dto, Task entity) {
        entity.setTitle(dto.getTitle());
        entity.setDescription(dto.getDescription());
        entity.setCompleted(dto.getCompleted());
        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("Usuário com ID " + dto.getUserId() + " não encontrado"));
        entity.setUser(user);
    }

    private void applyPartialUpdate(TaskDTO dto, Task entity) {
        if (dto.getTitle() != null) {
            entity.setTitle(dto.getTitle());
        }
        if (dto.getDescription() != null) {
            entity.setDescription(dto.getDescription());
        }
        if (dto.getCompleted() != null) {
            entity.setCompleted(dto.getCompleted());
        }

    }
}
