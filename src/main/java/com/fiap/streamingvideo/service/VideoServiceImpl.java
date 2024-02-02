package com.fiap.streamingvideo.service;

import com.fiap.streamingvideo.entity.VideoStatistics;
import com.fiap.streamingvideo.exception.NotFoundException;
import com.fiap.streamingvideo.mapper.VideoMapper;
import com.fiap.streamingvideo.model.VideoDTO;
import com.fiap.streamingvideo.repository.VideoRepository;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
@Slf4j
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
    return videoRepository.findAll()
        .skip((long) page * size)
        .take(size)
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

  public Mono<VideoStatistics> getVideoStatistics() {
    return videoRepository.calculateVideoStatistics()
        .doOnNext(stats -> log.info("Estatísticas recebidas: totalVideos={}, totalFavorited={}, avgViews={}", stats.getTotalVideos(),
            stats.getTotalFavorited(), stats.getAverageViews()));
  }

  @Override
  public Flux<VideoDTO> getVideosByTitle(String title) {
    return videoRepository.findByTitle(title)
        .switchIfEmpty(Mono.error(new NotFoundException("Video not found with title: " + title)))
        .map(videoMapper::fromEntity);
  }

  @Override
  public Flux<VideoDTO> getVideoByPublishDate(LocalDateTime publishDate) {
    return videoRepository.findByPublishDate(publishDate)
        .switchIfEmpty(Mono.error(new NotFoundException("Video not found with this date.")))
        .map(videoMapper::fromEntity);
  }

  @Override
  public Flux<VideoDTO> getVideoByTitleAndPublishDate(String title, LocalDateTime publishDate) {
    return videoRepository.findByTitleAndPublishDate(title, publishDate)
        .switchIfEmpty(Mono.error(new NotFoundException("Video not found with this title and date.")))
        .map(videoMapper::fromEntity);
  }

  @Override
  public Mono<VideoDTO> viewVideo(String videoId) {
    return videoRepository.findById(videoId)
        .flatMap(video -> {
          video.setViews(video.getViews() + 1);
          return videoRepository.save(video);
        })
        .map(videoMapper::fromEntity);
  }
}
