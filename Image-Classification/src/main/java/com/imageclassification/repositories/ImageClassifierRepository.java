package com.imageclassification.repositories;

import com.imageclassification.models.ImageClassifierEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ImageClassifierRepository extends JpaRepository<ImageClassifierEntity, Long> {
    Optional<ImageClassifierEntity> findByImageClassifierName(String name);
}
