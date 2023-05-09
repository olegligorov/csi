package com.imageclassification.services.impl;

import com.imageclassification.dtos.TagDTO;
import com.imageclassification.services.ImageService;
import com.imageclassification.util.ImageTagger;
import com.imageclassification.util.ImaggaIntegration;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

@Service
public class ImageServiceImpl implements ImageService {
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
        // Check if the URL is an image
        final BufferedImage img = ImageIO.read(url);
        int width = img.getWidth();
        int height = img.getHeight();
        return List.of(width, height);

//        return List.of(0, 0);
    }

    @Override
    public List<TagDTO> getImageTags(String imageUrl) throws IOException {
        ImageTagger imageTagger = new ImaggaIntegration();
        return imageTagger.getImageTags(imageUrl);
    }

}
