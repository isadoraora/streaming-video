package com.fiap.streamingvideo.controller;

import com.fiap.streamingvideo.model.CategoryDTO;
import com.fiap.streamingvideo.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping("/categories")
public class CategoryController {

  private final CategoryService categoryService;

  @PostMapping
  public Mono<ResponseEntity<CategoryDTO>> createCategory(@RequestBody Mono<CategoryDTO> categoryDTOMono) {
    return categoryService.insert(categoryDTOMono)
        .map(savedCategory -> ResponseEntity
            .status(HttpStatus.CREATED)
            .body(savedCategory))
        .defaultIfEmpty(ResponseEntity.status(HttpStatus.BAD_REQUEST).build());
  }

  @GetMapping
  public Flux<CategoryDTO> listAllCategories() {
    return categoryService.getAllCategories();
  }

}
