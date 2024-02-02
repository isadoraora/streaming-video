package com.fiap.streamingvideo.controller.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.fiap.streamingvideo.model.UserDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
class UserControllerIntegrationTest {

  @Autowired
  private WebTestClient webTestClient;

  private String userId;

  @BeforeEach
  void setup() {
    UserDTO userDTO = new UserDTO(null, "Pedro", "786756", "pedroscn@hotmail.com");
    userId = webTestClient.post()
        .uri("/user")
        .contentType(MediaType.APPLICATION_JSON)
        .body(BodyInserters.fromValue(userDTO))
        .exchange()
        .expectStatus().isCreated()
        .expectBody(UserDTO.class)
        .returnResult()
        .getResponseBody()
        .id();
  }

  @Test
  void createUser() {
    UserDTO userDTO = new UserDTO(null, "Pedro", "786756", "pedroscn@hotmail.com");

    webTestClient.post()
        .uri("/user")
        .contentType(MediaType.APPLICATION_JSON)
        .body(BodyInserters.fromValue(userDTO))
        .exchange()
        .expectStatus().isCreated()
        .expectBody(UserDTO.class)
        .consumeWith(response -> {
          assert response.getResponseBody() != null;
        });
  }

  @Test
  void getUserById() {
    webTestClient.get()
        .uri("/user/" + userId)
        .exchange()
        .expectStatus().isOk()
        .expectBody(UserDTO.class)
        .consumeWith(response -> {
          assert response.getResponseBody() != null;
          assertEquals("Pedro", response.getResponseBody().name());
          assertEquals("786756", response.getResponseBody().cpf());
          assertEquals("pedroscn@hotmail.com", response.getResponseBody().email());
        });
  }
}
