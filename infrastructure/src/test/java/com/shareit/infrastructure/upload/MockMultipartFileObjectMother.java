package com.shareit.infrastructure.upload;

import org.springframework.mock.web.MockMultipartFile;

public class MockMultipartFileObjectMother {

    public static MockMultipartFile newMockMultipartFile(String fileName) {
        return newMockMultipartFile(fileName, 10);
    }

    public static MockMultipartFile newMockMultipartFile(String fileName, int contentSize) {
        byte[] content = new byte[contentSize];
        return new MockMultipartFile(fileName, fileName, "multipart/form-data", content);
    }
}
