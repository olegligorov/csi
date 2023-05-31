package com.imageclassification.controllers;

import com.imageclassification.dtos.ImageDTO;
import com.imageclassification.models.Image;
import com.imageclassification.services.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@RestController
@CrossOrigin
//@RequestMapping("/ics/api/v1/images")
@RequestMapping("/images")
public class ImageController {
    private final ImageService imageService;

    @Autowired
    public ImageController(ImageService imageService) {
        this.imageService = imageService;
    }

    @PostMapping
    public ResponseEntity<?> fetchImageTags(@RequestBody ImageDTO imageDTO, @RequestParam(name = "noCache", required = false, defaultValue = "false") boolean noCache) {
        Image createdImage = imageService.getImageTags(imageDTO.getUrl(), noCache);

        return ResponseEntity.created(
                ServletUriComponentsBuilder.fromCurrentRequest().buildAndExpand().toUri()
        ).body(createdImage);
    }

    @GetMapping("/{imageId:\\d+}")
    public ResponseEntity<?> getImage(@PathVariable("imageId") Long imageId) {
        Image image = imageService.getImageById(imageId);
        return ResponseEntity.ok(image);
    }

//    @GetMapping("/test")
//    public ResponseEntity<?> testing() {
//        return ResponseEntity.ok("works");
//    }

//    @GetMapping
//    public List<Image> getAllImages() {
//        return imageService.getAllImages();
//    }

    @GetMapping
    public ResponseEntity<List<?>> getAllImagesPaged(
            @RequestParam(name = "tags", required = false) Collection<String> tags,
            @RequestParam(name = "order", defaultValue = "desc") String order,
            @RequestParam(name = "pageNumber", defaultValue = "0") int pageNumber,
            @RequestParam(name = "pageSize", defaultValue = "20") int pageSize) {

        if (tags != null) {
            return ResponseEntity.ok(imageService.getAllImagesWithTags(tags));
        }

        if (pageNumber == 0 && pageSize == 0) {
            return ResponseEntity.ok(imageService.getAllImages());
        }

        validateParameters(order, pageNumber, pageSize);

        Sort.Direction direction = Sort.Direction.DESC;
        if (order.equalsIgnoreCase("asc")) {
            direction = Sort.Direction.ASC;
        }

        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize, direction, "analysedAt");
        Page<Image> imagePage = imageService.getAllImagesPaged(pageRequest);
        return ResponseEntity.ok(imagePage.getContent());
    }

    private void validateParameters(String order, int pageNumber, int pageSize) {
        if (!order.equalsIgnoreCase("desc") && !order.equalsIgnoreCase("asc")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid order parameter, order should be asc or desc");
        }
        if (pageNumber < 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Page number can not be less than 0");
        }
        if (pageSize < 1) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Page size can not be less than 1");
        }
    }
}