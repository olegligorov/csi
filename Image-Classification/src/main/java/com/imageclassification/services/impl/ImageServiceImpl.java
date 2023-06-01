package com.imageclassification.services.impl;

import com.imageclassification.dtos.SavedImageDTO;
import com.imageclassification.models.Image;
import com.imageclassification.models.ImageTaggerEntity;
import com.imageclassification.models.Tag;
import com.imageclassification.repositories.ImageRepository;
import com.imageclassification.repositories.ImageTaggerRepository;
import com.imageclassification.repositories.TagRepository;
import com.imageclassification.services.ImageService;
import com.imageclassification.util.ImageTagger;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import org.springframework.web.server.ResponseStatusException;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class ImageServiceImpl implements ImageService {
    private final ImageRepository imageRepository;
    private final TagRepository tagRepository;
    private final ImageTagger imageTagger;
    private final ImageTaggerRepository imageTaggerRepository;

    private final Bucket bucket;

    private static final String UPLOADS_DIRECTORY = "." + File.separator + "uploads";

    private static final int REQUESTS_PER_MINUTE = 5;
    private static final int REQUESTS_REFILL_TIMER = 1;


    @Autowired
    public ImageServiceImpl(ImageRepository imageRepository, TagRepository tagRepository, ImageTagger imageTagger, ImageTaggerRepository imageTaggerRepository) {
        this.imageRepository = imageRepository;
        this.tagRepository = tagRepository;
        this.imageTagger = imageTagger;
        this.imageTaggerRepository = imageTaggerRepository;

        /**
         * Create Throttling bucket that allows only REQUESTS_PER_MINUTE requests every minute (REQUESTS_REFILL_TIMER)
         */
        Bandwidth limit = Bandwidth.classic(REQUESTS_PER_MINUTE, Refill.greedy(REQUESTS_PER_MINUTE, Duration.ofMinutes(REQUESTS_REFILL_TIMER)));
        this.bucket = Bucket.builder()
                .addLimit(limit)
                .build();
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
        /**
         returns Array(Width, Height);
         */
        URL url = new URL(imageUrl);
        final BufferedImage img = ImageIO.read(url);
        int width = img.getWidth();
        int height = img.getHeight();
        return List.of(width, height);
    }

    @Override
    public Image getImageTags(String imageUrl, boolean noCache) {
        if (!validateImage(imageUrl)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid Image URL");
        }
//      if noCache is false then we should check if the image is already in the database
        boolean imageIsPresent = false;
        Image createdImage = null;

        String imageChecksum;
        String imagePath;
        try {
            SavedImageDTO savedImageDTO = saveImageAndCalculateChecksum(imageUrl);
            imageChecksum = savedImageDTO.getChecksum();
            imagePath = savedImageDTO.getSavedPath();
        } catch (IOException e) {
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error while downloading image");
        }

        Optional<Image> image = imageRepository.findByChecksum(imageChecksum);
        if (image.isPresent()) {
            createdImage = image.get();
            imageIsPresent = true;
            if (!noCache) {
                return createdImage;
            }
        } else if (!noCache) {
            image = imageRepository.findByUrl(imageUrl);
            if (image.isPresent()) {
                return image.get();
            }
        }

        List<Integer> imageDimensions = new ArrayList<>();
        try {
            imageDimensions = getImageWidthAndHeight(imageUrl);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error while trying to get image width and height");
        }

        int imageWidth = imageDimensions.get(0);
        int imageHeight = imageDimensions.get(1);

        if (!bucket.tryConsume(1)) {
            throw new ResponseStatusException(HttpStatus.TOO_MANY_REQUESTS, "Maximum of 5 requests per minutes is succeeded, please try again in 1 minute");
        }
        String imageTaggerServiceName = imageTagger.getServiceName();
        ImageTaggerEntity imageTaggerServiceEntity = incrementTaggerServiceRequests(imageTaggerServiceName);

        Map<String, Double> tags = new HashMap<>();
        try {
            tags = imageTagger.getImageTags(imageUrl);
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Error while fetching image tags");
        }

        Map<Tag, Double> tagMap = new HashMap<>();
        if (imageIsPresent == false) {
            createdImage = imageRepository.save(new Image(imageUrl, imageChecksum, imagePath, imageTaggerServiceEntity, tagMap, imageWidth, imageHeight));
        }

        for (String tag : tags.keySet()) {
            Optional<Tag> existingTag = tagRepository.findByTag(tag);
            double confidence = tags.get(tag);
            if (existingTag.isEmpty()) {
                var createdTag = tagRepository.save(new Tag(tag));
                createdImage.addTag(createdTag, confidence);
            } else {
                createdImage.addTag(existingTag.get(), confidence);
            }
        }

        createdImage.setAnalysedAt(LocalDateTime.now());
        createdImage.setAnalysedByService(imageTaggerServiceEntity);
        imageTaggerServiceEntity.setLastUsed(LocalDateTime.now());
        imageTaggerRepository.save(imageTaggerServiceEntity);
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

    @Override
    public List<Image> getAllImagesSorted(Sort sort) {
        return imageRepository.findAll(sort);
    }


    @Override
    public Page<Image> getAllImagesPaged(Pageable pageRequest) {
        return imageRepository.findAll(pageRequest);
    }

    @Override
    public List<Image> getAllImagesWithTags(Collection<String> tags) {
        var tagSet = new HashSet<>(tags);
        return imageRepository.findImagesByTags(tagSet, tagSet.size());
    }

    private SavedImageDTO saveImageAndCalculateChecksum(String imageUrl) throws IOException {
        URL url = new URL(imageUrl);
        String filename = url.getFile();

        String clearedName = filename.substring(filename.lastIndexOf('/') + 1);

        int firstIndexOfParam = clearedName.indexOf('?');
        if (firstIndexOfParam != -1) {
            String extension = clearedName.substring(clearedName.lastIndexOf('.'), clearedName.indexOf('?'));
            String name = clearedName.substring(0, clearedName.lastIndexOf('.')) + clearedName.substring(clearedName.indexOf('?'));
            clearedName = name + extension;
        }
//        since those characters are not allowed as a file name, the name has to be cleared
        clearedName = clearedName.replaceAll("[<>:\\\\|/?*\"]", "_");
        byte[] image = downloadImage(imageUrl, url, clearedName);
        String checksum = calculateChecksum(image);

        return new SavedImageDTO(checksum, UPLOADS_DIRECTORY + File.separator + clearedName);
    }

    private byte[] downloadImage(String imageUrl, URL url, String originalFileName) throws IOException {
        try (InputStream inputStream = url.openStream()) {
            File uploads = new File(UPLOADS_DIRECTORY);

            if (!uploads.exists()) {
                uploads.mkdirs();
            }

            Path file;
            if (!Files.exists(Path.of(UPLOADS_DIRECTORY + File.separator + originalFileName))) {
                file = Files.createFile(Path.of(UPLOADS_DIRECTORY + File.separator + originalFileName));
            } else {
                file = Path.of(UPLOADS_DIRECTORY + File.separator + originalFileName);
            }

            Files.copy(inputStream, file, StandardCopyOption.REPLACE_EXISTING);
            return Files.readAllBytes(file);
        }
    }

    private String calculateChecksum(byte[] data) {
        return DigestUtils.md5DigestAsHex(data);
    }

    private ImageTaggerEntity incrementTaggerServiceRequests(String imageTaggerServiceName) {
        Optional<ImageTaggerEntity> imageClassifierEntity = imageTaggerRepository.findByImageTaggerName(imageTaggerServiceName);
        ImageTaggerEntity imageClassifier = null;

        if (imageClassifierEntity.isPresent()) {
            imageClassifier = imageClassifierEntity.get();
        } else {
            int imageTaggerLimit = imageTagger.getServiceLimit();
            imageClassifier = imageTaggerRepository.save(new ImageTaggerEntity(imageTaggerServiceName, 0, imageTaggerLimit));
        }

        int currentRequests = imageClassifier.getCurrentRequests();
        imageClassifier.setCurrentRequests(currentRequests + 1);
        return imageTaggerRepository.save(imageClassifier);
    }
}