package com.imageclassification.services;

import com.imageclassification.models.ImageTaggerEntity;

import java.util.List;

public interface ImageTaggerService {
    List<ImageTaggerEntity> getAllTaggerEntities();
}
