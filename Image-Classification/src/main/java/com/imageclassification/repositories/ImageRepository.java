package com.imageclassification.repositories;

import com.imageclassification.models.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface ImageRepository extends JpaRepository<Image, Long> {
    Optional<Image> findByUrl(String url);
    Optional<Image> findByChecksum(String url);

    @Query(value = "select * from images " +
            "join image_tag on images.image_id = image_tag.image_id " +
            "join tags on image_tag.tag_id = tags.tag_id where tags.tag in :tagList " +
            "group by images.image_id " +
            "having count(distinct tags.tag_id) = :tagCount", nativeQuery = true)
    List<Image> findImagesByTags(@Param("tagList") Collection<String> tagList, @Param("tagCount") int tagCount);
}
