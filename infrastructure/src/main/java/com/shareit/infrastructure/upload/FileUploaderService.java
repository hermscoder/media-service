package com.shareit.infrastructure.upload;

import com.shareit.infrastructure.integration.CloudinaryManager;
import com.shareit.infrastructure.upload.exception.FailedDestructionException;
import com.shareit.infrastructure.upload.exception.FailedUploadException;
import com.shareit.utils.commons.exception.InvalidParameterException;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

//TODO unit tests
@Service
public class FileUploaderService implements UploaderService {

    private final CloudinaryManager cloudinaryManager;

    public FileUploaderService(CloudinaryManager cloudinaryManager) {
        this.cloudinaryManager = cloudinaryManager;
    }

    public UploadedMedia upload(MultipartFile multipartFile, Map options) throws FailedUploadException {
        try {
            Map uploadResponse = cloudinaryManager.upload(multipartFile.getBytes(), options);
            return new UploadedMedia(
                    multipartFile.getOriginalFilename(),
                    uploadResponse.getOrDefault("url", "").toString(),
                    uploadResponse.getOrDefault("public_id", "").toString());
        } catch (IOException e) {
            throw new FailedUploadException(e.getMessage());
        }
    }

    @Override
    public List<UploadedMedia> uploadParallel(MultipartFile[] multipartFiles, Map options) throws FailedUploadException {
        UploadResult uploadResult = new UploadResult();

        if(multipartFiles == null || multipartFiles.length == 0){
            throw new InvalidParameterException("files", "Unable to upload. No files were provided.");
        } else if(multipartFiles.length == 1) {
            validateSingleMultipartFile(multipartFiles[0]);
        }

        Arrays.asList(multipartFiles).parallelStream().forEach(multipartFile -> {
            try {
                validateSingleMultipartFile(multipartFile);

                uploadResult.addUploadedMedia(upload(multipartFile, options));
            } catch (Exception e) {
                uploadResult.addUploadError(multipartFile.getOriginalFilename(), e.getMessage());
            }
        });

        if(uploadResult.hasErrors()) {
            destroyUploadedMedias(uploadResult.getUploadedMedias().stream().map(UploadedMedia::getPublicId).collect(Collectors.toList()));
            throw new FailedUploadException("Upload failed", uploadResult.getUploadErrors());
        }

        return uploadResult.getUploadedMedias();
    }

    private void validateSingleMultipartFile(MultipartFile multipartFile){
        if (multipartFile.isEmpty()) {
            throw new InvalidParameterException("files", String.format("Unable to upload. An empty file (%s) was provided.", multipartFile.getOriginalFilename()));
        } else if (multipartFile.getSize() > cloudinaryManager.getCloudinarySettings().getMaxFileSize()) {
            throw new InvalidParameterException("files", String.format("Unable to upload. File (%s) size too large. More than %.0f Mbs", multipartFile.getOriginalFilename(), cloudinaryManager.getCloudinarySettings().getMaxFileSize()/1000000.0));
        }
    }

    @Async
    public CompletableFuture<Void> destroyUploadedMedias(List<String> publicIds) {
        return CompletableFuture.runAsync(() -> publicIds.forEach(publicId -> destroy(publicId)));
    }

    public Map destroy(String publicId) throws FailedDestructionException {
        return destroy(publicId, Collections.emptyMap());
    }
    public Map destroy(String publicId, Map options) throws FailedDestructionException {
        try {
            return cloudinaryManager.destroy(publicId, options);
        } catch (IOException e) {
            throw new FailedDestructionException(e.getMessage());
        }
    }
}
