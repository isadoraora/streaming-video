package com.fiap.streamingvideo.controller.integration;

import com.fiap.streamingvideo.model.UserDTO;
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

  @Test
  void createUserIntegrationTest() {
    UserDTO userDTO = new UserDTO("1747959", "84659459");

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
}
