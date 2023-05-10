package com.imageclassification.controllers;

import com.imageclassification.models.Image;
import com.imageclassification.models.Tag;
import com.imageclassification.services.ImageService;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.time.Duration;
import java.time.LocalDateTime;

@RestController
//@RequestMapping("/csi/api/v1/images")
@RequestMapping("/images")
public class ImageController {
    private final ImageService imageService;
    private final Bucket bucket;
    private static final int REQUESTS_PER_MINUTE = 5;
    private static final int REQUESTS_REFILL_TIMER = 1;

    @Autowired
    public ImageController(ImageService imageService) {
        this.imageService = imageService;

        // Create Throttling bucket that allows only REQUESTS_PER_MINUTE requests every minute (REQUESTS_REFILL_TIMER)
        Bandwidth limit = Bandwidth.classic(REQUESTS_PER_MINUTE, Refill.greedy(REQUESTS_PER_MINUTE, Duration.ofMinutes(REQUESTS_REFILL_TIMER)));
        this.bucket = Bucket.builder()
                .addLimit(limit)
                .build();
    }

    @PostMapping
    public ResponseEntity<?> getImageTags(@RequestParam("imageUrl") String imageUrl) {
        if (!bucket.tryConsume(1)) {
            throw new ResponseStatusException(HttpStatus.TOO_MANY_REQUESTS, "Maximum of 5 requests per minutes is succeeded, please try again in 1 minute");
        }
        Image createdImage = imageService.getImageTags(imageUrl);
        createdImage.setAnalysedAt(LocalDateTime.now());

        return ResponseEntity.created(
                ServletUriComponentsBuilder.fromCurrentRequest().buildAndExpand().toUri()
        ).body(createdImage);
    }
}