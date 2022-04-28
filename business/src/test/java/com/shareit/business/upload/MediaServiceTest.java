package com.shareit.business.upload;

import com.shareit.business.MediaService;
import com.shareit.data.repository.MediaRepository;
import com.shareit.domain.dto.Media;
import com.shareit.domain.entity.MediaEntity;
import com.shareit.domain.entity.MediaType;
import com.shareit.infrastructure.upload.UploaderService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

//TODO unit tests
class MediaServiceTest {
    private final UploaderService uploaderService;
    private final MediaService mediaService;
    private final MediaRepository mediaRepository;


    private MediaEntity mediaEntity = new MediaEntity(1L,
            MediaType.VIDEO,
            "https://uploadedurl.com",
            "m29fvh6s");

    private Media expectedMedia = new Media(1L, "test.png", MediaType.IMAGE);

    public MediaServiceTest() {
        this.uploaderService = Mockito.mock(UploaderService.class);
        this.mediaRepository = Mockito.mock(MediaRepository.class);
        this.mediaService = new MediaService(uploaderService, mediaRepository);
    }

    @Test
    public void testCreateMedia() {
        when(mediaRepository.save(any(MediaEntity.class))).thenReturn(mediaEntity);

        MultipartFile[] multipartFiles = new MultipartFile[] {new MockMultipartFile("test.png", new byte[10])};

        List<Media> media = mediaService.createMedias(multipartFiles);
        assertNotNull(media);
        assertFalse(media.isEmpty());
        assertEquals(expectedMedia.getId(), media.get(0).getId());
        verify(uploaderService).upload(multipartFiles[0], Collections.emptyMap());

    }
}