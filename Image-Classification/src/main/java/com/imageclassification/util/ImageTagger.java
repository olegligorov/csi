package com.imageclassification.util;

import com.imageclassification.dtos.TagDTO;
import com.imageclassification.models.Tag;

import java.io.IOException;
import java.util.Set;

public interface ImageTagger {
    Set<Tag> getImageTags(String imageUrl) throws IOException;
    String getServiceName();
}