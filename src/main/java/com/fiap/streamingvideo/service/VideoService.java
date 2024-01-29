package com.fiap.streamingvideo.service;

import com.fiap.streamingvideo.model.VideoDTO;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface VideoService {
  Mono<VideoDTO> createVideo(Mono<VideoDTO> videoDTOMono);

  Mono<VideoDTO> getVideoById(String id);

  Mono<Void> deleteById(String id);

  Flux<VideoDTO> getAllVideos(int page, int size);

  Mono<VideoDTO> updateVideo(String id, Mono<VideoDTO> videoDTOMono);

  Flux<VideoDTO> getRecommendations(Long userId, int page, int size);

}
