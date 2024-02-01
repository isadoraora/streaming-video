package com.fiap.streamingvideo.mapper;

import com.fiap.streamingvideo.entity.User;
import com.fiap.streamingvideo.model.UserDTO;

public class UserMapper {

  public static UserDTO fromEntity(User user) {
    return new UserDTO(
        user.getUserId(),
        user.getVideoId());
  }

  public static User toEntity(UserDTO userDTO) {
    User user = new User();
    user.setUserId(userDTO.userId());
    user.setVideoId(userDTO.videoId());
    return user;

  }
}
