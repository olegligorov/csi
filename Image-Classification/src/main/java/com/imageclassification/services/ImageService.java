package com.imageclassification.services;

import com.imageclassification.dtos.TagDTO;

import java.io.IOException;
import java.util.List;

public interface ImageService {
    boolean validateImage(String imageUrl);
    List<Integer> getImageWidthAndHeight(String imageUrl);
    List<TagDTO> getImageTags(String imageUrl) throws IOException;
}
