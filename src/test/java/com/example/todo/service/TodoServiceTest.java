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
    todo.setId(1);
    when(todoRepository.findById(1)).thenReturn(Optional.of(todo));

    Todo result = todoService.findById(1);

    assertNotNull(result);
    assertEquals(1, result.getId());
  }

  @Test
  void testFindById_NotFound() {
    when(todoRepository.findById(1)).thenReturn(Optional.empty());

    assertThrows(ResourceNotFoundException.class, () -> {
      todoService.findById(1);
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
    existingTodo.setId(1);
    Todo updatedTodo = new Todo();
    updatedTodo.setId(1);
    updatedTodo.setTitle("Updated Title");
    updatedTodo.setDescription("Updated Description");
    updatedTodo.setCompleted(true);

    when(todoRepository.findById(1)).thenReturn(Optional.of(existingTodo));
    when(todoRepository.update(1, updatedTodo)).thenReturn(updatedTodo);

    Todo result = todoService.updateTodo(1, updatedTodo);

    assertNotNull(result);
    assertEquals("Updated Title", result.getTitle());
    assertEquals("Updated Description", result.getDescription());
    assertTrue(result.getCompleted());
  }

  @Test
  void testUpdateTodo_NotFound() {
    Todo updatedTodo = Todo.builder().id(1).build();
    when(todoRepository.update(1, updatedTodo)).thenThrow(new ResourceNotFoundException());

    assertThrows(ResourceNotFoundException.class, () -> {
      todoService.updateTodo(1, updatedTodo);
    });
  }

  @Test
  void testDeleteTodo() {
    doNothing().when(todoRepository).deleteById(1);

    todoService.deleteTodo(1);

    verify(todoRepository, times(1)).deleteById(1);
  }
}
