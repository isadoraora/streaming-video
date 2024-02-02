package com.fiap.streamingvideo.repository;

import static com.mongodb.assertions.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.fiap.streamingvideo.entity.Video;
import com.fiap.streamingvideo.entity.VideoStatistics;
import java.time.LocalDateTime;
import java.util.Collections;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@DataMongoTest
public class VideoRepositoryTest {

  @Autowired
  private VideoRepository videoRepository;

  @BeforeEach
  void setUp() {
    videoRepository.deleteAll().block();

    LocalDateTime publishDate = LocalDateTime.parse("2024-01-27T18:00:00");

    Video video = new Video("Sinais", "Milharal com desenhos estranhos", "www.nerdflix.com/sinais", publishDate,
        Collections.singletonList("65b71f254e0c824109a067b3"), true);
    videoRepository.save(video).block();
  }

  @Test
  void findByPublishDateTest() {
    LocalDateTime publishDate = LocalDateTime.parse("2024-01-27T18:00:00");

    StepVerifier.create(videoRepository.findByPublishDate(publishDate))
        .assertNext(video -> {
          assertNotNull(video);
          assertEquals(publishDate, video.getPublishDate());
        })
        .verifyComplete();
  }

  @Test
  void testVideoStatisticsAggregation() {
    assertNotNull(videoRepository);

    Mono<VideoStatistics> result = videoRepository.calculateVideoStatistics();

    StepVerifier.create(result)
        .assertNext(statistics -> {
          assertNotNull(statistics);
          assertNotNull(statistics.getTotalVideos());
          assertNotNull(statistics.getTotalFavorited());
          assertNotNull(statistics.getAverageViews());
        })
        .verifyComplete();
  }

}

