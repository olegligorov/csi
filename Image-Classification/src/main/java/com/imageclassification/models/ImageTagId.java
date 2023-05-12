package com.imageclassification.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
public class ImageTagId implements Serializable {
    @Column(name = "image_id")
    private Long imageId;

    @Column(name = "tag_id")
    private Long tagId;

}
