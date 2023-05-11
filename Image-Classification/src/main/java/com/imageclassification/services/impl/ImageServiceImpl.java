package com.imageclassification.services.impl;

import com.imageclassification.models.Image;
import com.imageclassification.models.Tag;
import com.imageclassification.repositories.ImageRepository;
import com.imageclassification.repositories.TagRepository;
import com.imageclassification.services.ImageService;
import com.imageclassification.util.ImageTagger;
import com.imageclassification.util.ImaggaIntegration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class ImageServiceImpl implements ImageService {
    private final ImageRepository imageRepository;
    private final TagRepository tagRepository;
    private final ImageTagger imageTagger;

    @Autowired
    public ImageServiceImpl(ImageRepository imageRepository, TagRepository tagRepository, ImageTagger imageTagger) {
        this.imageRepository = imageRepository;
        this.tagRepository = tagRepository;
        this.imageTagger = imageTagger;
    }

    @Override
    public boolean validateImage(String imageUrl) {
        try {
            URL url = new URL(imageUrl);
            // Check if the URL is an image
            return ImageIO.read(url) != null;
        } catch (Exception e) {
            return false;
        }
    }

    public List<Integer> getImageWidthAndHeight(String imageUrl) throws Exception {
//      returns Array(Width, Height);
        URL url = new URL(imageUrl);
        final BufferedImage img = ImageIO.read(url);
        int width = img.getWidth();
        int height = img.getHeight();
        return List.of(width, height);
    }

    @Override
    public Set<Tag> getTags(String imageUrl) throws IOException {

//        ImageTagger imageTagger = new ImaggaIntegration();
        return imageTagger.getImageTags(imageUrl);
    }

    @Override
    public Image getImageTags(String imageUrl) {
        if (!validateImage(imageUrl)) {
            //return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid Image URL");
        }

//        Check if the image is already in the database
        Optional<Image> image = imageRepository.findByUrl(imageUrl);
        if (image.isPresent()) {
            return image.get();
        }

        List<Integer> imageDimensions = new ArrayList<>();
        try {
            imageDimensions = getImageWidthAndHeight(imageUrl);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error while trying to get image width and height");
        }

        int imageWidth = imageDimensions.get(0);
        int imageHeight = imageDimensions.get(1);
        Set<Tag> tags = new HashSet<>();
//        ImageTagger imageTagger = new ImaggaIntegration();
        String imageTaggerServiceName = imageTagger.getServiceName();

        try {
            tags = imageTagger.getImageTags(imageUrl);
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error while fetching image tags");
        }

        tagRepository.saveAll(tags);

        Image createdImage = new Image(imageUrl, imageTaggerServiceName, tags, imageWidth, imageHeight);
        return imageRepository.save(createdImage);
    }

    @Override
    public Image getImageById(Long id) {
        return imageRepository.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("Image with id %d was not found", id)));
    }

    @Override
    public List<Image> getAllImages() {
        return imageRepository.findAll();
    }
}
