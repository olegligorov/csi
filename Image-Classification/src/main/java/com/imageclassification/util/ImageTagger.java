package com.imageclassification.util;

import com.imageclassification.dtos.TagDTO;

import java.io.IOException;
import java.util.List;

public interface ImageTagger {
    public List<TagDTO> getImageTags(String imageUrl) throws IOException;
}