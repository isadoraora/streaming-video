package com.fiap.streamingvideo.model;

import java.time.LocalDateTime;
import java.util.List;

public record VideoDTO(
    String id,
    String title,
    String description,
    String url,
    LocalDateTime publishDate,
    List<String> categories,
    Boolean isFavorite
) {
}
