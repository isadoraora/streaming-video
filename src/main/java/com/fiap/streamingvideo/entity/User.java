package com.fiap.streamingvideo.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@Document
public class User {
  @Id
  private String userId;

  private String name;
  private String cpf;
  private String email;

  public User(String id, String name, String cpf, String email) {
    this.userId = id;
    this.name = name;
    this.cpf = cpf;
    this.email = email;
  }

  public User() {
  }
}
