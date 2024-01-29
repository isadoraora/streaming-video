package com.fiap.streamingvideo.service;

import com.fiap.streamingvideo.exception.NotFoundException;
import com.fiap.streamingvideo.mapper.VideoMapper;
import com.fiap.streamingvideo.model.VideoDTO;
import com.fiap.streamingvideo.repository.VideoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class VideoServiceImpl implements VideoService {

  private final VideoRepository videoRepository;
  private final VideoMapper videoMapper;

  @Override
  public Mono<VideoDTO> createVideo(Mono<VideoDTO> videoDTOMono) {
    return videoDTOMono
        .map(videoMapper::toEntity)
        .flatMap(videoRepository::save)
        .map(videoMapper::fromEntity);
  }


  @Override
  public Mono<VideoDTO> getVideoById(String id) {
    return videoRepository.findById(id)
        .switchIfEmpty(Mono.error(new NotFoundException("Video not found with id: " + id)))
        .map(videoMapper::fromEntity);
  }

  @Override
  public Mono<Void> deleteById(String id) {
    return videoRepository.existsById(id)
        .flatMap(exists -> {
          if (exists) {
            return videoRepository.deleteById(id);
          } else {
            return Mono.error(new NotFoundException("Video not found with id: " + id));
          }
        });
  }

  @Override
  public Flux<VideoDTO> getAllVideos(int page, int size) {
    return videoRepository.findAllBy(page, size)
        .map(videoMapper::fromEntity);
  }

  @Override
  public Mono<VideoDTO> updateVideo(String id, Mono<VideoDTO> videoDTOMono) {
    return videoRepository.findById(id)
        .flatMap(existingVideo -> videoDTOMono.map(videoDTO -> {
          existingVideo.setTitle(videoDTO.title());
          existingVideo.setUrl(videoDTO.url());
          existingVideo.setDescription(videoDTO.description());
          existingVideo.setIsFavorite(videoDTO.isFavorite());
          existingVideo.setPublishDate(videoDTO.publishDate());
          existingVideo.setCategories(videoDTO.categories());
          return existingVideo;
        }))
        .flatMap(videoRepository::save)
        .map(videoMapper::fromEntity)
        .switchIfEmpty(Mono.error(new NotFoundException("Video not found with id: " + id)));
  }

  @Override
  public Flux<VideoDTO> getRecommendations(Long userId, int page, int size) {
    return userFavoriteVideoRepository.findFavoritesByUserId(userId)
        .skip((long) page * size)
        .take(size)
        .map(VideoMapper::fromEntity);
  }

}
