package com.example.todo.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.example.todo.controller.ResourceNotFoundException;
import com.example.todo.entity.Todo;
import com.example.todo.repository.TodoRepository;

public class TodoServiceTest {

  @InjectMocks
  private TodoService todoService;

  @Mock
  private TodoRepository todoRepository;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void testFindById_Success() {
    Todo todo = new Todo();
    todo.setId(1L);
    when(todoRepository.findById(1L)).thenReturn(Optional.of(todo));

    Todo result = todoService.findById(1L);

    assertNotNull(result);
    assertEquals(1L, result.getId());
  }

  @Test
  void testFindById_NotFound() {
    when(todoRepository.findById(1L)).thenReturn(Optional.empty());

    assertThrows(ResourceNotFoundException.class, () -> {
      todoService.findById(1L);
    });
  }

  @Test
  void testFindAll() {
    Todo todo1 = new Todo();
    Todo todo2 = new Todo();
    List<Todo> todos = Arrays.asList(todo1, todo2);
    when(todoRepository.findAll()).thenReturn(todos);

    List<Todo> result = todoService.findAll();

    assertNotNull(result);
    assertEquals(2, result.size());
  }

  @Test
  void testCreateTodo() {
    Todo todo = new Todo();
    when(todoRepository.save(todo)).thenReturn(todo);

    Todo result = todoService.createTodo(todo);

    assertNotNull(result);
    verify(todoRepository, times(1)).save(todo);
  }

  @Test
  void testUpdateTodo_Success() {
    Todo existingTodo = new Todo();
    existingTodo.setId(1L);
    Todo updatedTodo = new Todo();
    updatedTodo.setTitle("Updated Title");
    updatedTodo.setDescription("Updated Description");
    updatedTodo.setCompleted(true);

    when(todoRepository.findById(1L)).thenReturn(Optional.of(existingTodo));
    when(todoRepository.save(existingTodo)).thenReturn(existingTodo);

    Todo result = todoService.updateTodo(1L, updatedTodo);

    assertNotNull(result);
    assertEquals("Updated Title", result.getTitle());
    assertEquals("Updated Description", result.getDescription());
    assertTrue(result.getCompleted());
  }

  @Test
  void testUpdateTodo_NotFound() {
    Todo updatedTodo = new Todo();
    when(todoRepository.findById(1L)).thenReturn(Optional.empty());

    assertThrows(ResourceNotFoundException.class, () -> {
      todoService.updateTodo(1L, updatedTodo);
    });
  }

  @Test
  void testDeleteTodo() {
    doNothing().when(todoRepository).deleteById(1L);

    todoService.deleteTodo(1L);

    verify(todoRepository, times(1)).deleteById(1L);
  }
}
