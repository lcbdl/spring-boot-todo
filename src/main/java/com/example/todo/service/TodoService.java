package com.example.todo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.todo.controller.ResourceNotFoundException;
import com.example.todo.entity.Todo;
import com.example.todo.repository.TodoRepository;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Service
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TodoService {
  @Autowired
  private TodoRepository repository;

  public Todo findById(Long id) {
    return repository.findById(id).orElseThrow(() -> new ResourceNotFoundException());
  }

  public List<Todo> findAll() {
    return repository.findAll();
  }

  public Todo createTodo(Todo newTodo) {
    return repository.save(newTodo);
  }

  public Todo updateTodo(Long id, Todo todo) {
    Todo curTodo = this.repository.findById(id).orElseThrow(() -> new ResourceNotFoundException());
    curTodo.setTitle(todo.getTitle());
    curTodo.setDescription(todo.getDescription());
    curTodo.setCompleted(todo.getCompleted());
    return repository.save(curTodo);
  }

  public void deleteTodo(Long id) {
    repository.deleteById(id);
  }

}
