package com.fiap.streamingvideo.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@Document
@AllArgsConstructor
public class UserFavorites {
  private String userId;
  private String videoId;
}
