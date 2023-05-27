package com.imageclassification.services;

import com.imageclassification.models.Tag;

import java.util.List;

public interface TagService {
    List<Tag> getAllTags();

    List<Tag> getTagsStartingWith(String prefix);
}
