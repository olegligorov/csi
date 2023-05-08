package com.imageclassification.controllers;

import com.imageclassification.dtos.ImageDTO;
import com.imageclassification.dtos.TagDTO;
import com.imageclassification.services.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
//@RequestMapping("/csi/api/v1/images")
@RequestMapping("/images")
public class ImageController {

    private final ImageService imageService;

    @Autowired
    public ImageController(ImageService imageService) {
        this.imageService = imageService;
    }

    //    @GetMapping
//    public ImageDTO getImgTags(@RequestParam("imageUrl") String imageUrl) {
//
//        return new ImageDTO(imageUrl);
//    }
//

    //    can also be done with a body instead of @RequestParam
    @PostMapping
    public ResponseEntity<ImageDTO> getImageTags(@RequestParam("imageUrl") String imageUrl) {
        if (!imageService.validateImage(imageUrl)) {
            System.out.println("Invalid URL");
//            TODO create an error message class and return it as a 400 Error
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        var imageDimensions = imageService.getImageWidthAndHeight(imageUrl);
        int imageWidth = imageDimensions.get(0);
        int imageHeight = imageDimensions.get(1);
        List<TagDTO> imageTags = new ArrayList<>();

        try {
            imageTags = imageService.getImageTags(imageUrl);
        } catch (IOException e) {
//            TODO
            e.printStackTrace();
        }

        ImageDTO createdDTO = new ImageDTO(imageUrl, imageTags, imageWidth, imageHeight);

//        TODO response code 200 or 201?
        return ResponseEntity.created(
                ServletUriComponentsBuilder.fromCurrentRequest().buildAndExpand().toUri()
        ).body(createdDTO);
    }

}
