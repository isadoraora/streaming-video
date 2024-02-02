package com.fiap.streamingvideo.controller.integration;

import static com.mongodb.assertions.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.fiap.streamingvideo.model.VideoDTO;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
public class VideoControllerIntegrationTest {

  @Autowired
  private WebTestClient webTestClient;

  @Test
  public void createVideoIntegrationTest() {
    VideoDTO newVideo =
        new VideoDTO(null, "New Title", "New Description", "http://newurl.com", LocalDateTime.now(),
            List.of("Action", "Drama"), false);

    webTestClient.post()
        .uri("/videos")
        .contentType(MediaType.APPLICATION_JSON)
        .bodyValue(newVideo)
        .exchange()
        .expectStatus().isCreated()
        .expectBody(VideoDTO.class)
        .consumeWith(response -> {
          VideoDTO createdVideo = response.getResponseBody();
          assertNotNull(createdVideo);
          assertNotNull(createdVideo.id());
          assertEquals("New Title", createdVideo.title());
        });
  }
}
