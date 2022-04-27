package com.shareit.core;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class MediaControllersWebTest {

    private static final String MEDIA_ENDPOINT = "/v1/media";

    @Autowired
    private MockMvc mockMvc;

    private Long expectedMediaId = 1L;

    @Test
    @Order(1)
    public void testCreateMediaOk() throws Exception {
        Path path = Paths.get("src/test/resources/logo.png");

        byte[] testFile = Files.readAllBytes(path);
        MockMultipartFile file1 = new MockMultipartFile("files", "test1.png", "text/plain", testFile);

        mockMvc.perform(
                MockMvcRequestBuilders.multipart(MEDIA_ENDPOINT)
                        .file(file1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(expectedMediaId))
                .andReturn();
    }

    @Test
    @Order(2)
    public void testCreateMediaEmptyFile() throws Exception {
        MockMultipartFile firstFile = new MockMultipartFile("files", "test.png", "text/plain", new byte[0]);

        mockMvc.perform(
                MockMvcRequestBuilders.multipart(MEDIA_ENDPOINT)
                        .file(firstFile))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Unable to upload. No files were provided."));;

    }

    @Test
    @Order(3)
    public void testCreateMediaFilesNotValid() throws Exception {
        MockMultipartFile firstFile = new MockMultipartFile("files", "file1.png", "text/plain", new byte[0]);
        MockMultipartFile secondFile = new MockMultipartFile("files", "file1.png", "text/plain", new byte[0]);

        mockMvc.perform(
                MockMvcRequestBuilders.multipart(MEDIA_ENDPOINT)
                        .file(firstFile)
                        .file(secondFile))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors", Matchers.hasSize(2)))
                .andExpect(jsonPath("$.errors[0].message").value("Unable to upload. An empty file (file1.png) was provided."))
                .andExpect(jsonPath("$.errors[1].message").value("Unable to upload. An empty file (file1.png) was provided."));

    }

    @Test
    @Order(4)
    public void testGetMedia() throws Exception {
        mockMvc.perform(get(MEDIA_ENDPOINT + "/" + expectedMediaId).with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.url").isNotEmpty())
                .andExpect(jsonPath("$.type").value("IMAGE"));
    }

    @Test
    @Order(5)
    public void testSetMediaToBeDeleted() throws Exception {
        mockMvc.perform(delete(MEDIA_ENDPOINT)
                .param("ids", String.valueOf(expectedMediaId)).with(csrf()))
                .andExpect(status().isNoContent());
    }

    @Test
    @Order(6)
    public void testGetDeletedMedia() throws Exception {
        mockMvc.perform(get(MEDIA_ENDPOINT + "/" + expectedMediaId).with(csrf()))
                .andExpect(status().isNoContent());
    }
}