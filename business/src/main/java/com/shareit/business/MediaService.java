package com.shareit.business;

import com.shareit.data.repository.MediaRepository;
import com.shareit.domain.dto.Media;
import com.shareit.domain.entity.MediaEntity;
import com.shareit.domain.mapper.MediaMapper;
import com.shareit.infrastructure.upload.UploadedMedia;
import com.shareit.infrastructure.upload.UploaderService;
import com.shareit.utils.commons.exception.BadRequestException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


@Service
public class MediaService {

    private UploaderService uploaderService;
    private MediaRepository mediaRepository;

    public MediaService(UploaderService uploaderService, MediaRepository mediaRepository) {
        this.uploaderService = uploaderService;
        this.mediaRepository = mediaRepository;
    }

    public List<Media> createMedias(MultipartFile[] multipartFiles) {
        List<Media> mediaList = new ArrayList<>();

        if(multipartFiles == null || multipartFiles.length == 0) {
            throw new BadRequestException("Unable to upload. No files were provided.");
        }

        List<UploadedMedia> uploadedMediaList = uploaderService.uploadParallel(multipartFiles, Collections.emptyMap());


        List<MediaEntity> mediaEntityList = MediaMapper.INSTANCE.toEntityList(uploadedMediaList);
        mediaList.addAll(MediaMapper.INSTANCE.toModeList(mediaRepository.saveAll(mediaEntityList)));

        return mediaList;
    }

    public int setMediaToBeDeleted(Long... ids) {
        return mediaRepository.setMediaToBeDeleted(ids);
    }

    public List<Media> findMediasToBeDeleted() {
        List<Media> mediasToBeDeleted = new ArrayList<>();

        Optional<List<MediaEntity>> allToBeDeletedOptional = mediaRepository.findAllToBeDeleted();

        allToBeDeletedOptional
                .ifPresent(mediaEntities ->
                        mediaEntities.stream().forEach(mediaEntity ->
                                        mediasToBeDeleted.add(MediaMapper.INSTANCE.toModel(mediaEntity))));

        return mediasToBeDeleted;
    }

    public Media findMediaById(Long id) {
        Optional<MediaEntity> mediaOptional = mediaRepository.findById(id);
        if(mediaOptional.isEmpty())
            return null;

        return MediaMapper.INSTANCE.toModel(mediaOptional.get());
    }
}
