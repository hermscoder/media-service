package com.shareit.domain.mapper;

import com.shareit.domain.dto.Media;
import com.shareit.domain.entity.MediaEntity;
import com.shareit.domain.entity.MediaType;
import com.shareit.infrastructure.upload.UploadedMedia;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Mapper
public interface MediaMapper {
    MediaMapper INSTANCE = Mappers.getMapper(MediaMapper.class);

    default List<MediaEntity> toEntityList(List<UploadedMedia> uploadedMediaList) {
        return uploadedMediaList.stream().map(this::toEntity).collect(Collectors.toList());
    }

    @Mapping(target = "type", source = "fileName", qualifiedByName = "getMediaTypeByFileName")
    @Mapping(target = "pendingDeletion", constant = "false")
    MediaEntity toEntity(UploadedMedia uploadedMedia);

    default List<Media> toModeList(List<MediaEntity> mediaEntity) {
        return mediaEntity.stream().map(this::toModel).collect(Collectors.toList());
    }

    Media toModel(MediaEntity mediaEntity);

    @Named("getMediaTypeByFileName")
    default MediaType getMediaTypeByFileName(String fileName) {
        String[] parts = fileName.split("\\.");
        if(parts.length == 2) {
            String extension = parts[1];

            if(Arrays.asList("png", "jpg", "jpeg").contains(extension)) {
                return MediaType.IMAGE;
            } else if(Arrays.asList("mp4", "mov", "wmv", "avi", "webm", "html5").contains(extension)) {
                return MediaType.VIDEO;
            } else {
                return MediaType.OTHER;
            }

        }
        return null;
    }
}
