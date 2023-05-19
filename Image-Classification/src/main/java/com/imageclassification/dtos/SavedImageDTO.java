package com.imageclassification.dtos;

public class SavedImageDTO {
    private String checksum;
    private String savedPath;

    public SavedImageDTO(String checksum, String savedPath) {
        this.checksum = checksum;
        this.savedPath = savedPath;
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
}
