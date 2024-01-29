package com.fiap.streamingvideo.repository;

import com.fiap.streamingvideo.entity.Category;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface CategoryRepository extends ReactiveCrudRepository<Category, String> {
}
