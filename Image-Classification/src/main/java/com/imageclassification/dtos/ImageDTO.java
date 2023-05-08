package com.imageclassification.dtos;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class ImageDTO {
    private String url;
    private LocalDateTime analysedAt;
    private List<TagDTO> tags;
    private int width;
    private int height;

    public ImageDTO(String url, List<TagDTO> tags, int width, int height) {
        this.url = url;
        this.tags = tags;
        this.width = width;
        this.height = height;
        analysedAt = LocalDateTime.now();
    }

}
