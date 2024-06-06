package com.example.todo.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.example.todo.TodoApplication;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import net.minidev.json.JSONObject;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = TodoApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource("classpath:application-test.properties")
public class TodoRestControllerInterationTest {

  private final ObjectMapper objectMapper = new ObjectMapper();

  @LocalServerPort
  private int port;

  TestRestTemplate restTemplate;

  HttpHeaders headers;

  private String createURLWithPort(String uri) {
    return "http://localhost:" + port + uri;
  }

  @BeforeEach
  public void runBeforeAllTests() {
    headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    restTemplate = new TestRestTemplate();
  }

  @Test
  public void getGetTodoById_Fail() throws JsonMappingException, JsonProcessingException {
    String responseStr = restTemplate.getForObject(
        createURLWithPort("/todos/100"),
        String.class);
    JsonNode root = objectMapper.readTree(responseStr);

    assertNotNull(responseStr);
    assertNotNull(root);
    assertEquals(404, root.get("code").intValue());
    assertEquals("Resource not found", root.get("message").textValue());
  }

  @Test
  public void getGetTodoById_Success() throws JsonMappingException, JsonProcessingException {
    // Create a todo.
    JSONObject todoJsonObject = new JSONObject();

    todoJsonObject.put("title", "My first task");
    todoJsonObject.put("description", "Prepare lunch");

    HttpEntity<String> request = new HttpEntity<>(todoJsonObject.toString(), headers);

    restTemplate.postForObject(createURLWithPort("/todos"), request, String.class);

    String responseStr = restTemplate.getForObject(
        createURLWithPort("/todos/2"),
        String.class);
    JsonNode root = objectMapper.readTree(responseStr);

    assertNotNull(responseStr);
    assertNotNull(root);
    assertEquals(2, root.get("id").intValue());
    assertEquals("My first task", root.get("title").textValue());
  }

  @Test
  public void createTodo() throws JsonProcessingException {
    JSONObject todoJsonObject = new JSONObject();

    todoJsonObject.put("title", "My first task");
    todoJsonObject.put("description", "Prepare lunch");

    HttpEntity<String> request = new HttpEntity<>(todoJsonObject.toString(), headers);

    String responseStr = restTemplate.postForObject(createURLWithPort("/todos"), request, String.class);

    JsonNode root = objectMapper.readTree(responseStr);

    assertNotNull(responseStr);
    assertNotNull(root);
    assertEquals("My first task", root.get("title").textValue());

    todoJsonObject = new JSONObject();

    todoJsonObject.put("title", "My second task");
    todoJsonObject.put("description", "Prepare dinner");

    request = new HttpEntity<>(todoJsonObject.toString(), headers);

    responseStr = restTemplate.postForObject(createURLWithPort("/todos"), request, String.class);

    root = objectMapper.readTree(responseStr);

    assertNotNull(responseStr);
    assertNotNull(root);
    assertEquals("My second task", root.get("title").textValue());
  }

  @Test
  public void updateTodo() throws JsonProcessingException {
    JSONObject todoJsonObject = new JSONObject();
    // create a todo
    todoJsonObject.put("title", "Task to be changed");
    todoJsonObject.put("description", "Prepare lunch");

    HttpEntity<String> request = new HttpEntity<>(todoJsonObject.toString(), headers);

    String responseStr = restTemplate.postForObject(createURLWithPort("/todos"), request, String.class);
    JsonNode root = objectMapper.readTree(responseStr);
    int id = root.get("id").intValue();

    todoJsonObject = new JSONObject();
    todoJsonObject.put("id", id);
    todoJsonObject.put("title", "Task was changed");
    todoJsonObject.put("description", "Prepare lunch");

    request = new HttpEntity<>(todoJsonObject.toString(), headers);

    responseStr = restTemplate.patchForObject(createURLWithPort("/todos/" + id), request, String.class);

    root = objectMapper.readTree(responseStr);

    assertNotNull(responseStr);
    assertNotNull(root);
    assertEquals(id, root.get("id").intValue());
    assertEquals("Task was changed", root.get("title").textValue());
  }

  @Test
  public void deleteTodo() throws JsonProcessingException {
    JSONObject todoJsonObject = new JSONObject();
    // create a todo
    todoJsonObject.put("title", "Task to be deleted");
    todoJsonObject.put("description", "Prepare lunch");

    HttpEntity<String> request = new HttpEntity<>(todoJsonObject.toString(), headers);

    String responseStr = restTemplate.postForObject(createURLWithPort("/todos"), request, String.class);
    JsonNode root = objectMapper.readTree(responseStr);
    int id = root.get("id").intValue();

    restTemplate.delete(createURLWithPort("/todos/" + id));

    responseStr = restTemplate.getForObject(
        createURLWithPort("/todos/" + id),
        String.class);
    root = objectMapper.readTree(responseStr);

    assertNotNull(responseStr);
    assertNotNull(root);
    assertEquals(404, root.get("code").intValue());
    assertEquals("Resource not found", root.get("message").textValue());

  }

}
