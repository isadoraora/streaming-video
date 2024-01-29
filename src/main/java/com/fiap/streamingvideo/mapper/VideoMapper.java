package com.fiap.streamingvideo.mapper;

import com.fiap.streamingvideo.entity.Video;
import com.fiap.streamingvideo.model.VideoDTO;
import org.springframework.stereotype.Component;

@Component
public class VideoMapper {

  public VideoDTO fromEntity(Video video) {
    return new VideoDTO(
        video.getId(),
        video.getTitle(),
        video.getDescription(),
        video.getUrl(),
        video.getPublishDate(),
        video.getCategories(),
        video.getIsFavorite()
    );
  }

  public Video toEntity(VideoDTO videoDTO) {
    Video video = new Video();
    video.setId(videoDTO.id());
    video.setTitle(videoDTO.title());
    video.setDescription(videoDTO.description());
    video.setUrl(videoDTO.url());
    video.setPublishDate(videoDTO.publishDate());
    video.setCategories(videoDTO.categories());
    video.setIsFavorite(videoDTO.isFavorite());
    return video;
  }
}



