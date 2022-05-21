package com.shareit.presentation;

import com.shareit.domain.dto.Media;
import com.shareit.business.MediaService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping(path = "/v1/media", produces = MediaType.APPLICATION_JSON_VALUE)
public class MediaController {

    private final MediaService mediaService;

    public MediaController(MediaService mediaService) {
        this.mediaService = mediaService;
    }

    @GetMapping("{id}")
    public ResponseEntity<Media> getMedia(@PathVariable Long id) {
        Media mediaById = mediaService.findMediaById(id);

        return mediaById == null ? ResponseEntity.noContent().build() : ResponseEntity.ok(mediaService.findMediaById(id));
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<List<Media>> createMedia(@RequestPart("files") MultipartFile[] files) {
        return ResponseEntity.ok(mediaService.createMedias(files));
    }

    @PutMapping(value = "{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Media> updateMedia(@PathVariable Long id, @RequestPart("file") MultipartFile file) {
        return ResponseEntity.ok(mediaService.updateMedia(id, file));
    }

    @DeleteMapping
    public ResponseEntity<Void> setMediaToBeDeleted(@RequestParam("ids") Long... mediaIds) {
        mediaService.setMediaToBeDeleted(mediaIds);
        return ResponseEntity.noContent().build();
    }
}
