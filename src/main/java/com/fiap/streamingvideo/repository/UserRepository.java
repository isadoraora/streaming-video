package com.fiap.streamingvideo.repository;

import com.fiap.streamingvideo.entity.User;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface UserRepository extends ReactiveCrudRepository<User, String> {
}
