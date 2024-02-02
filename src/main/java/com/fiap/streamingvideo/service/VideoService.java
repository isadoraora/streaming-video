package com.fiap.streamingvideo.service;

import com.fiap.streamingvideo.entity.VideoStatistics;
import com.fiap.streamingvideo.model.VideoDTO;
import java.time.LocalDateTime;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface VideoService {
  Mono<VideoDTO> createVideo(Mono<VideoDTO> videoDTOMono);

  Mono<VideoDTO> getVideoById(String id);

  Mono<Void> deleteById(String id);

  Flux<VideoDTO> getAllVideos(int page, int size);

  Mono<VideoDTO> updateVideo(String id, Mono<VideoDTO> videoDTOMono);

  Mono<VideoStatistics> getVideoStatistics();

  Flux<VideoDTO> getVideosByTitle(String title);

  Flux<VideoDTO> getVideoByPublishDate(LocalDateTime publishDate);

  Flux<VideoDTO> getVideoByTitleAndPublishDate(String title, LocalDateTime publishDate);

  Mono<VideoDTO> viewVideo(String videoId);

}
