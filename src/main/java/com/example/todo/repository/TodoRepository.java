package com.example.todo.repository;

import java.util.List;
import java.util.Optional;

import com.example.todo.entity.Todo;

public interface TodoRepository {
  public List<Todo> findAll();

  public Optional<Todo> findById(Integer id);

  public void deleteById(Integer id);

  public Todo save(Todo todo);

  public Todo update(Todo todo);
}
