package com.imageclassification.repositories;

import com.imageclassification.models.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TagRepository extends JpaRepository<Tag, Long> {
    Optional<Tag> findByTag(String tagName);
    List<Tag> findByTagStartingWith(String prefix);
}
