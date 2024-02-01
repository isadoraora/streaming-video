package com.fiap.streamingvideo.service;

import static com.mongodb.assertions.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import com.fiap.streamingvideo.entity.Category;
import com.fiap.streamingvideo.exception.NotFoundException;
import com.fiap.streamingvideo.mapper.CategoryMapper;
import com.fiap.streamingvideo.model.CategoryDTO;
import com.fiap.streamingvideo.repository.CategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@ExtendWith(MockitoExtension.class)
class CategoryServiceImplUnitTest {

  @Mock
  private CategoryRepository categoryRepository;

  @InjectMocks
  private CategoryServiceImpl categoryService;

  private CategoryDTO categoryDTO;

  private Category category;

  @BeforeEach
  void setUp() {
    categoryDTO = new CategoryDTO("1", "Horror");
    category = CategoryMapper.toEntity(categoryDTO);
  }

  @Test
  void insertSuccessfully() {
    given(categoryRepository.save(any(Category.class))).willReturn(Mono.just(category));

    StepVerifier.create(categoryService.insert(Mono.just(categoryDTO)))
        .assertNext(savedCategory -> {
          assertNotNull(savedCategory);
          assertEquals(categoryDTO.name(), savedCategory.name());
          assertEquals(categoryDTO.id(), savedCategory.id());
        })
        .verifyComplete();
  }

  @Test
  void getAllCategoriesSuccessfully() {
    given(categoryRepository.findAll()).willReturn(Flux.just(category));

    StepVerifier.create(categoryService.getAllCategories())
        .assertNext(retrievedCategory -> {
          assertNotNull(retrievedCategory);
          assertEquals(categoryDTO.name(), retrievedCategory.name());
          assertEquals(categoryDTO.id(), retrievedCategory.id());
        })
        .verifyComplete();
  }

  @Test
  void getCategoryByIdSuccessfully() {
    given(categoryRepository.findById("1")).willReturn(Mono.just(category));

    StepVerifier.create(categoryService.getCategoryById("1"))
        .assertNext(retrievedCategory -> {
          assertNotNull(retrievedCategory);
          assertEquals(categoryDTO.name(), retrievedCategory.name());
          assertEquals(categoryDTO.id(), retrievedCategory.id());
        })
        .verifyComplete();
  }

  @Test
  void shouldTrowExceptionWhenCategoryIdNotFound() {
    String id = "nonExistingId";
    given(categoryRepository.findById(id)).willReturn(Mono.empty());

    StepVerifier.create(categoryService.getCategoryById(id))
        .expectErrorMatches(throwable -> throwable instanceof NotFoundException &&
            throwable.getMessage().equals("Category not found with id: " + id))
        .verify();
  }
}
