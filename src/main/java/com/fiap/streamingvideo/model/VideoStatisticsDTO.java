package com.fiap.streamingvideo.model;

public record VideoStatisticsDTO(
    Integer totalVideos,
    Integer totalFavorited,
    Double averageViews
) {

}

