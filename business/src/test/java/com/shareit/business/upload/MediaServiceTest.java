package com.shareit.business.upload;

import com.shareit.business.MediaService;
import com.shareit.data.repository.MediaRepository;
import com.shareit.domain.dto.Media;
import com.shareit.domain.entity.MediaEntity;
import com.shareit.domain.entity.MediaType;
import com.shareit.exception.MediaNotFoundException;
import com.shareit.infrastructure.upload.UploadError;
import com.shareit.infrastructure.upload.UploadedMedia;
import com.shareit.infrastructure.upload.UploaderService;
import com.shareit.infrastructure.upload.exception.FailedUploadException;
import com.shareit.utils.commons.exception.InvalidParameterException;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class MediaServiceTest {
    private final UploaderService uploaderService;
    private final MediaService mediaService;
    private final MediaRepository mediaRepository;

    private String fileName = "test.png";
    private String url = "http://test.com/test.png";
    private String publicId = "o1nx8";
    private Long id = 1L;



    private List<UploadedMedia> uploadedMediaList = Arrays.asList(new UploadedMedia(fileName, url, publicId));

    private List<MediaEntity> mediaEntityList = Arrays.asList(
            new MediaEntity(id,
                    MediaType.IMAGE,
                    url,
                    publicId));

    private Media expectedMedia = new Media(id, url, MediaType.IMAGE);

    public MediaServiceTest() {
        this.uploaderService = Mockito.mock(UploaderService.class);
        this.mediaRepository = Mockito.mock(MediaRepository.class);
        this.mediaService = new MediaService(uploaderService, mediaRepository);
    }

    @Test
    public void testCreateMedia() {
        when(uploaderService.uploadParallel(any(MultipartFile[].class), anyMap())).thenReturn(uploadedMediaList);
        when(mediaRepository.saveAll(anyList())).thenReturn(mediaEntityList);

        MultipartFile[] multipartFiles = new MultipartFile[] {new MockMultipartFile(fileName, new byte[10])};

        List<Media> media = mediaService.createMedias(multipartFiles);
        assertNotNull(media);
        assertFalse(media.isEmpty());
        assertEquals(expectedMedia, media.get(0));
        verify(uploaderService).uploadParallel(multipartFiles, Collections.emptyMap());

    }

    @Test
    public void testCreateMediaThrowBadRequestException() {
        when(uploaderService.uploadParallel(any(MultipartFile[].class), anyMap())).thenReturn(uploadedMediaList);
        when(mediaRepository.saveAll(anyList())).thenReturn(mediaEntityList);

        InvalidParameterException badRequestException = assertThrows(InvalidParameterException.class, () -> mediaService.createMedias(new MultipartFile[0]));
        assertNotNull(badRequestException);
        assertEquals("Unable to upload. No files were provided.", badRequestException.getMessage());
    }

    @Test
    public void testCreateMediaThrowInvalidParameterExceptionByUploader() {
        doThrow(new InvalidParameterException("files", "Unable to upload. An empty file (test.png) was provided.")).when(uploaderService).uploadParallel(any(MultipartFile[].class), anyMap());
        when(mediaRepository.saveAll(anyList())).thenReturn(mediaEntityList);

        InvalidParameterException invalidParameterException = assertThrows(InvalidParameterException.class, () -> mediaService.createMedias(new MultipartFile[0]));
        assertNotNull(invalidParameterException);
        assertEquals("Unable to upload. No files were provided.", invalidParameterException.getMessage());
        assertEquals("files", invalidParameterException.getParamName());
    }

    @Test
    public void testCreateMediaThrowFailedUploadExceptionByUploader() {
        List<UploadError> expectedUploadErrorList = Arrays.asList(new UploadError("test.png", "Max file size exceeded"));
        doThrow(new FailedUploadException("Upload failed", expectedUploadErrorList)).when(uploaderService).uploadParallel(any(MultipartFile[].class), anyMap());
        when(mediaRepository.saveAll(anyList())).thenReturn(mediaEntityList);

        FailedUploadException failedUploadException =
                assertThrows(
                        FailedUploadException.class,
                        () -> mediaService.createMedias(new MultipartFile[] {new MockMultipartFile(fileName, new byte[10])}));
        assertNotNull(failedUploadException);
        assertEquals("Upload failed", failedUploadException.getMessage());
        assertEquals(expectedUploadErrorList.get(0), failedUploadException.getUploadErrors().get(0));
    }

    @Test
    public void testSetMediaToBeDeleted() {
        when(mediaRepository.setMediaToBeDeleted(anyLong(), anyLong())).thenReturn(2);

        int rowsAffected = mediaService.setMediaToBeDeleted(1L, 2L);
        assertTrue(rowsAffected == 2);
    }

    @Test
    public void testFindMediasToBeDeleted() {
        when(mediaRepository.findAllToBeDeleted()).thenReturn(Optional.of(mediaEntityList));

        List<Media> mediasToBeDeleted = mediaService.findMediasToBeDeleted();

        assertNotNull(mediasToBeDeleted);
        assertFalse(mediasToBeDeleted.isEmpty());
        assertEquals(expectedMedia, mediasToBeDeleted.get(0));
    }

    @Test
    public void testFindMediaById() {
        when(mediaRepository.findById(anyLong())).thenReturn(Optional.of(mediaEntityList.get(0)));

        Media mediaById = mediaService.findMediaById(1L);

        assertNotNull(mediaById);
        assertEquals(expectedMedia, mediaById);
    }

    @Test
    public void testFindMediaByIdNotFound() {
        when(mediaRepository.findById(anyLong())).thenReturn(Optional.empty());

        Media mediaById = mediaService.findMediaById(1L);

        assertNull(mediaById);
    }

    @Test
    public void testUpdateMediaThrowsMediaNotFoundException() {
        when(mediaRepository.findById(anyLong())).thenReturn(Optional.empty());

        MediaNotFoundException mediaNotFoundException = assertThrows(MediaNotFoundException.class, () -> mediaService.updateMedia(1L, newMockMultipartFile(fileName)));
        assertNotNull(mediaNotFoundException);
        assertEquals(mediaNotFoundException.getMessage(), "Media 1 could not be found");
    }

    @Test
    public void testUpdateMedia() {
        UploadedMedia uploadedMedia = new UploadedMedia(fileName, url, publicId);
        when(mediaRepository.findById(anyLong())).thenReturn(Optional.of(mediaEntityList.get(0)));
        when(uploaderService.upload(any(MultipartFile.class), anyMap())).thenReturn(uploadedMedia);
        when(mediaRepository.updateMediaUploadInformation(anyLong(), anyString(), anyString())).thenReturn(1);
        when(uploaderService.destroyUploadedMedias(anyList())).thenReturn(Mockito.mock(CompletableFuture.class));

        Media updatedMedia = mediaService.updateMedia(1L, newMockMultipartFile(fileName));

        assertNotNull(updatedMedia);
        assertEquals(expectedMedia, updatedMedia);
    }

    private static MockMultipartFile newMockMultipartFile(String fileName) {
        return newMockMultipartFile(fileName, 10);
    }

    private static MockMultipartFile newMockMultipartFile(String fileName, int contentSize) {
        byte[] content = new byte[contentSize];
        return new MockMultipartFile(fileName, fileName, "multipart/form-data", content);
    }

}