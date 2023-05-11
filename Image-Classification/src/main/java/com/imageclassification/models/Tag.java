package com.imageclassification.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
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
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
//@Table(name = "tags", uniqueConstraints = @UniqueConstraint(columnNames = {"tag"}))
@Table(name = "tags")
@NoArgsConstructor
public class Tag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tag_id")
    private Long id;

    @NonNull
    private String tag;

    @NonNull
    private double confidence;

    @JsonIgnore
    @ManyToMany(mappedBy = "imageTags")
    private Set<Image> imageSet = new HashSet<>();

    public Tag(String tag, double confidence) {
        this.confidence = confidence;
        this.tag = tag;
    }
}
