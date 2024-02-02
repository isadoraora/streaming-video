package com.fiap.streamingvideo.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.fiap.streamingvideo.model.UserDTO;
import com.fiap.streamingvideo.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import reactor.core.publisher.Mono;

@WebFluxTest(UserController.class)
class UserControllerUnitTest {

  @Autowired
  private WebTestClient webTestClient;

  @MockBean
  private UserService userService;

  @Test
  void createUserTest() {
    UserDTO userDTO = new UserDTO("1234", "Marcio", "567898765", "marcio@msn.com");

    when(userService.create(any())).thenReturn(Mono.just(userDTO));

    webTestClient.post()
        .uri("/user")
        .contentType(MediaType.APPLICATION_JSON)
        .body(BodyInserters.fromValue(userDTO))
        .exchange()
        .expectStatus().isCreated()
        .expectBody(UserDTO.class)
        .isEqualTo(userDTO);
  }

  @Test
  void getUserByIdTest() {
    String userId = "1234";
    UserDTO userDTO = new UserDTO(userId, "Marcio", "567898765", "marcio@msn.com");

    when(userService.getById(userId)).thenReturn(Mono.just(userDTO));

    webTestClient.get()
        .uri("/user/{id}", userId)
        .exchange()
        .expectStatus().isOk()
        .expectBody(UserDTO.class)
        .isEqualTo(userDTO);
  }
}
