package com.example.todo.controller;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.example.todo.entity.Todo;
import com.example.todo.service.TodoService;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@RestController
@RequestMapping("/todos")
public class TodoRestController {
  private Logger logger = LoggerFactory.getLogger(TodoRestController.class);

  @Autowired
  private TodoService todoService;

  @PostMapping()
  public ResponseEntity<Todo> createTodo(@RequestBody Todo todo) {
    Todo newTodo = todoService.createTodo(todo);
    return new ResponseEntity<>(newTodo, HttpStatusCode.valueOf(201));
  }

  @GetMapping()
  public ResponseEntity<Iterable<Todo>> findAllTodos() {
    List<Todo> todos = todoService.findAll();
    return new ResponseEntity<>(todos, HttpStatusCode.valueOf(200));
  }

  @GetMapping("/{id}")
  public ResponseEntity<Todo> findTodoById(@PathVariable Integer id) {
    ResponseEntity<Todo> response = new ResponseEntity<>(todoService.findById(id), HttpStatus.OK);
    return response;
  }

  @PatchMapping("/{id}")
  public ResponseEntity<Todo> updateTodo(@PathVariable Integer id, @RequestBody Todo todo) {
    ResponseEntity<Todo> response = new ResponseEntity<>(todoService.updateTodo(id, todo), HttpStatus.OK);
    return response;
  }

  @DeleteMapping("/{id}")
  public ResponseEntity deleteTodo(@PathVariable Integer id) {
    todoService.deleteTodo(id);
    return new ResponseEntity(HttpStatus.NO_CONTENT);
  }

}
