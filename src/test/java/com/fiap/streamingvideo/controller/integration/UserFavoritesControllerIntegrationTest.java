package com.fiap.streamingvideo.controller.integration;


import com.fiap.streamingvideo.model.VideoDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
public class UserFavoritesControllerIntegrationTest {

  @Autowired
  private WebTestClient webTestClient;

  @Test
  void getRecommendationsTest() {
    String userId = "algumUserId";
    webTestClient.get()
        .uri("/users/{userId}/recommendations?page=0&size=10", userId)
        .exchange()
        .expectStatus().isOk()
        .expectBodyList(VideoDTO .class);
  }

  @Test
  void favoriteVideoTest() {
    String userId = "algumUserId";
    String videoId = "algumVideoId";

    webTestClient.post()
        .uri("/users/{userId}/favorites/{videoId}", userId, videoId)
        .exchange()
        .expectStatus().isOk();
  }

  @Test
  void unfavoriteVideoTest() {
    String userId = "algumUserId";
    String videoId = "algumVideoId";

    webTestClient.delete()
        .uri("/users/{userId}/favorites/{videoId}", userId, videoId)
        .exchange()
        .expectStatus().isOk();
  }
}

