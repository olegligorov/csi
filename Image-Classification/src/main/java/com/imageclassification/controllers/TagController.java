package com.imageclassification.controllers;

import com.imageclassification.models.Tag;
import com.imageclassification.services.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/tags")
public class TagController {
    private final TagService tagService;

    @Autowired
    public TagController(TagService tagService) {
        this.tagService = tagService;
    }

    @GetMapping
    public List<Tag> getAllTags(@RequestParam(name = "prefix", defaultValue = "") String prefix) {
        if (prefix.isBlank()) {
            return tagService.getAllTags();
        }
        return tagService.getTagsStartingWith(prefix);
    }
}
