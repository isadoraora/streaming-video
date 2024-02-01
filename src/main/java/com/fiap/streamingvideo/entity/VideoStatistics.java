package com.fiap.streamingvideo.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class VideoStatistics {
  private long totalVideos;
  private long totalFavorited;
  private double averageViews;
}
