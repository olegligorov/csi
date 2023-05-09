package com.imageclassification.services.impl;

import com.imageclassification.services.ImageService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ImageServiceImplTest {
    private final ImageService imageService =  new ImageServiceImpl();

    @Test
    void validateImageWithValidImage() {

        boolean actual = imageService.validateImage("https://docs.imagga.com/static/images/docs/sample/japan-605234_1280.jpg");

        assertTrue(actual, "The imageUrl should be valid.");
    }

    @Test
    void validateImageWithInvalidImage() {

        boolean actual = imageService.validateImage("https://docs.imagga.com/static/images/docs/sample/jadfdfpan-605234_1280.jpg");

        assertFalse(actual, "The imageUrl should be invalid.");

    }

    @Test
    void getImageWidthAndHeightOfAValidImage() {
        List<Integer> expected = List.of(1280, 850);
        try {
        List<Integer> actual = imageService.getImageWidthAndHeight("https://docs.imagga.com/static/images/docs/sample/japan-605234_1280.jpg");
        assertIterableEquals(expected, actual);
        } catch (Exception e) {
            Assertions.fail("Image with url https://docs.imagga.com/static/images/docs/sample/japan-605234_1280.jpg should have width x height of 1280x850 but it throws exception");
        }
    }

    @Test()
    void getImageWidthAndHeightOfAnInvalidImage() {
        Assertions.assertThrows(Exception.class, () -> {
            imageService.getImageWidthAndHeight("https://docs.imagga.com/static/images/docs/sample/jafdfpan-605234_1280.jpg");
        });
    }
}