package com.shareit.domain.mapper;

import com.shareit.domain.dto.Media;
import com.shareit.domain.entity.MediaEntity;
import com.shareit.domain.entity.MediaType;
import com.shareit.infrastructure.upload.UploadedMedia;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

//TODO unit tests
@Mapper
public interface MediaMapper {
    MediaMapper INSTANCE = Mappers.getMapper(MediaMapper.class);

    default List<MediaEntity> toEntityList(List<UploadedMedia> uploadedMediaList) {
        return uploadedMediaList.stream().map(this::toEntity).collect(Collectors.toList());
    }

    default MediaEntity toEntity(UploadedMedia uploadedMedia) {
        return new MediaEntity(
                null ,
                MediaType.IMAGE,
                uploadedMedia.getUrl(),
                uploadedMedia.getPublicId());
    }

    default List<Media> toModeList(List<MediaEntity> mediaEntity) {
        return mediaEntity.stream().map(this::toModel).collect(Collectors.toList());
    }

    Media toModel(MediaEntity mediaEntity);

}
