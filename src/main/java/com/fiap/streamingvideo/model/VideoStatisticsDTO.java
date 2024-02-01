package com.fiap.streamingvideo.model;

public record VideoStatisticsDTO(
    long totalVideos,
    long totalFavorited,
    double averageViews
) {

}

