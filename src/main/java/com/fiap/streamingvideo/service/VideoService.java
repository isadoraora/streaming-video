package com.fiap.streamingvideo.service;

import com.fiap.streamingvideo.model.VideoDTO;
import com.fiap.streamingvideo.model.VideoStatisticsDTO;
import java.time.LocalDateTime;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface VideoService {
  Mono<VideoDTO> createVideo(Mono<VideoDTO> videoDTOMono);

  Mono<VideoDTO> getVideoById(String id);

  Mono<Void> deleteById(String id);

  Flux<VideoDTO> getAllVideos(int page, int size);

  Mono<VideoDTO> updateVideo(String id, Mono<VideoDTO> videoDTOMono);

  Mono<VideoStatisticsDTO> getVideoStatistics();

  Mono<VideoDTO> getVideoByTitle(String title);

  Mono<VideoDTO> getVideoByPublishDate(LocalDateTime publishDate);

  Mono<VideoDTO> getVideoByTitleAndPublishDate(String title, LocalDateTime publishDate);


}
