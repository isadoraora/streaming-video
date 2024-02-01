package com.fiap.streamingvideo.service;

import com.fiap.streamingvideo.entity.User;
import com.fiap.streamingvideo.entity.Video;
import com.fiap.streamingvideo.mapper.UserMapper;
import com.fiap.streamingvideo.mapper.VideoMapper;
import com.fiap.streamingvideo.model.UserDTO;
import com.fiap.streamingvideo.model.VideoDTO;
import com.fiap.streamingvideo.repository.UserRepository;
import com.fiap.streamingvideo.repository.VideoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

  private final UserRepository userRepository;
  private final VideoRepository videoRepository;
  private final VideoMapper videoMapper;

  @Override
  public Mono<UserDTO> create(Mono<UserDTO> userDTOMono) {
    return userDTOMono
        .map(UserMapper::toEntity)
        .flatMap(userRepository::save)
        .map(UserMapper::fromEntity);
  }

  @Override
  public Mono<User> favoriteVideo(String userId, String videoId) {
    return Mono.zip(
            userRepository.findById(userId),
            videoRepository.findById(videoId)
        )
        .flatMap(tuple -> {
          User user = tuple.getT1();
          Video video = tuple.getT2();
          User userFavoriteVideo = new User(user.getUserId(), video.getId());
          return userRepository.save(userFavoriteVideo);
        });
  }

  @Override
  public Mono<Void> unfavoriteVideo(String userId, String videoId) {
    return userRepository.deleteByUserIdAndVideoId(userId, videoId);
  }

  @Override
  public Flux<VideoDTO> getRecommendations(String userId, int page, int size) {
    long skip = (long) page * size;
    return userRepository.findByUserId(userId)
        .skip(skip)
        .take(size)
        .flatMap(favorite -> videoRepository.findById(favorite.getVideoId()))
        .map(videoMapper::fromEntity);
  }
}


