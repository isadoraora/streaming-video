package com.fiap.streamingvideo.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import com.fiap.streamingvideo.model.VideoDTO;
import com.fiap.streamingvideo.model.VideoStatisticsDTO;
import com.fiap.streamingvideo.service.VideoService;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@WebFluxTest(VideoController.class)
class VideoControllerUnitTest {

  @Autowired
  private WebTestClient webTestClient;

  @MockBean
  private VideoService videoService;

  private VideoDTO videoDTO;

  @BeforeEach
  void setUp() {
    String id = UUID.randomUUID().toString();
    videoDTO = new VideoDTO(id, "title", "description", "url",
        LocalDateTime.now(), List.of("category"), false);
  }

  @Test
  void createVideoTest() {
    when(videoService.createVideo(any())).thenReturn(Mono.just(videoDTO));

    webTestClient.post()
        .uri("/videos")
        .contentType(MediaType.APPLICATION_JSON)
        .body(BodyInserters.fromValue(videoDTO))
        .exchange()
        .expectStatus().isCreated()
        .expectBody(VideoDTO.class)
        .isEqualTo(videoDTO);
  }

  @Test
  void getVideoByIdTest() {
    String id = "id";
    Mono<VideoDTO> videoDTOMono = Mono.just(videoDTO);

    when(videoService.getVideoById(id)).thenReturn(videoDTOMono);

    webTestClient.get()
        .uri("/videos/{id}", id)
        .exchange()
        .expectStatus().isOk()
        .expectBody(VideoDTO.class)
        .isEqualTo(videoDTO);
  }

  @Test
  void deleteVideoTest() {
    String id = "id";

    when(videoService.deleteById(id)).thenReturn(Mono.empty());

    webTestClient.delete()
        .uri("/videos/{id}", id)
        .exchange()
        .expectStatus().isNoContent();
  }

  @Test
  void listAllVideosTest() {
    when(videoService.getAllVideos(0, 10)).thenReturn(Flux.just(videoDTO));

    webTestClient.get()
        .uri("/videos?page=0&size=10")
        .exchange()
        .expectStatus().isOk()
        .expectBodyList(VideoDTO.class)
        .hasSize(1)
        .contains(videoDTO);
  }

  @Test
  void updateVideoTest() {
    String id = videoDTO.id();

    when(videoService.updateVideo(eq(id), any())).thenReturn(Mono.just(videoDTO));

    webTestClient.put()
        .uri("/videos/{id}", id)
        .contentType(MediaType.APPLICATION_JSON)
        .body(BodyInserters.fromValue(videoDTO))
        .exchange()
        .expectStatus().isOk()
        .expectBody(VideoDTO.class)
        .isEqualTo(videoDTO);
  }

  @Test
  void getVideoByTitleTest() {
    String title = videoDTO.title();

    when(videoService.getVideoByTitle(title)).thenReturn(Mono.just(videoDTO));

    webTestClient.get()
        .uri(uriBuilder -> uriBuilder
            .path("/videos/search/by-title")
            .queryParam("title", title)
            .build())
        .exchange()
        .expectStatus().isOk()
        .expectBody(VideoDTO.class)
        .isEqualTo(videoDTO);
  }

  @Test
  void getVideoByPublishDateTest() {
    LocalDateTime publishDate = videoDTO.publishDate();

    when(videoService.getVideoByPublishDate(publishDate)).thenReturn(Mono.just(videoDTO));

    webTestClient.get()
        .uri(uriBuilder -> uriBuilder
            .path("/videos/search/by-publish-date")
            .queryParam("publishDate", publishDate.toString())
            .build())
        .exchange()
        .expectStatus().isOk()
        .expectBody(VideoDTO.class)
        .isEqualTo(videoDTO);
  }

  @Test
  void getVideoByTitleAndPublishDateTest() {
    String title = videoDTO.title();
    LocalDateTime publishDate = videoDTO.publishDate();

    when(videoService.getVideoByTitleAndPublishDate(title, publishDate)).thenReturn(Mono.just(videoDTO));

    webTestClient.get()
        .uri(uriBuilder -> uriBuilder
            .path("/videos/search")
            .queryParam("title", title)
            .queryParam("publishDate", publishDate.toString())
            .build())
        .exchange()
        .expectStatus().isOk()
        .expectBody(VideoDTO.class)
        .isEqualTo(videoDTO);
  }

  @Test
  void getVideoStatisticsTest() {
    VideoStatisticsDTO videoStatisticsDTO = new VideoStatisticsDTO(10, 5, 100.0);
    when(videoService.getVideoStatistics()).thenReturn(Mono.just(videoStatisticsDTO));

    webTestClient.get()
        .uri("/videos/statistics")
        .exchange()
        .expectStatus().isOk()
        .expectBody(VideoStatisticsDTO.class)
        .isEqualTo(videoStatisticsDTO);
  }

}
