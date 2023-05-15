package com.imageclassification.services;

import com.imageclassification.models.Image;
import com.imageclassification.models.Tag;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Set;

public interface ImageService {
    boolean validateImage(String imageUrl);
    List<Integer> getImageWidthAndHeight(String imageUrl) throws Exception;
    Image getImageTags(String imageUrl);
    Image getImageById(Long id);
    List<Image> getAllImages();
    List<Image> getAllImagesWithTags(Collection<String> tags);
}
