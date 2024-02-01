package com.fiap.streamingvideo.service;

import static com.mongodb.assertions.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import com.fiap.streamingvideo.entity.User;
import com.fiap.streamingvideo.entity.Video;
import com.fiap.streamingvideo.mapper.UserMapper;
import com.fiap.streamingvideo.mapper.VideoMapper;
import com.fiap.streamingvideo.model.UserDTO;
import com.fiap.streamingvideo.model.VideoDTO;
import com.fiap.streamingvideo.repository.UserRepository;
import com.fiap.streamingvideo.repository.VideoRepository;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.Collections;
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
class UserServiceImplUnitTest {

  @Mock
  private UserRepository userRepository;
  @Mock
  private VideoRepository videoRepository;
  @Mock
  private VideoMapper videoMapper;
  @InjectMocks
  private UserServiceImpl userService;

  private UserDTO userDTO;
  private User user;
  private static final String USER_ID = "575585";
  private static final String VIDEO_ID = "96536523895";
  private static final String VIDEO_ID2 = "1234678";
  private static final LocalDateTime DATE_TIME = LocalDateTime.of(2022, Month.JANUARY, 1, 12, 0);


  @BeforeEach
  void setUp() {
    userDTO = new UserDTO("7", "16768");
    user = UserMapper.toEntity(userDTO);
  }

  @Test
  void createSuccessfully() {
    given(userRepository.save(any(User.class))).willReturn(Mono.just(user));

    StepVerifier.create(userService.create(Mono.just(userDTO)))
        .assertNext(createdUser -> {
          assertNotNull(createdUser);
          assertEquals(userDTO.userId(), createdUser.userId());
          assertEquals(userDTO.videoId(), createdUser.videoId());
        })
        .verifyComplete();
  }

  @Test
  void favoriteVideoSuccessfully() {
    User user = new User(USER_ID, null);
    Video video = new Video(VIDEO_ID, "Video Title", "Description", "url",
        LocalDateTime.now(), null, false, 150L);

    given(userRepository.findById(USER_ID)).willReturn(Mono.just(user));
    given(videoRepository.findById(VIDEO_ID)).willReturn(Mono.just(video));
    given(userRepository.save(any(User.class))).willReturn(Mono.just(new User(USER_ID, VIDEO_ID)));

    StepVerifier.create(userService.favoriteVideo(USER_ID, VIDEO_ID))
        .assertNext(favoritedVideo -> {
          assertNotNull(favoritedVideo);
          assertEquals(USER_ID, favoritedVideo.getUserId());
          assertEquals(VIDEO_ID, favoritedVideo.getVideoId());
        })
        .verifyComplete();
  }

  @Test
  void unfavoriteVideoSuccessfully() {
    given(userRepository.deleteByUserIdAndVideoId(USER_ID, VIDEO_ID)).willReturn(Mono.empty());

    StepVerifier.create(userService.unfavoriteVideo(USER_ID, VIDEO_ID))
        .verifyComplete();

    verify(userRepository).deleteByUserIdAndVideoId(USER_ID, VIDEO_ID);
  }

  @Test
  void getRecommendationsSuccessfully() {
    int page = 0;
    int size = 2;

    User favorite1 = new User(USER_ID, VIDEO_ID);
    User favorite2 = new User(USER_ID, VIDEO_ID2);
    Video video1 = new Video(VIDEO_ID, "Video Title 1", "Description 1", "url1", DATE_TIME, Collections.singletonList("2456"), false, 120L);
    Video video2 =
        new Video(VIDEO_ID2, "Video Title 2", "Description 2", "url2", DATE_TIME, Collections.singletonList("36436336"), true, 100L);
    VideoDTO videoDTO1 =
        new VideoDTO(VIDEO_ID, "Video Title 1", "Description 1", "url1", DATE_TIME, Collections.singletonList("2456"), false);
    VideoDTO videoDTO2 =
        new VideoDTO(VIDEO_ID2, "Video Title 2", "Description 2", "url2", DATE_TIME, Collections.singletonList("36436336"), true);

    given(userRepository.findByUserId(USER_ID)).willReturn(Flux.just(favorite1, favorite2));
    given(videoRepository.findById(VIDEO_ID)).willReturn(Mono.just(video1));
    given(videoRepository.findById(VIDEO_ID2)).willReturn(Mono.just(video2));
    given(videoMapper.fromEntity(video1)).willReturn(
        new VideoDTO(VIDEO_ID, "Video Title 1", "Description 1", "url1", DATE_TIME, Collections.singletonList("2456"), false));
    given(videoMapper.fromEntity(video2)).willReturn(
        new VideoDTO(VIDEO_ID2, "Video Title 2", "Description 2", "url2", DATE_TIME, Collections.singletonList("36436336"), true));

    StepVerifier.create(userService.getRecommendations(USER_ID, page, size))
        .assertNext(videoDTO -> {
          assertNotNull(videoDTO);
          assertEquals(videoDTO1, videoDTO);

        })
        .assertNext(videoDTO -> {
          assertNotNull(videoDTO);
          assertEquals(videoDTO2, videoDTO);
        })
        .verifyComplete();

  }
}
