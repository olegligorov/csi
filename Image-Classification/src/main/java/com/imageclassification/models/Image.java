package com.imageclassification.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Objects;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
//@RequiredArgsConstructor
//@AllArgsConstructor
@Table(name = "images", uniqueConstraints = @UniqueConstraint(columnNames = {"url"}))
public class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "image_id")
    private Long id;

    @NonNull
    private String url;
    private LocalDateTime analysedAt;
    private String analysedByService;

    //    @ManyToMany(cascade = CascadeType.ALL)
//    @JoinTable(name = "image_tag",
//            joinColumns = @JoinColumn(name = "image_id"),
//            inverseJoinColumns = @JoinColumn(name = "tag_id")
//    )
    @OneToMany(mappedBy = "image", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ImageTag> imageTags = new HashSet<>();

    private int width;
    private int height;

    public Image(String url, String analysedByService, Set<ImageTag> imageTags, int width, int height) {
        this.url = url;
        this.analysedByService = analysedByService;
        this.imageTags = imageTags;
        this.width = width;
        this.height = height;
        analysedAt = LocalDateTime.now();
    }

    public void addTag(Tag tag, double confidenceRate) {
        ImageTag imageTag = new ImageTag(this, tag, confidenceRate);
        imageTags.add(imageTag);
        tag.getImageTags().add(imageTag);
    }

    public void removeTag(Tag tag) {
        for (Iterator<ImageTag> iterator = imageTags.iterator(); iterator.hasNext(); ) {
            ImageTag imageTag = iterator.next();
            if (imageTag.getImage().equals(this) && imageTag.getTag().equals(tag)) {
                iterator.remove();
                imageTag.getTag().getImageTags().remove(imageTag);
                imageTag.setImage(null);
                imageTag.setTag(null);
            }
        }
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
}
