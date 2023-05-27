package com.imageclassification.repositories;

import com.imageclassification.models.ImageTaggerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ImageClassifierRepository extends JpaRepository<ImageTaggerEntity, Long> {
    Optional<ImageTaggerEntity> findByImageTaggerName(String name);
}
