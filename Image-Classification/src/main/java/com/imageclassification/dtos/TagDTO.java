package com.imageclassification.dtos;

public class TagDTO {
    private double confidence;
    private String tag;

    public TagDTO(String tag, double confidence) {
        this.confidence = confidence;
        this.tag = tag;
    }

    public double getConfidence() {
        return confidence;
    }

    public void setConfidence(double confidence) {
        this.confidence = confidence;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    @Override
    public String toString() {
        return "TagDTO{" +
                "confidence=" + confidence +
                ", tag='" + tag + '\'' +
                '}';
    }
}
