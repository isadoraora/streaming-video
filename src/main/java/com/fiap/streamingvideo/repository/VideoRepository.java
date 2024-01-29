package com.fiap.streamingvideo.repository;

import com.fiap.streamingvideo.entity.Video;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface VideoRepository extends ReactiveCrudRepository<Video, String> {

  Flux<Video> findAllBy(int skip, int limit);

}
