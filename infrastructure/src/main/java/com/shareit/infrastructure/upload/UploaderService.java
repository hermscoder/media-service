package com.shareit.infrastructure.upload;

import com.shareit.infrastructure.upload.exception.FailedDestructionException;
import com.shareit.infrastructure.upload.exception.FailedUploadException;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

public interface UploaderService {
    Map upload(MultipartFile multipartFile, Map options) throws FailedUploadException;
    Map destroy(String publicId, Map options) throws FailedDestructionException;
    public List<UploadedMedia> uploadParallel(MultipartFile[] multipartFiles, Map options) throws FailedUploadException;
}
