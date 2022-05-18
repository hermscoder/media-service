package com.shareit.infrastructure.upload;

import com.shareit.infrastructure.integration.CloudinaryConfiguration;
import com.shareit.infrastructure.integration.CloudinaryManager;
import com.shareit.infrastructure.integration.CloudinarySettings;
import com.shareit.infrastructure.upload.exception.FailedDestructionException;
import com.shareit.infrastructure.upload.exception.FailedUploadException;
import com.shareit.utils.commons.exception.InvalidParameterException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class FileUploaderServiceTest {

    private final CloudinaryManager cloudinaryManager;
    private final FileUploaderService fileUploaderService;

    private String fileName = "test.png";
    private String url = "http://test.com/test.png";
    private String publicId = "o1nx8";

    private final Map expectedUploadReturnMap = Map.of(
                                        "url", url,
                                        "public_id", publicId);

    private final UploadedMedia expectedUploadMedia = new UploadedMedia(fileName, url, publicId);

    private final List<UploadedMedia> expectedUploadedMediaList = Arrays.asList(new UploadedMedia(fileName, url, publicId), new UploadedMedia(fileName, url, publicId));

    private final MockMultipartFile mockMultipartFile = new MockMultipartFile(fileName, fileName, "multipart/form-data",new byte[10]);

    private CloudinarySettings cloudinarySettings;


    FileUploaderServiceTest() {
        this.cloudinaryManager = Mockito.mock(CloudinaryManager.class);
        this.fileUploaderService = new FileUploaderService(cloudinaryManager);
    }

    @BeforeEach
    void setUp() {
        CloudinaryConfiguration cloudinaryConfiguration = new CloudinaryConfiguration();
        cloudinaryConfiguration.setMaxFileSize(10485760L);

        this.cloudinarySettings = new CloudinarySettings(cloudinaryConfiguration);
    }

    @Test
    void testUpload() throws IOException {
        when(cloudinaryManager.upload(any(), anyMap())).thenReturn(expectedUploadReturnMap);

        UploadedMedia uploadedMedia = fileUploaderService.upload(mockMultipartFile, Collections.emptyMap());

        assertNotNull(uploadedMedia);
        assertEquals(expectedUploadMedia, uploadedMedia);
    }

    @Test
    void testUploadThrowFailedUploadException() throws IOException {
        when(cloudinaryManager.upload(any(), anyMap())).thenThrow(new FailedUploadException("Unable to upload"));

        FailedUploadException failedUploadException = assertThrows(FailedUploadException.class, () -> fileUploaderService.upload(mockMultipartFile, Collections.emptyMap()));

        assertNotNull(failedUploadException);
        assertEquals("Unable to upload", failedUploadException.getMessage());
        assertEquals(0, failedUploadException.getUploadErrors().size());
    }

    @Test
    void testUploadParallelSingleFile() throws IOException {
        when(cloudinaryManager.getCloudinarySettings()).thenReturn(cloudinarySettings);
        when(cloudinaryManager.upload(any(), anyMap())).thenReturn(expectedUploadReturnMap);

        MultipartFile[] multipartFiles = new MultipartFile[] { mockMultipartFile };
        List<UploadedMedia> uploadedMediaList = fileUploaderService.uploadParallel(multipartFiles, Collections.emptyMap());

        assertNotNull(uploadedMediaList);
        assertFalse(uploadedMediaList.isEmpty());
        assertTrue(uploadedMediaList.size() == 1);
        assertEquals(expectedUploadedMediaList.get(0), uploadedMediaList.get(0));
        verify(cloudinaryManager, times(1)).upload(mockMultipartFile.getBytes(), Collections.emptyMap());
    }

    @Test
    void testUploadParallelTwoFiles() throws IOException {
        when(cloudinaryManager.getCloudinarySettings()).thenReturn(cloudinarySettings);
        when(cloudinaryManager.upload(any(), anyMap())).thenReturn(expectedUploadReturnMap);

        MultipartFile[] multipartFiles = new MultipartFile[] { mockMultipartFile, mockMultipartFile };
        List<UploadedMedia> uploadedMediaList = fileUploaderService.uploadParallel(multipartFiles, Collections.emptyMap());

        assertNotNull(uploadedMediaList);
        assertFalse(uploadedMediaList.isEmpty());
        assertTrue(uploadedMediaList.size() == 2);
        assertEquals(expectedUploadedMediaList.get(0), uploadedMediaList.get(0));
        assertEquals(expectedUploadedMediaList.get(1), uploadedMediaList.get(1));
        verify(cloudinaryManager, times(2)).upload(mockMultipartFile.getBytes(), Collections.emptyMap());
    }

    @Test
    void testUploadParallelWithSingleEmptyFile() {
        when(cloudinaryManager.getCloudinarySettings()).thenReturn(cloudinarySettings);

        MultipartFile[] multipartFiles = new MultipartFile[] { MockMultipartFileObjectMother.newMockMultipartFile(fileName, 0) };
        InvalidParameterException invalidParameterException = assertThrows(InvalidParameterException.class, () -> fileUploaderService.uploadParallel(multipartFiles, Collections.emptyMap()));

        assertNotNull(invalidParameterException);
        assertEquals("files", invalidParameterException.getParamName());
        assertEquals("Unable to upload. An empty file (" + fileName + ") was provided.", invalidParameterException.getMessage());
    }

    @Test
    void testUploadParallelWithSingleMaxSizeFile() {
        when(cloudinaryManager.getCloudinarySettings()).thenReturn(cloudinarySettings);

        MultipartFile[] multipartFiles = new MultipartFile[] { MockMultipartFileObjectMother.newMockMultipartFile(fileName, 10485761) };
        InvalidParameterException invalidParameterException = assertThrows(InvalidParameterException.class, () -> fileUploaderService.uploadParallel(multipartFiles, Collections.emptyMap()));

        assertNotNull(invalidParameterException);
        assertEquals("files", invalidParameterException.getParamName());
        assertEquals("Unable to upload. File (" + fileName + ") size too large. More than 10 Mbs", invalidParameterException.getMessage());
    }

    @Test
    void testUploadParallelWithOneEmptyFileAndOneMaxSizeFile() throws IOException {
        when(cloudinaryManager.getCloudinarySettings()).thenReturn(cloudinarySettings);

        MultipartFile[] multipartFiles = new MultipartFile[] {
                MockMultipartFileObjectMother.newMockMultipartFile("empty_file.png", 0),
                MockMultipartFileObjectMother.newMockMultipartFile("max_size_file.png", 10485761) };
        FailedUploadException failedUploadException = assertThrows(FailedUploadException.class, () -> fileUploaderService.uploadParallel(multipartFiles, Collections.emptyMap()));

        assertNotNull(failedUploadException);
        assertEquals(failedUploadException.getMessage(), "Upload failed");
        assertNotNull(failedUploadException.getUploadErrors());
        assertTrue(failedUploadException.getUploadErrors().size() == 2);
        assertEquals(failedUploadException.getUploadErrors().get(0), new UploadError("empty_file.png", "Unable to upload. An empty file (empty_file.png) was provided."));
        assertEquals(failedUploadException.getUploadErrors().get(1), new UploadError("max_size_file.png", "Unable to upload. File (max_size_file.png) size too large. More than 10 Mbs"));
        verify(cloudinaryManager, times(0)).destroy(publicId, Collections.emptyMap());
    }

    @Test
    void testUploadParallelWithOneFileUploadedAndOneMaxSizeFile() throws IOException {
        when(cloudinaryManager.getCloudinarySettings()).thenReturn(cloudinarySettings);
        when(cloudinaryManager.upload(any(), anyMap())).thenReturn(expectedUploadReturnMap);

        MultipartFile[] multipartFiles = new MultipartFile[] {
                MockMultipartFileObjectMother.newMockMultipartFile(fileName),
                MockMultipartFileObjectMother.newMockMultipartFile("max_size_file.png", 10485761) };
        FailedUploadException failedUploadException = assertThrows(FailedUploadException.class, () -> fileUploaderService.uploadParallel(multipartFiles, Collections.emptyMap()));

        assertNotNull(failedUploadException);
        assertEquals(failedUploadException.getMessage(), "Upload failed");
        assertNotNull(failedUploadException.getUploadErrors());
        assertTrue(failedUploadException.getUploadErrors().size() == 1);
        assertEquals(failedUploadException.getUploadErrors().get(0), new UploadError("max_size_file.png", "Unable to upload. File (max_size_file.png) size too large. More than 10 Mbs"));
        verify(cloudinaryManager, times(1)).destroy(publicId, Collections.emptyMap());
    }

    @Test
    void testDestroyUploadedMedias() throws IOException, ExecutionException, InterruptedException {
        fileUploaderService.destroyUploadedMedias(List.of("ahs8gff", "9yhahm0")).get();
        verify(cloudinaryManager, times(1)).destroy("ahs8gff", Collections.emptyMap());
        verify(cloudinaryManager, times(1)).destroy("9yhahm0", Collections.emptyMap());
    }

    @Test
    void testDestroyUploadedMediasThrowsFailedDestructionException() throws IOException {
        when(cloudinaryManager.destroy(anyString(), anyMap())).thenThrow(new FailedDestructionException("Unable to destroy file"));
        ExecutionException executionException = assertThrows(ExecutionException.class, () -> fileUploaderService.destroyUploadedMedias(List.of("ahs8gff", "9yhahm0")).get());
        assertEquals(FailedDestructionException.class, executionException.getCause().getClass());
        assertEquals("Unable to destroy file", executionException.getCause().getMessage());
    }

    @Test
    void testDestroy() throws IOException {
        when(cloudinaryManager.destroy(anyString(), anyMap())).thenReturn(Collections.emptyMap());
        Map destroyReturn = fileUploaderService.destroy(publicId);
        verify(cloudinaryManager, times(1)).destroy(publicId, Collections.emptyMap());
        assertNotNull(destroyReturn);
        assertEquals(Collections.emptyMap(), destroyReturn);
    }

    @Test
    void testDestroyThrowsFailedDestructionException() throws IOException {
        when(cloudinaryManager.destroy(anyString(), anyMap())).thenThrow(new FailedDestructionException("Unable to destroy file"));
        FailedDestructionException failedDestructionException = assertThrows(FailedDestructionException.class, () -> fileUploaderService.destroy(publicId));
        assertNotNull(failedDestructionException);
        assertEquals(failedDestructionException.getMessage(), "Unable to destroy file");
    }


}