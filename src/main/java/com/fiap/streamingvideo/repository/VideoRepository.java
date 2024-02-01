package com.fiap.streamingvideo.repository;

import com.fiap.streamingvideo.entity.Video;
import com.fiap.streamingvideo.entity.VideoStatistics;
import java.time.LocalDateTime;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface VideoRepository extends ReactiveCrudRepository<Video, String> {

  Flux<Video> findAllByOrderByPublishDateDesc(Pageable pageable);

  Mono<Video> findByTitle(String title);

  Mono<Video> findByPublishDate(LocalDateTime publishDate);

  Mono<Video> findByTitleAndPublishDate(String title, LocalDateTime publishDate);

  @Aggregation(pipeline = {
      "{ $group: { _id: null, totalVideos: { $sum: 1 }, totalFavorited: { $sum: { $cond: [ '$isFavorited', 1, 0 ] } }, avgViews: { $avg: '$views' } } }"
  })
  Mono<VideoStatistics> calculateVideoStatistics();

}
