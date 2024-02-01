package com.fiap.streamingvideo.repository;

import com.fiap.streamingvideo.entity.User;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface UserRepository extends ReactiveCrudRepository<User, String> {

  Flux<User> findByUserId(String userId);

  Mono<Void> deleteByUserIdAndVideoId(String userId, String videoId);

  @Aggregation(pipeline = {
      "{ $unwind: '$favoriteVideos' }",
      "{ $group: { _id: '$favoriteVideos' } }",
      "{ $count: 'distinctVideoCount' }"
  })
  Mono<Long> countDistinctVideoId();
}
