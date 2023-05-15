package com.imageclassification.services;

import com.imageclassification.models.Image;
import com.imageclassification.models.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Set;

public interface ImageService {
    boolean validateImage(String imageUrl);
    List<Integer> getImageWidthAndHeight(String imageUrl) throws Exception;

    Image getImageTags(String imageUrl, boolean noCache);

    Image getImageById(Long id);
    List<Image> getAllImages();
    Page<Image> getAllImagesPaged(Pageable pageRequest);
    List<Image> getAllImagesWithTags(Collection<String> tags);
}
