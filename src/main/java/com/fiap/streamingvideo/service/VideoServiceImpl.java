package com.fiap.streamingvideo.service;

import com.fiap.streamingvideo.exception.NotFoundException;
import com.fiap.streamingvideo.mapper.VideoMapper;
import com.fiap.streamingvideo.model.VideoDTO;
import com.fiap.streamingvideo.model.VideoStatisticsDTO;
import com.fiap.streamingvideo.repository.VideoRepository;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
    Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "publishDate"));
    return videoRepository.findAllByOrderByPublishDateDesc(pageable)
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
  public Mono<VideoStatisticsDTO> getVideoStatistics() {
    return videoRepository.calculateVideoStatistics()
        .map(statistics -> new VideoStatisticsDTO(
            statistics.getTotalVideos(),
            statistics.getTotalFavorited(),
            statistics.getAverageViews()));
  }

  @Override
  public Mono<VideoDTO> getVideoByTitle(String title) {
    return videoRepository.findByTitle(title)
        .switchIfEmpty(Mono.error(new NotFoundException("Video not found with this title.")))
        .map(videoMapper::fromEntity);
  }

  @Override
  public Mono<VideoDTO> getVideoByPublishDate(LocalDateTime publishDate) {
    return videoRepository.findByPublishDate(publishDate)
        .switchIfEmpty(Mono.error(new NotFoundException("Video not found with this date.")))
        .map(videoMapper::fromEntity);
  }

  @Override
  public Mono<VideoDTO> getVideoByTitleAndPublishDate(String title, LocalDateTime publishDate) {
    return videoRepository.findByTitleAndPublishDate(title, publishDate)
        .switchIfEmpty(Mono.error(new NotFoundException("Video not found with this title and date.")))
        .map(videoMapper::fromEntity);
  }
}
