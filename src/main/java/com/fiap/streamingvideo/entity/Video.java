package com.fiap.streamingvideo.entity;

import java.time.LocalDateTime;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@Document
public class Video {

  @Id
  private String id;

  private String title;
  private String description;
  private String url;
  private LocalDateTime publishDate;
  private List<String> categories;
  private Boolean isFavorite;
  private Long views;
}
