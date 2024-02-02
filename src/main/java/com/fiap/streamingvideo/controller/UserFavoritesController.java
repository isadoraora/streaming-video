package com.fiap.streamingvideo.controller;

import com.fiap.streamingvideo.entity.UserFavorites;
import com.fiap.streamingvideo.model.VideoDTO;
import com.fiap.streamingvideo.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserFavoritesController {

  private final UserService userService;

  @GetMapping("/{userId}/recommendations")
  public Flux<VideoDTO> getRecommendations(@PathVariable String userId,
                                           @RequestParam(value = "page", defaultValue = "0") int page,
                                           @RequestParam(value = "size", defaultValue = "10") int size) {
    return userService.getRecommendations(userId, page, size);
  }

  @PostMapping("/{userId}/favorites/{videoId}")
  public Mono<ResponseEntity<Void>> favoriteVideo(@PathVariable String userId, @PathVariable String videoId) {
    return userService.favoriteVideo(userId, videoId)
        .thenReturn(ResponseEntity.ok().build());
  }

  @DeleteMapping("/{userId}/favorites/{videoId}")
  public Mono<ResponseEntity<Void>> unfavoriteVideo(@PathVariable String userId, @PathVariable String videoId) {
    return userService.unfavoriteVideo(userId, videoId)
        .thenReturn(ResponseEntity.ok().build());
  }

}
