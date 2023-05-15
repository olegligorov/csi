package com.imageclassification.controllers;

import com.imageclassification.models.Image;
import com.imageclassification.models.Tag;
import com.imageclassification.services.ImageService;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

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
//    TODO
    public ResponseEntity<?> fetchImageTags(@RequestParam("imageUrl") String imageUrl, @RequestParam(name = "noCache", required = false, defaultValue = "false") boolean noCache) {
        System.out.println("No cache is:" + noCache);
        if (!bucket.tryConsume(1)) {
            throw new ResponseStatusException(HttpStatus.TOO_MANY_REQUESTS, "Maximum of 5 requests per minutes is succeeded, please try again in 1 minute");
        }
        Image createdImage = imageService.getImageTags(imageUrl, noCache);
        createdImage.setAnalysedAt(LocalDateTime.now());

        return ResponseEntity.created(
                ServletUriComponentsBuilder.fromCurrentRequest().buildAndExpand().toUri()
        ).body(createdImage);
    }

    @GetMapping("/{imageId:\\d+}")
    public Image getImage(@PathVariable("imageId") Long imageId) {
        Image image = imageService.getImageById(imageId);
        return image;
    }

//    @GetMapping
//    public List<Image> getAllImages() {
//        return imageService.getAllImages();
//    }

    @GetMapping
    public List<Image> getAllImages(@RequestParam(name = "order", defaultValue = "desc") String order,
                                    @RequestParam(name = "pageNumber", defaultValue = "0") int pageNumber,
                                    @RequestParam(name = "pageSize", defaultValue = "20") int pageSize) {
        int offset = pageNumber * pageSize;

        Sort.Direction direction = Sort.Direction.DESC;
        if (order.equalsIgnoreCase("asc")) {
            direction = Sort.Direction.ASC;
        }

        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize, direction, "analysedAt");
        Page<Image> imagePage = imageService.getAllImagesPaged(pageRequest);
        return imagePage.getContent();
    }

    @GetMapping("/tags")
    public List<Image> getAllImagesWithTags(@RequestParam(value = "tags") Collection<String> tags) {
        return imageService.getAllImagesWithTags(tags);
    }
}