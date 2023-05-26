package com.imageclassification.services.impl;

import com.imageclassification.models.Tag;
import com.imageclassification.repositories.TagRepository;
import com.imageclassification.services.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TagServiceImpl implements TagService {
    private final TagRepository tagRepository;

    @Autowired
    public TagServiceImpl(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    @Override
    public List<Tag> getAllTags() {
        return tagRepository.findAll();
    }

    @Override
    public List<Tag> getTagsStartingWith(String prefix) {
        return tagRepository.findByTagStartingWith(prefix);
    }
}
