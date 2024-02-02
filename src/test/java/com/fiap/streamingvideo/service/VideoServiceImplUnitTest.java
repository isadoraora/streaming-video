package com.fiap.streamingvideo.service;

import static com.mongodb.assertions.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import com.fiap.streamingvideo.entity.Video;
import com.fiap.streamingvideo.entity.VideoStatistics;
import com.fiap.streamingvideo.exception.NotFoundException;
import com.fiap.streamingvideo.mapper.VideoMapper;
import com.fiap.streamingvideo.model.VideoDTO;
import com.fiap.streamingvideo.repository.VideoRepository;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@ExtendWith(MockitoExtension.class)
class VideoServiceImplUnitTest {

  @Mock
  private VideoRepository videoRepository;
  @Mock
  private VideoMapper videoMapper;

  @InjectMocks
  VideoServiceImpl videoService;

  private VideoDTO videoDTO;

  private Video video;

  @BeforeEach
  void setUp() {
    videoDTO = new VideoDTO("85757", "Lion king", "Simba's life in the jungle", "www.nerdflix.com/lion-king",
        LocalDateTime.of(2023, Month.JANUARY, 1, 12, 0), List.of("8765478564"), true);
    video = new Video("85757", "Lion king", "Simba's life in the jungle", "www.nerdflix.com/lion-king",
        LocalDateTime.of(2023, Month.JANUARY, 1, 12, 0), List.of("8765478564"), true, 100L);
  }

  @Test
  void createVideoSuccessfully() {
    given(videoMapper.toEntity(any(VideoDTO.class))).willReturn(video);
    given(videoMapper.fromEntity(any(Video.class))).willReturn(videoDTO);
    given(videoRepository.save(any(Video.class))).willReturn(Mono.just(video));

    StepVerifier.create(videoService.createVideo(Mono.just(videoDTO)))
        .assertNext(createdVideo -> {
          assertNotNull(createdVideo);
          assertEquals(videoDTO.id(), createdVideo.id());
          assertEquals(videoDTO.title(), createdVideo.title());
          assertEquals(videoDTO.description(), createdVideo.description());
          assertEquals(videoDTO.url(), createdVideo.url());
          assertEquals(videoDTO.publishDate(), createdVideo.publishDate());
          assertEquals(videoDTO.isFavorite(), createdVideo.isFavorite());
          assertEquals(videoDTO.categories(), createdVideo.categories());
        })
        .verifyComplete();
  }

  @Test
  void getVideoByIdSuccessfully() {
    String id = videoDTO.id();
    given(videoRepository.findById(id)).willReturn(Mono.just(video));
    given(videoMapper.fromEntity(video)).willReturn(videoDTO);

    StepVerifier.create(videoService.getVideoById(id))
        .assertNext(retrievedVideo -> {
          assertNotNull(retrievedVideo);
          assertEquals(videoDTO.id(), retrievedVideo.id());
          assertEquals(videoDTO.title(), retrievedVideo.title());
          assertEquals(videoDTO.description(), retrievedVideo.description());
          assertEquals(videoDTO.url(), retrievedVideo.url());
          assertEquals(videoDTO.publishDate(), retrievedVideo.publishDate());
          assertEquals(videoDTO.isFavorite(), retrievedVideo.isFavorite());
          assertEquals(videoDTO.categories(), retrievedVideo.categories());
        })
        .verifyComplete();
  }

  @Test
  void getVideoByIdNotFound() {
    String id = "nonExistingId";
    given(videoRepository.findById(id)).willReturn(Mono.empty());

    StepVerifier.create(videoService.getVideoById(id))
        .expectError(NotFoundException.class)
        .verify();
  }

  @Test
  void deleteVideoSuccessfully() {
    String id = videoDTO.id();
    given(videoRepository.existsById(id)).willReturn(Mono.just(true));
    given(videoRepository.deleteById(id)).willReturn(Mono.empty());

    StepVerifier.create(videoService.deleteById(id))
        .verifyComplete();

    verify(videoRepository).deleteById(id);
  }

  @Test
  void deleteVideoNotFound() {
    String id = "nonExistingId";
    given(videoRepository.existsById(id)).willReturn(Mono.just(false));

    StepVerifier.create(videoService.deleteById(id))
        .expectError(NotFoundException.class)
        .verify();
  }

  @Test
  void updateVideoSuccessfully() {
    String id = videoDTO.id();
    Video updatedVideo = video;
    given(videoRepository.findById(id)).willReturn(Mono.just(video));
    given(videoRepository.save(any(Video.class))).willReturn(Mono.just(updatedVideo));
    given(videoMapper.fromEntity(updatedVideo)).willReturn(videoDTO);

    StepVerifier.create(videoService.updateVideo(id, Mono.just(videoDTO)))
        .assertNext(updatedVideoDTO -> {
          assertNotNull(updatedVideoDTO);
          assertEquals(videoDTO.id(), updatedVideoDTO.id());
          assertEquals(videoDTO.title(), updatedVideoDTO.title());
          assertEquals(videoDTO.description(), updatedVideoDTO.description());
          assertEquals(videoDTO.url(), updatedVideoDTO.url());
          assertEquals(videoDTO.publishDate(), updatedVideoDTO.publishDate());
          assertEquals(videoDTO.isFavorite(), updatedVideoDTO.isFavorite());
          assertEquals(videoDTO.categories(), updatedVideoDTO.categories());
        })
        .verifyComplete();
  }

  @Test
  void updateVideoNotFound() {
    String id = "nonExistingId";
    given(videoRepository.findById(id)).willReturn(Mono.empty());

    StepVerifier.create(videoService.updateVideo(id, Mono.just(videoDTO)))
        .expectError(NotFoundException.class)
        .verify();
  }

  @Test
  void getVideoStatisticsSuccessfully() {
    VideoStatistics statistics = new VideoStatistics(10, 5, 2.5);
    given(videoRepository.calculateVideoStatistics()).willReturn(Mono.just(statistics));

    StepVerifier.create(videoService.getVideoStatistics())
        .assertNext(videoStatisticsDTO -> {
          assertNotNull(videoStatisticsDTO);
          assertEquals(10, videoStatisticsDTO.getTotalVideos());
          assertEquals(5, videoStatisticsDTO.getTotalFavorited());
          assertEquals(2.5, videoStatisticsDTO.getAverageViews());
        })
        .verifyComplete();
  }

  @Test
  void getVideoByTitleSuccessfully() {
    given(videoRepository.findByTitle(videoDTO.title())).willReturn(Flux.just(video));
    given(videoMapper.fromEntity(video)).willReturn(videoDTO);

    StepVerifier.create(videoService.getVideosByTitle(videoDTO.title()))
        .assertNext(foundVideoDTO -> {
          assertNotNull(foundVideoDTO);
          assertEquals(videoDTO.title(), foundVideoDTO.title());
        })
        .verifyComplete();
  }

  @Test
  void getVideoByTitleNotFound() {
    given(videoRepository.findByTitle("Nonexistent Title")).willReturn(Flux.empty());

    StepVerifier.create(videoService.getVideosByTitle("Nonexistent Title"))
        .expectError(NotFoundException.class)
        .verify();
  }

  @Test
  void getVideoByPublishDateSuccessfully() {
    given(videoRepository.findByPublishDate(videoDTO.publishDate())).willReturn(Flux.just(video));
    given(videoMapper.fromEntity(video)).willReturn(videoDTO);

    StepVerifier.create(videoService.getVideoByPublishDate(videoDTO.publishDate()))
        .assertNext(foundVideoDTO -> {
          assertNotNull(foundVideoDTO);
          assertEquals(videoDTO.publishDate(), foundVideoDTO.publishDate());
        })
        .verifyComplete();
  }

  @Test
  void getVideoByTitleAndPublishDateSuccessfully() {
    given(videoRepository.findByTitleAndPublishDate(videoDTO.title(), videoDTO.publishDate()))
        .willReturn(Flux.just(video));
    given(videoMapper.fromEntity(video)).willReturn(videoDTO);

    StepVerifier.create(videoService.getVideoByTitleAndPublishDate(videoDTO.title(), videoDTO.publishDate()))
        .assertNext(foundVideoDTO -> {
          assertNotNull(foundVideoDTO);
          assertEquals(videoDTO.title(), foundVideoDTO.title());
          assertEquals(videoDTO.publishDate(), foundVideoDTO.publishDate());
        })
        .verifyComplete();
  }
}
