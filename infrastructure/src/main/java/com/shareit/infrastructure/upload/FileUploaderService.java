package com.shareit.infrastructure.upload;

import com.shareit.infrastructure.integration.CloudinaryManager;
import com.shareit.infrastructure.upload.exception.FailedDestructionException;
import com.shareit.infrastructure.upload.exception.FailedUploadException;
import com.shareit.utils.commons.exception.BadRequestException;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.CompletableFuture;

@Service
public class FileUploaderService implements UploaderService {

    private final CloudinaryManager cloudinaryManager;

    public FileUploaderService(CloudinaryManager cloudinaryManager) {
        this.cloudinaryManager = cloudinaryManager;
    }

    public Map upload(MultipartFile multipartFile, Map options) throws FailedUploadException {
        try {
            return cloudinaryManager.upload(multipartFile.getBytes(), options);
        } catch (IOException e) {
            throw new FailedUploadException(e.getMessage());
        }
    }

    @Override
    public List<UploadedMedia> uploadParallel(MultipartFile[] multipartFiles, Map options) throws FailedUploadException {
        UploadResult uploadResult = new UploadResult();

        if(multipartFiles == null || multipartFiles.length == 0 || multipartFiles[0].getSize() == 0) {
            throw new BadRequestException("Unable to upload. No files were provided.");
        }

        Arrays.asList(multipartFiles).parallelStream().forEach(multipartFile -> {
            try {
                if (multipartFile.isEmpty()) {
                    throw new BadRequestException(String.format("Unable to upload. An empty file (%s) was provided.", multipartFile.getOriginalFilename()));
                } else if (multipartFile.getSize() > cloudinaryManager.getCloudinarySettings().getMaxFileSize()) {
                    throw new BadRequestException(String.format("Unable to upload. File (%s) size too large. More than %.0f Mbs", multipartFile.getOriginalFilename(), cloudinaryManager.getCloudinarySettings().getMaxFileSize()/1000000.0));
                }

                Map upload = cloudinaryManager.upload(multipartFile.getBytes(), options);
                uploadResult.addUploadedMedia(
                        multipartFile.getOriginalFilename(),
                        upload.getOrDefault("url", "").toString(),
                        upload.getOrDefault("public_id", "").toString());
            } catch (Exception e) {
                uploadResult.addUploadError(multipartFile.getOriginalFilename(), e.getMessage());
            }
        });

        if(uploadResult.hasErrors()) {
            destroyUploadedMedias(uploadResult.getUploadedMedias());
            throw new FailedUploadException("Upload failed", uploadResult.getUploadErrors());
        }

        return uploadResult.getUploadedMedias();
    }

    @Async
    CompletableFuture<Void> destroyUploadedMedias(List<UploadedMedia> uploadedMedias) {
        return CompletableFuture.runAsync(() -> uploadedMedias.forEach(uploadedMedia -> destroy(uploadedMedia.getPublicId(), Collections.emptyMap())));
    }

    public Map destroy(String publicId, Map options) throws FailedDestructionException {
        try {
            return cloudinaryManager.destroy(publicId, options);
        } catch (IOException e) {
            throw new FailedDestructionException(e.getMessage());
        }
    }
}
