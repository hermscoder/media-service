package com.shareit.infrastructure.upload;

import com.shareit.infrastructure.integration.CloudinaryConfiguration;
import com.shareit.infrastructure.integration.CloudinaryManager;
import com.shareit.infrastructure.integration.CloudinarySettings;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

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

    private final List<UploadedMedia> expectedUploadedMediaList = Arrays.asList(new UploadedMedia(fileName, url, publicId));

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

        UploadedMedia uploadedMedia = fileUploaderService.upload(new MockMultipartFile(fileName, new byte[10]), Collections.emptyMap());

        assertNotNull(uploadedMedia);
        assertEquals(expectedUploadMedia, uploadedMedia);
    }

    @Test
    void testUploadParallel() throws IOException {
        when(cloudinaryManager.getCloudinarySettings()).thenReturn(cloudinarySettings);
        when(cloudinaryManager.upload(any(), anyMap())).thenReturn(expectedUploadReturnMap);

        MultipartFile[] multipartFiles = new MultipartFile[] {new MockMultipartFile(fileName, fileName, "multipart/form-data",new byte[10])};
        List<UploadedMedia> uploadedMediaList = fileUploaderService.uploadParallel(multipartFiles, Collections.emptyMap());

        assertNotNull(uploadedMediaList);
        assertFalse(uploadedMediaList.isEmpty());
        assertEquals(expectedUploadedMediaList.get(0), uploadedMediaList.get(0));
    }

    @Test
    void testDestroyUploadedMedias() {
    }

    @Test
    void testDestroy() {
    }
}