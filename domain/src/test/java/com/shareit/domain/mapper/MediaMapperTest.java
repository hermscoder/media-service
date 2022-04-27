package com.shareit.domain.mapper;

import com.shareit.domain.dto.Media;
import com.shareit.domain.entity.MediaEntity;
import com.shareit.domain.entity.MediaType;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static org.junit.jupiter.api.Assertions.*;

class MediaMapperTest {

    private final MediaMapper mediaMapper = Mappers.getMapper(MediaMapper.class);


    private final MediaEntity mediaEntity = new MediaEntity(1L,
            MediaType.IMAGE,
            "test.png",
            "123123");

    private final Media userModel = new Media(1L,
            "test.png",
            MediaType.IMAGE);

    @Test
    public void testToModel() {
        Media model = mediaMapper.toModel(mediaEntity);
        assertNotNull(model);
        assertEquals(userModel, model);
    }

}