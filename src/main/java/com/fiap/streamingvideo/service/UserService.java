package com.fiap.streamingvideo.service;

import com.fiap.streamingvideo.entity.User;
import com.fiap.streamingvideo.model.UserDTO;
import com.fiap.streamingvideo.model.VideoDTO;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface UserService {

  Mono<UserDTO> create(Mono<UserDTO> userDTOMono);

  Mono<User> favoriteVideo(String userId, String videoId);
  Mono<Void> unfavoriteVideo(String userId, String videoId);

  Flux<VideoDTO> getRecommendations(String userId, int page, int size);

}
