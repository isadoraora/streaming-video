package com.fiap.streamingvideo.controller;

import com.fiap.streamingvideo.model.VideoDTO;
import com.fiap.streamingvideo.service.VideoService;
import lombok.RequiredArgsConstructor;
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
}
