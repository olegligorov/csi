package com.imageclassification.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "image_classifiers", uniqueConstraints = @UniqueConstraint(columnNames = {"image_classifier_name"}))
public class ImageClassifierEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "classifier_id")
    private Long id;

    @Column(name = "image_classifier_name")
    private String imageClassifierName;

    private int currentRequests;
    private int classifierLimit;

    public ImageClassifierEntity(String imageClassifierName, int currentRequests, int limit) {
        this.imageClassifierName = imageClassifierName;
        this.currentRequests = currentRequests;
        this.classifierLimit = limit;
    }
}
