package com.shareit.infrastructure.upload;

public class UploadedMedia {
    private String fileName;
    private String url;
    private String publicId;
    public UploadedMedia(String fileName, String url, String publicId) {
        this.fileName = fileName;
        this.url = url;
        this.publicId = publicId;
    }

    public String getFileName() {
        return fileName;
    }

    public String getUrl() {
        return url;
    }

    public String getPublicId() {
        return publicId;
    }
}
