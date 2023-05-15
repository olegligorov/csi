package com.imageclassification.repositories;

import com.imageclassification.models.Image;
import com.imageclassification.models.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TagRepository extends JpaRepository<Tag, Long> {
    Optional<Tag> findByTag(String tagName);

}
