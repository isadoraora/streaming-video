package com.fiap.streamingvideo.controller;

import static org.mockito.Mockito.when;

import com.fiap.streamingvideo.model.VideoDTO;
import com.fiap.streamingvideo.service.UserService;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@WebFluxTest(UserFavoritesController.class)
public class UserFavoritesControllerUnitTest {

  LocalDateTime publishDate = LocalDateTime.parse("2024-01-29T18:00:00");

  @Autowired
  private WebTestClient webTestClient;

  @MockBean
  private UserService userService;

  @Test
  void getRecommendationsTest() {
    String userId = "someUserId";
    VideoDTO videoDTO = new VideoDTO("7654", "Movie title", "Movie description",
        "movie url", publishDate, List.of("84759746"), false);
    Flux<VideoDTO> videoDTOFlux = Flux.just(videoDTO);

    when(userService.getRecommendations(userId, 0, 10)).thenReturn(videoDTOFlux);

    webTestClient.get()
        .uri("/users/{userId}/recommendations?page=0&size=10", userId)
        .exchange()
        .expectStatus().isOk()
        .expectBodyList(VideoDTO.class)
        .contains(videoDTO);
  }

  @Test
  void favoriteVideoTest() {
    String userId = "userId";
    String videoId = "videoId";

    when(userService.favoriteVideo(userId, videoId)).thenReturn(Mono.empty());

    webTestClient.post()
        .uri("/users/{userId}/favorites/{videoId}", userId, videoId)
        .exchange()
        .expectStatus().isOk();
  }

  @Test
  void unfavoriteVideoTest() {
    String userId = "userId";
    String videoId = "videoId";

    when(userService.unfavoriteVideo(userId, videoId)).thenReturn(Mono.empty());

    webTestClient.delete()
        .uri("/users/{userId}/favorites/{videoId}", userId, videoId)
        .exchange()
        .expectStatus().isOk();
  }
}
