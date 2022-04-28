package com.shareit.domain.mapper;

import com.shareit.domain.dto.Media;
import com.shareit.domain.entity.MediaEntity;
import com.shareit.domain.entity.MediaType;
import com.shareit.infrastructure.upload.UploadedMedia;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static org.junit.jupiter.api.Assertions.*;

class MediaMapperTest {

    private final MediaMapper mediaMapper = Mappers.getMapper(MediaMapper.class);

    private String fileName = "test.png";
    private String url = "http://test.com/test.png";
    private String publicId = "o1nx8";
    private Long id = 1L;

    private final MediaEntity mediaEntity = new MediaEntity(id, MediaType.IMAGE, url, publicId);
    private final Media userModel = new Media(id, url, MediaType.IMAGE);

    @Test
    public void testToEntityImageUploadedMedia() {
        MediaEntity entity = mediaMapper.toEntity(new UploadedMedia(fileName, url, publicId));
        assertNotNull(entity);
        assertEquals(new MediaEntity(null, MediaType.IMAGE, url, publicId), entity);
    }

    @Test
    public void testToEntityVideoUploadedMedia() {
        MediaEntity entity = mediaMapper.toEntity(new UploadedMedia("video.mp4", url, publicId));
        assertNotNull(entity);
        assertEquals(new MediaEntity(null, MediaType.VIDEO, url, publicId), entity);
    }

    @Test
    public void testToEntityOtherUploadedMedia() {
        MediaEntity entity = mediaMapper.toEntity(new UploadedMedia("document.doc", url, publicId));
        assertNotNull(entity);
        assertEquals(new MediaEntity(null, MediaType.OTHER, url, publicId), entity);
    }

    @Test
    public void testToModel() {
        Media model = mediaMapper.toModel(mediaEntity);
        assertNotNull(model);
        assertEquals(userModel, model);
    }

}