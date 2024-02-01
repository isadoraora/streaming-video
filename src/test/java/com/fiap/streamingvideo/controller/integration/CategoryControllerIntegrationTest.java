package com.fiap.streamingvideo.controller.integration;

import static com.mongodb.assertions.Assertions.assertNotNull;

import com.fiap.streamingvideo.model.CategoryDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import reactor.core.publisher.Mono;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
class CategoryControllerIntegrationTest {

  @Autowired
  private WebTestClient webTestClient;

  @Test
  void createCategoryIntegrationTest() {
    CategoryDTO categoryDTO = new CategoryDTO(null, "Category Integration");
    Mono<CategoryDTO> categoryDTOMono = Mono.just(categoryDTO);

    webTestClient.post()
        .uri("/categories")
        .contentType(MediaType.APPLICATION_JSON)
        .body(BodyInserters.fromValue(categoryDTO))
        .exchange()
        .expectStatus().isCreated()
        .expectBody(CategoryDTO.class)
        .consumeWith(response -> {
          assert response.getResponseBody() != null;
          assertNotNull(response.getResponseBody().id());
        });
  }
}
