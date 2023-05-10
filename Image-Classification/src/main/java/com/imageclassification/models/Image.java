package com.imageclassification.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
//@RequiredArgsConstructor
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

    @ManyToMany
    @JoinTable(name = "image_tag",
            joinColumns = @JoinColumn(name = "image_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    private Set<Tag> imageTags = new HashSet<>();

    private int width;
    private int height;

    public Image(String url, Set<Tag> imageTags, int width, int height) {
        this.url = url;
        this.imageTags = imageTags;
        this.width = width;
        this.height = height;
        analysedAt = LocalDateTime.now();
    }

    public Image(String url, String analysedByService, Set<Tag> imageTags, int width, int height) {
        this.url = url;
        this.analysedByService = analysedByService;
        this.imageTags = imageTags;
        this.width = width;
        this.height = height;
        analysedAt = LocalDateTime.now();
    }

}
