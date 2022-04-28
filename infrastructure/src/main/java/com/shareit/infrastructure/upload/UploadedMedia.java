package com.shareit.infrastructure.upload;

import java.util.Objects;

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

    @Override
    public String toString() {
        return "UploadedMedia{" +
                "fileName='" + fileName + '\'' +
                ", url='" + url + '\'' +
                ", publicId='" + publicId + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UploadedMedia that = (UploadedMedia) o;
        return Objects.equals(fileName, that.fileName) && Objects.equals(url, that.url) && Objects.equals(publicId, that.publicId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fileName, url, publicId);
    }
}
