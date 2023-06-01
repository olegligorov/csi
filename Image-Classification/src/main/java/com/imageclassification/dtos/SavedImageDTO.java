package com.imageclassification.dtos;

public class SavedImageDTO {
    private String checksum;
    private String savedPath;
    private byte[] imageContent;
    public SavedImageDTO(byte[] image, String checksum, String savedPath) {
        this.checksum = checksum;
        this.savedPath = savedPath;
        this.imageContent = image;
    }

    public String getChecksum() {
        return checksum;
    }

    public void setChecksum(String checksum) {
        this.checksum = checksum;
    }

    public String getSavedPath() {
        return savedPath;
    }

    public void setSavedPath(String savedPath) {
        this.savedPath = savedPath;
    }

    public byte[] getImageContent() {
        return imageContent;
    }

    public void setImageContent(byte[] imageContent) {
        this.imageContent = imageContent;
    }
}
