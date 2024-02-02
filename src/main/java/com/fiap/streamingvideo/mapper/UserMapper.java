package com.fiap.streamingvideo.mapper;

import com.fiap.streamingvideo.entity.User;
import com.fiap.streamingvideo.model.UserDTO;

public class UserMapper {

  public static UserDTO fromEntity(User user) {
    return new UserDTO(
        user.getUserId(),
        user.getName(),
        user.getCpf(),
        user.getEmail());
  }

  public static User toEntity(UserDTO userDTO) {
    User user = new User();
    user.setUserId(userDTO.id());
    user.setName(userDTO.name());
    user.setCpf(userDTO.cpf());
    user.setEmail(userDTO.email());
    return user;

  }
}
