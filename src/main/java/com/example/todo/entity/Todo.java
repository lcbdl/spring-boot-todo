package com.example.todo.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Todo {

  private Integer id;

  private String title;
  private String description;
  @Builder.Default
  private Boolean completed = false;
}
