package com.example.todo.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.example.todo.entity.Todo;
import com.example.todo.service.TodoService;

public class TodoRestControllerTest {

  @InjectMocks
  private TodoRestController todoRestController;

  @Mock
  private TodoService todoService;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void testCreateTodo() {
    Todo todo = new Todo();
    Todo savedTodo = new Todo();
    when(todoService.createTodo(todo)).thenReturn(savedTodo);

    ResponseEntity<Todo> response = todoRestController.createTodo(todo);

    assertNotNull(response);
    assertEquals(HttpStatus.CREATED, response.getStatusCode());
    assertEquals(savedTodo, response.getBody());
    verify(todoService, times(1)).createTodo(todo);
  }

  @Test
  void testFindAllTodos() {
    Todo todo1 = new Todo();
    Todo todo2 = new Todo();
    List<Todo> todos = Arrays.asList(todo1, todo2);
    when(todoService.findAll()).thenReturn(todos);

    ResponseEntity<Iterable<Todo>> response = todoRestController.findAllTodos();

    assertNotNull(response);
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(todos, response.getBody());
    verify(todoService, times(1)).findAll();
  }

  @Test
  void testFindTodoById_Success() {
    Todo todo = new Todo();
    when(todoService.findById(1L)).thenReturn(todo);

    ResponseEntity<Todo> response = todoRestController.findTodoById(1L);

    assertNotNull(response);
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(todo, response.getBody());
    verify(todoService, times(1)).findById(1L);
  }

  @Test
  void testFindTodoById_Fail() {
    when(todoService.findById(1L)).thenThrow(new ResourceNotFoundException());

    assertThrowsExactly(ResourceNotFoundException.class, () -> {
      ResponseEntity<Todo> response = todoRestController.findTodoById(1L);
      assertNull(response);
    });
  }

  @Test
  void testUpdateTodo() {
    Todo todo = new Todo();
    Todo updatedTodo = new Todo();
    when(todoService.updateTodo(1L, todo)).thenReturn(updatedTodo);

    ResponseEntity<Todo> response = todoRestController.updateTodo(1L, todo);

    assertNotNull(response);
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(updatedTodo, response.getBody());
    verify(todoService, times(1)).updateTodo(1L, todo);
  }

  @Test
  void testDeleteTodo() {
    doNothing().when(todoService).deleteTodo(1L);

    ResponseEntity response = todoRestController.deleteTodo(1L);

    assertNotNull(response);
    assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    verify(todoService, times(1)).deleteTodo(1L);
  }
}
