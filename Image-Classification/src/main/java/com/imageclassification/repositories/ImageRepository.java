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

//    TODO
//    @Query(value = "select i from Image i join i.imageTags t where t.tag in :tagList group by i having count(t) = :size")
//    List<Image> findImagesByTags(@Param("tagList") Collection<String> tags, @Param("size") int size);
}
