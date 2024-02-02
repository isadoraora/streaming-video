package com.fiap.streamingvideo.repository;

import com.fiap.streamingvideo.entity.UserFavorites;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface UserFavoritesRepository extends ReactiveCrudRepository<UserFavorites, String> {
  Mono<Void> deleteByUserIdAndVideoId(String userId, String videoId);

  Flux<UserFavorites> findByUserId(String userId);

}
