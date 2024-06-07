package com.example.todo.repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.example.todo.controller.ResourceNotFoundException;
import com.example.todo.entity.Todo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Repository
public class JdbcTodoRepositoryImpl implements TodoRepository {

  @Autowired
  private JdbcTemplate jdbcTemplate;

  public List<Todo> findAll() {
    return jdbcTemplate.query("select * from todo",
        (rs, rowNum) -> Todo.builder().id(rs.getInt("id")).title(rs.getString("title"))
            .description(rs.getString("description")).completed(rs.getBoolean("completed")).build());
  }

  public Optional<Todo> findById(Integer id) {
    try {
      return jdbcTemplate.queryForObject("select * from todo where id = ?",
          (rs, rowNum) -> Optional.of(Todo.builder().id(rs.getInt("id")).title(rs.getString("title"))
              .description(rs.getString("description")).completed(rs.getBoolean("completed")).build()),
          id);
    } catch (EmptyResultDataAccessException e) {
      return Optional.empty();
    }
  }

  public void deleteById(Integer id) {
    jdbcTemplate.update("delete from todo where id=?", id);
  }

  public Todo save(Todo todo) {
    KeyHolder keyHolder = new GeneratedKeyHolder();
    jdbcTemplate.update(connection -> {
      PreparedStatement ps = connection
          .prepareStatement("insert into todo (title, description, completed) values (?, ?, ?)",
              Statement.RETURN_GENERATED_KEYS);
      ps.setString(1, todo.getTitle());
      ps.setString(2, todo.getDescription());
      ps.setBoolean(3, todo.getCompleted() == null ? false : todo.getCompleted());
      return ps;
    }, keyHolder);
    if (keyHolder.getKey() != null) {
      todo.setId(keyHolder.getKeyAs(Integer.class));
    }
    return todo;
  }

  public Todo update(Todo todo) {
    this.findById(todo.getId()).orElseThrow(() -> new ResourceNotFoundException());
    jdbcTemplate.update("update todo set title=?, description=?, completed=? where id=?", todo.getTitle(),
        todo.getDescription(), todo.getCompleted() == null ? false : todo.getCompleted(), todo.getId());
    return todo;
  }

}
