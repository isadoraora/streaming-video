package com.fiap.streamingvideo.service;

import com.fiap.streamingvideo.mapper.CategoryMapper;
import com.fiap.streamingvideo.model.CategoryDTO;
import com.fiap.streamingvideo.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

  private final CategoryRepository categoryRepository;

  @Override
  public Mono<CategoryDTO> insert(Mono<CategoryDTO> categoryDTOMono) {
    return categoryDTOMono
        .map(CategoryMapper::toEntity)
        .flatMap(categoryRepository::save)
        .map(CategoryMapper::fromEntity);
  }

  @Override
  public Flux<CategoryDTO> getAllCategories() {
    return categoryRepository.findAll()
        .map(CategoryMapper::fromEntity);
  }

}
