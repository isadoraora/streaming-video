package com.fiap.streamingvideo.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.fiap.streamingvideo.model.CategoryDTO;
import com.fiap.streamingvideo.service.CategoryService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@WebFluxTest(CategoryController.class)
class CategoryControllerUnitTest {

  @Autowired
  private WebTestClient webTestClient;

  @MockBean
  private CategoryService categoryService;

  @Test
  void createCategory() {
    CategoryDTO categoryDTO = new CategoryDTO("1", "Category 1");
    Mono<CategoryDTO> categoryDTOMono = Mono.just(categoryDTO);

    when(categoryService.insert(any())).thenReturn(categoryDTOMono);

    webTestClient.post()
        .uri("/categories")
        .contentType(MediaType.APPLICATION_JSON)
        .body(BodyInserters.fromValue(categoryDTO))
        .exchange()
        .expectStatus().isCreated()
        .expectBody(CategoryDTO.class)
        .isEqualTo(categoryDTO);
  }

  @Test
  void listAllCategories() {
    CategoryDTO categoryDTO1 = new CategoryDTO("1", "Category 1");
    CategoryDTO categoryDTO2 = new CategoryDTO("2", "Category 2");
    Flux<CategoryDTO> categoryDTOFlux = Flux.just(categoryDTO1, categoryDTO2);

    when(categoryService.getAllCategories()).thenReturn(categoryDTOFlux);

    webTestClient.get()
        .uri("/categories")
        .exchange()
        .expectStatus().isOk()
        .expectBodyList(CategoryDTO.class)
        .hasSize(2)
        .contains(categoryDTO1, categoryDTO2);
  }

  @Test
  void getCategoryById() {
    CategoryDTO categoryDTO = new CategoryDTO("1", "Category 1");
    Mono<CategoryDTO> categoryDTOMono = Mono.just(categoryDTO);

    when(categoryService.getCategoryById("1")).thenReturn(categoryDTOMono);

    webTestClient.get()
        .uri("/categories/1")
        .exchange()
        .expectStatus().isOk()
        .expectBody(CategoryDTO.class)
        .isEqualTo(categoryDTO);
  }

}
