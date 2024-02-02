package com.fiap.streamingvideo.entity;

import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@Document
@RequiredArgsConstructor
@AllArgsConstructor
public class Video {

  @Id
  private String id;

  private String title;
  private String description;
  private String url;
  private LocalDateTime publishDate;
  private List<String> categories;
  private Boolean isFavorite;
  private Long views = 0L;

  public Video(String title, String description, String url,
               LocalDateTime publishDate, List<String> categories,
               boolean isFavorite) {
    this.title = title;
    this.description = description;
    this.url = url;
    this.publishDate = publishDate;
    this.categories = categories;
    this.isFavorite = isFavorite;
  }
}
