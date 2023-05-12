package com.imageclassification.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import java.io.Serializable;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ImageTag implements Serializable {
    @EmbeddedId
    private ImageTagId id;

    @JsonIgnore
    @ManyToOne
    @MapsId("imageId")
    private Image image;

    @JsonIgnore
    @ManyToOne
    @MapsId("tagId")
    private Tag tag;

    @Column(name = "confidence_rate")
    private double confidenceRate;

    public ImageTag(Image image, Tag tag, double confidenceRate) {
        this.image = image;
        this.tag = tag;
        this.confidenceRate = confidenceRate;
        this.id = new ImageTagId(image.getId(), tag.getId());
    }
}
