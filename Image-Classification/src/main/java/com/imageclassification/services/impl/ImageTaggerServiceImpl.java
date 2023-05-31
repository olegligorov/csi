package com.imageclassification.services.impl;

import com.imageclassification.models.ImageTaggerEntity;
import com.imageclassification.repositories.ImageTaggerRepository;
import com.imageclassification.services.ImageTaggerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ImageTaggerServiceImpl implements ImageTaggerService {
    private final ImageTaggerRepository imageTaggerRepository;

    @Autowired
    public ImageTaggerServiceImpl(ImageTaggerRepository imageTaggerRepository) {
        this.imageTaggerRepository = imageTaggerRepository;
    }

    @Override
    public List<ImageTaggerEntity> getAllTaggerEntities() {
        return imageTaggerRepository.findAll();
    }
}
