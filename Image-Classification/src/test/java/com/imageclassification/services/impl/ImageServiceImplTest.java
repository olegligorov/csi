package com.imageclassification.services.impl;

import com.imageclassification.models.Image;
import com.imageclassification.models.ImageTaggerEntity;
import com.imageclassification.models.Tag;
import com.imageclassification.repositories.ImageRepository;
import com.imageclassification.repositories.ImageTaggerRepository;
import com.imageclassification.repositories.TagRepository;
import com.imageclassification.services.ImageService;
import com.imageclassification.util.ImageTagger;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class ImageServiceImplTest {
    @Mock
    private ImageRepository imageRepository;
    @Mock
    private TagRepository tagRepository;
    @Mock
    private ImageTagger imageTagger;
    @Mock
    private ImageTaggerRepository imageTaggerRepository;
    private ImageService imageService;

    private ImageTaggerEntity imageTaggerServiceEntity;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        imageService = new ImageServiceImpl(imageRepository, tagRepository, imageTagger, imageTaggerRepository);
        imageTaggerServiceEntity = new ImageTaggerEntity("Imagga", 1000, 5);
    }

    @Test
    void testValidateImageWithValidImage() {
        boolean actual = imageService.validateImage("https://docs.imagga.com/static/images/docs/sample/japan-605234_1280.jpg");

        assertTrue(actual, "The imageUrl should be valid.");
    }

    @Test
    void testValidateImageWithInvalidImage() {
        boolean actual = imageService.validateImage("https://docs.imagga.com/static/images/docs/sample/jadfdfpan-605234_1280.jpg");

        assertFalse(actual, "The imageUrl should be invalid.");

    }

    @Test
    void testGetImageWidthAndHeightOfAValidImage() {
        List<Integer> expected = List.of(1280, 850);
        try {
        List<Integer> actual = imageService.getImageWidthAndHeight("https://docs.imagga.com/static/images/docs/sample/japan-605234_1280.jpg");
        assertIterableEquals(expected, actual);
        } catch (Exception e) {
            Assertions.fail("Image with url https://docs.imagga.com/static/images/docs/sample/japan-605234_1280.jpg should have width x height of 1280x850 but it throws exception");
        }
    }

    @Test
    void testGetImageWidthAndHeightOfAnInvalidImage() {
        Assertions.assertThrows(Exception.class, () -> {
            imageService.getImageWidthAndHeight("https://docs.imagga.com/static/images/docs/sample/jafdfpan-605234_1280.jpg");
        });
    }

    @Test
    void testGetImageTagsWithCachedImageReturnsCachedImage() {
        String imageUrl = "https://docs.imagga.com/static/images/docs/sample/japan-605234_1280.jpg";
        Image cachedImage = new Image(imageUrl, "checksum", "path", imageTaggerServiceEntity, new HashMap<>(), 800, 600);
        when(imageRepository.findByChecksum(anyString())).thenReturn(Optional.of(cachedImage));
        when(imageTaggerRepository.findByImageTaggerName(anyString())).thenReturn(Optional.of(imageTaggerServiceEntity));

        Image result = imageService.getImageTags(imageUrl, false);

        assertSame(cachedImage, result);
    }

    @Test
    void getImageTagsWithoutCacheReturnsNewlyCreatedImage() throws IOException {
        String imageUrl = "https://docs.imagga.com/static/images/docs/sample/japan-605234_1280.jpg";

        Image savedImage = new Image(imageUrl, "checksum", "savedPath", imageTaggerServiceEntity, new HashMap<>(), 1280, 850);

        when(imageRepository.findByChecksum(anyString())).thenReturn(Optional.empty());
        when(imageTagger.getServiceName()).thenReturn("Imagga");
        when(imageTagger.getImageTags(imageUrl)).thenReturn(new HashMap<>());
        when(imageRepository.save(any(Image.class))).thenReturn(savedImage);
        when(tagRepository.findByTag(anyString())).thenReturn(Optional.empty());
        when(imageTaggerRepository.findByImageTaggerName(anyString())).thenReturn(Optional.of(imageTaggerServiceEntity));
        when(imageTaggerRepository.save(any())).thenReturn(imageTaggerServiceEntity);

        Image result = imageService.getImageTags(imageUrl, true);

        assertSame(savedImage, result, "The image is not the same as the expected one");
        verify(imageRepository, atLeast(1)).save(any(Image.class));
        verify(tagRepository, times(0)).save(any(Tag.class));
    }

    @Test
    void testGetImageByIdOfAnExistingImage() {
        Long imageId = 1L;
        Image expectedImage = new Image("https://docs.imagga.com/static/images/docs/sample/japan-605234_1280.jpg", "checksum", "savedPath", imageTaggerServiceEntity, new HashMap<>(), 1280, 850);
        when(imageRepository.findById(imageId)).thenReturn(Optional.of(expectedImage));
        when(imageTaggerRepository.findByImageTaggerName(anyString())).thenReturn(Optional.of(imageTaggerServiceEntity));

        Image result = imageService.getImageById(imageId);

        assertSame(expectedImage, result, "The image is not the same as the expected");
    }

    @Test
    void testGetImageByIdOfANonExistingImage() {
        Long imageId = 1L;
        when(imageRepository.findById(imageId)).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> imageService.getImageById(imageId), "getImageById should throw an exception with status code 404 when the image is not found");
        verify(imageRepository).findById(imageId);
    }
}