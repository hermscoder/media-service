package com.shareit.presentation;

import com.shareit.domain.dto.Media;
import com.shareit.business.MediaService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping(path = "/v1/media")
public class MediaController {

    private final MediaService mediaService;

    public MediaController(MediaService mediaService) {
        this.mediaService = mediaService;
    }

    @GetMapping("{id}")
    public ResponseEntity<Media> findAllToBeDeleted(@PathVariable Long id) {
        Media mediaById = mediaService.findMediaById(id);

        return mediaById == null ? ResponseEntity.noContent().build() : ResponseEntity.ok(mediaService.findMediaById(id));
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<List<Media>> createMedia(@RequestPart("file") MultipartFile[] files) {
        return ResponseEntity.ok(mediaService.createMedias(files));
    }

    @DeleteMapping()
    public ResponseEntity<Void> setMediaToBeDeleted(@RequestParam("ids") Long... mediaIds) {
        mediaService.setMediaToBeDeleted(mediaIds);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("toBeDeleted")
    public ResponseEntity<List<Media>> findAllToBeDeleted() {
        return ResponseEntity.ok(mediaService.findMediasToBeDeleted());
    }
}
