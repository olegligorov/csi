package com.imageclassification.dtos;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ImageDTO {
    private String url;

    public ImageDTO(String url) {
        this.url = url;
    }
}
