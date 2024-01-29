package com.fiap.streamingvideo.mapper;

import com.fiap.streamingvideo.entity.Category;
import com.fiap.streamingvideo.model.CategoryDTO;

public class CategoryMapper {

  public static CategoryDTO fromEntity(Category category) {
    return new CategoryDTO(
        category.getId(),
        category.getName());
  }

  public static Category toEntity(CategoryDTO categoryDTO) {
    Category category = new Category();
    category.setId(categoryDTO.id());
    category.setName(categoryDTO.name());
    return category;
  }
}
