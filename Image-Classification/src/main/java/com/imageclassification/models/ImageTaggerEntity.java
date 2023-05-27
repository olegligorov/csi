package com.imageclassification.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "image_taggers", uniqueConstraints = @UniqueConstraint(columnNames = {"image_tagger_name"}))
public class ImageTaggerEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "image_tagger_id")
    private Long id;

    @Column(name = "image_tagger_name")
    private String imageTaggerName;

    private int currentRequests;
    private int taggerLimit;

    private LocalDateTime lastUsed;

    @OneToMany(mappedBy = "analysedByService")
    @JsonIgnore
    private Set<Image> analysedImages;

    public ImageTaggerEntity(String imageTaggerName, int currentRequests, int limit) {
        this.imageTaggerName = imageTaggerName;
        this.currentRequests = currentRequests;
        this.taggerLimit = limit;
        lastUsed = LocalDateTime.now();
    }
}
