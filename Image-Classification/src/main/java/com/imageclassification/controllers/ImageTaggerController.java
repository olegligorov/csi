package com.imageclassification.controllers;

import com.imageclassification.models.ImageTaggerEntity;
import com.imageclassification.services.ImageTaggerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/tagger_services")
public class ImageTaggerController {
    private final ImageTaggerService imageTaggerService;

    @Autowired
    public ImageTaggerController(ImageTaggerService imageTaggerService) {
        this.imageTaggerService = imageTaggerService;
    }

    @GetMapping
    public ResponseEntity<List<ImageTaggerEntity>> getAllTaggingServices() {
        return ResponseEntity.ok(imageTaggerService.getAllTaggerEntities());
    }
}
