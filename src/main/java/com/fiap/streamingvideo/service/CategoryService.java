package com.fiap.streamingvideo.service;

import com.fiap.streamingvideo.model.CategoryDTO;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CategoryService {
  Mono<CategoryDTO> insert(Mono<CategoryDTO> categoryDTOMono);

  Flux<CategoryDTO> getAllCategories();
  Mono<CategoryDTO> getCategoryById(String id);

}

