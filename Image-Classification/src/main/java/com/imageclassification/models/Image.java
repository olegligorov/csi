package com.imageclassification.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.MapKeyJoinColumn;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Objects;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "images", uniqueConstraints = @UniqueConstraint(columnNames = {"url", "checksum"}))
public class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "image_id")
    private Long id;

    @NonNull
    @Lob
    private String url;

    @Column(name = "checksum")
    private String checksum;

    private String imagePath;

    private LocalDateTime analysedAt;
    @ManyToOne
    @JoinColumn(name = "image_tagger_id")
//    @JsonIgnore
    private ImageTaggerEntity analysedByService;

    @ElementCollection
    @CollectionTable(name = "image_tag",
            joinColumns = @JoinColumn(name = "image_id"))
    @MapKeyJoinColumn(name = "tag_id")
    @Column(name = "confidence_rate")
    private Map<Tag, Double> tags;

    private int width;
    private int height;

    public Image(String url, ImageTaggerEntity analysedByService, Map<Tag, Double> tags, int width, int height) {
        this.url = url;
        this.analysedByService = analysedByService;
        this.tags = tags;
        this.width = width;
        this.height = height;
        analysedAt = LocalDateTime.now();
    }

    public Image(String url, String checksum, String imagePath, ImageTaggerEntity analysedByService, Map<Tag, Double> tags, int width, int height) {
        this.url = url;
        this.checksum = checksum;
        this.imagePath = imagePath;
        this.analysedByService = analysedByService;
        this.tags = tags;
        this.width = width;
        this.height = height;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Image)) return false;
        Image image = (Image) o;
        return getId().equals(image.getId()) && getUrl().equals(image.getUrl());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getUrl());
    }

    public void addTag(Tag tag, double confidenceRate) {
        tags.put(tag, confidenceRate);
    }
}