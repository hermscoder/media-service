package com.shareit.infrastructure.upload;

import java.util.ArrayList;
import java.util.List;

public class UploadResult {

    private final List<UploadError> uploadErrors;
    private final List<UploadedMedia> uploadedMedias;

    public UploadResult() {
        uploadErrors = new ArrayList<>();
        uploadedMedias = new ArrayList<>();
    }

    public UploadResult(List<UploadError> uploadErrors, List<UploadedMedia> uploadedMedias) {
        this.uploadErrors = uploadErrors;
        this.uploadedMedias = uploadedMedias;
    }

    public void addUploadedMedia(String fileName, String url, String public_id) {
        uploadedMedias.add(new UploadedMedia(fileName, url, public_id));
    }

    public void addUploadError(String fileName, String errorMsg) {
        uploadErrors.add(new UploadError(fileName, errorMsg));
    }

    public List<UploadError> getUploadErrors() {
        return uploadErrors;
    }

    public List<UploadedMedia> getUploadedMedias() {
        return uploadedMedias;
    }

    public boolean hasErrors() {
        return uploadErrors.size() > 0;
    }
}
