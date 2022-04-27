package com.shareit.core;

import com.shareit.domain.entity.MediaType;
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


import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
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

    @Test
    @Order(1)
    public void testCreateMediaOk() throws Exception {
        MockMultipartFile firstFile = new MockMultipartFile("data", "test.png", "text/plain", "test".getBytes());

        mockMvc.perform(
                MockMvcRequestBuilders.multipart(MEDIA_ENDPOINT)
                .file(firstFile))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andReturn();
    }

    @Test
    @Order(2)
    public void testCreateMediaFileNotValid() throws Exception {
        MockMultipartFile firstFile = new MockMultipartFile("data", "test.png", "text/plain", new byte[0]);

        mockMvc.perform(
                MockMvcRequestBuilders.multipart(MEDIA_ENDPOINT)
                        .file(firstFile))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Invalid param: email"));;

    }

    @Test
    @Order(4)
    public void testGetMedia() throws Exception {
        mockMvc.perform(get(MEDIA_ENDPOINT + "/1").with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.url").isNotEmpty())
                .andExpect(jsonPath("$.type").value(MediaType.IMAGE));
    }

}