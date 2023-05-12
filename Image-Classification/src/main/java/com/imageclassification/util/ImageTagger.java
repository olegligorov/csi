package com.imageclassification.util;

import com.imageclassification.dtos.TagDTO;
import com.imageclassification.models.Tag;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

public interface ImageTagger {
    Map<String, Double> getImageTags(String imageUrl) throws IOException;
    String getServiceName();
}