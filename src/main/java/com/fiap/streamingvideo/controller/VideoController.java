package com.fiap.streamingvideo.controller;

import com.fiap.streamingvideo.entity.VideoStatistics;
import com.fiap.streamingvideo.model.VideoDTO;
import com.fiap.streamingvideo.service.VideoService;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping("/videos")
public class VideoController {

  private final VideoService videoService;

  @PostMapping
  public Mono<ResponseEntity<VideoDTO>> createVideo(@RequestBody Mono<VideoDTO> videoDTOMono) {
    return videoService.createVideo(videoDTOMono)
        .map(savedVideo -> ResponseEntity
            .status(HttpStatus.CREATED)
            .body(savedVideo))
        .defaultIfEmpty(ResponseEntity.status(HttpStatus.BAD_REQUEST).build());
  }

  @GetMapping("/{id}")
  public Mono<ResponseEntity<VideoDTO>> getVideoById(@PathVariable String id) {
    return videoService.getVideoById(id)
        .map(ResponseEntity::ok)
        .defaultIfEmpty(ResponseEntity.notFound().build());
  }

  @DeleteMapping("/{id}")
  public Mono<ResponseEntity<Void>> deleteVideo(@PathVariable String id) {
    return videoService.deleteById(id)
        .thenReturn(ResponseEntity.noContent().build());
  }

  @GetMapping
  public Flux<VideoDTO> listAllVideos(@RequestParam(value = "page", defaultValue = "0") int page,
                                      @RequestParam(value = "size", defaultValue = "10") int size) {
    return videoService.getAllVideos(page, size);
  }

  @PutMapping("/{id}")
  public Mono<ResponseEntity<VideoDTO>> updateVideo(@PathVariable String id, @RequestBody Mono<VideoDTO> videoDTOMono) {
    return videoService.updateVideo(id, videoDTOMono)
        .map(ResponseEntity::ok)
        .defaultIfEmpty(ResponseEntity.notFound().build());
  }

  @GetMapping("/search/by-title")
  public Mono<ResponseEntity<Flux<VideoDTO>>> getVideosByTitle(@RequestParam String title) {
    Flux<VideoDTO> videoFlux = videoService.getVideosByTitle(title);
    return videoFlux.hasElements()
        .map(hasElements -> hasElements ? ResponseEntity.ok(videoFlux) : ResponseEntity.notFound().build());
  }

  @GetMapping("/search/by-publish-date")
  public Mono<ResponseEntity<Flux<VideoDTO>>> getVideoByPublishDate(
      @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime publishDate) {
    Flux<VideoDTO> videoDTOFlux = videoService.getVideoByPublishDate(publishDate);
    return videoDTOFlux.hasElements()
        .map(hasElements -> hasElements
            ? ResponseEntity.ok(videoDTOFlux)
            : ResponseEntity.notFound().build());
  }

  @GetMapping("/search")
  public Mono<ResponseEntity<Flux<VideoDTO>>> getVideoByTitleAndPublishDate(
      @RequestParam String title,
      @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime publishDate) {
    Flux<VideoDTO> videoDTOFlux = videoService.getVideoByTitleAndPublishDate(title, publishDate);
    return videoDTOFlux.hasElements()
        .map(hasElements -> hasElements
            ? ResponseEntity.ok(videoDTOFlux)
            : ResponseEntity.notFound().build());
  }

  @GetMapping("/all/statistics")
  public Mono<VideoStatistics> getVideoStatistics() {
    return videoService.getVideoStatistics();
  }

  @GetMapping("/{videoId}/view")
  public Mono<ResponseEntity<VideoDTO>> viewVideo(@PathVariable String videoId) {
    return videoService.viewVideo(videoId)
        .map(ResponseEntity::ok)
        .defaultIfEmpty(ResponseEntity.notFound().build());
  }
}
