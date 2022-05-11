package com.shareit.business;

import com.shareit.data.repository.MediaRepository;
import com.shareit.domain.dto.Media;
import com.shareit.domain.entity.MediaEntity;
import com.shareit.domain.mapper.MediaMapper;
import com.shareit.exception.MediaNotFoundException;
import com.shareit.infrastructure.upload.UploadedMedia;
import com.shareit.infrastructure.upload.UploaderService;
import com.shareit.utils.commons.exception.InvalidParameterException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;


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
            throw new InvalidParameterException("files", "Unable to upload. No files were provided.");
        }

        List<UploadedMedia> uploadedMediaList = uploaderService.uploadParallel(multipartFiles, Collections.emptyMap());


        List<MediaEntity> mediaEntityList = MediaMapper.INSTANCE.toEntityList(uploadedMediaList);
        mediaList.addAll(MediaMapper.INSTANCE.toModelList(mediaRepository.saveAll(mediaEntityList)));

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

    //TODO write test for this method
    public Media updateMedia(Long mediaId, MultipartFile file) {

        Optional<MediaEntity> existingMediaOptional = mediaRepository.findById(mediaId);

        if(existingMediaOptional.isEmpty()) {
            throw new MediaNotFoundException(String.format("Media %s could notbe found", mediaId));
        }
        MediaEntity existingMedia = existingMediaOptional.get();

        UploadedMedia uploadedMedia = uploaderService.upload(file, Collections.emptyMap());
        mediaRepository.updateMediaUploadInformation(mediaId, uploadedMedia.getPublicId(), uploadedMedia.getUrl());
        Media media = MediaMapper.INSTANCE.toModel(existingMedia);
        media.setUrl(uploadedMedia.getUrl());

        uploaderService.destroyUploadedMedias(Arrays.asList(existingMedia.getPublicId()));
        return media;
    }
}
