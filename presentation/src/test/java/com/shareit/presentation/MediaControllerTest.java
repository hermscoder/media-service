package com.shareit.presentation;

import com.shareit.domain.dto.Media;
import com.shareit.business.MediaService;
import com.shareit.domain.entity.MediaType;
import com.shareit.utils.commons.exception.InvalidParameterException;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class MediaControllerTest {
    private final MediaController mediaController;
    private final MediaService mediaService;

    MediaControllerTest() {
        this.mediaService = Mockito.mock(MediaService.class);
        this.mediaController = new MediaController(mediaService);
    }

    @Test
    public void testCreateMedia() {
        List<Media> expectedMediaList = List.of(new Media(1L, "test.png", MediaType.IMAGE));
        when(mediaService.createMedias(any(MultipartFile[].class))).thenReturn(expectedMediaList);

        ResponseEntity<List<Media>> mediasCreatedResponse =
                mediaController.createMedia(new MultipartFile[] {new MockMultipartFile("test.png", new byte[10])});
        assertNotNull(mediasCreatedResponse);
        assertEquals(HttpStatus.OK, mediasCreatedResponse.getStatusCode());
        assertEquals(expectedMediaList, mediasCreatedResponse.getBody());
    }

    @Test
    public void testCreateMediaThrowInvalidParameterException() {
        when(mediaService.createMedias(any(MultipartFile[].class))).thenThrow(new InvalidParameterException("passwordConfirmation"));

        InvalidParameterException invalidParameterException = assertThrows(InvalidParameterException.class,
                () -> mediaController.createMedia(new MultipartFile[] {new MockMultipartFile("test.png", new byte[10])}));

        assertNotNull(invalidParameterException);
        assertEquals("passwordConfirmation", invalidParameterException.getParamName());
        assertEquals("Invalid param: passwordConfirmation", invalidParameterException.getMessage());
    }



}