package com.mohammed.image_manager.controllers;

import com.mohammed.image_manager.dtos.*;
import com.mohammed.image_manager.mappers.ImageMapper;
import com.mohammed.image_manager.services.ImageService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.mohammed.image_manager.dtos.OverlayRequest;
import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/images")
@CrossOrigin(origins = "http://localhost:3000") // Required for Next.js
public class ImageController {
    private final ImageService imageService;
    private final ImageMapper imageMapper;

    public ImageController(ImageService imageService, ImageMapper imageMapper) {
        this.imageService = imageService;
        this.imageMapper = imageMapper;
    }

    @GetMapping
    public List<ImageDetailsResponse> getAll() {
        return imageService.getAllImages().stream()
                .map(imageMapper::toDetailsResponse)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ImageDetailsResponse getDetails(@PathVariable UUID id) {
        return imageMapper.toDetailsResponse(imageService.getImageById(id));
    }

    @PostMapping("/upload")
    public ImageDetailsResponse upload(@RequestParam("file") MultipartFile file,
                                       @RequestParam("title") String title,
                                       @RequestParam("description") String desc) throws IOException {
        return imageMapper.toDetailsResponse(imageService.uploadImage(file, title, desc));
    }

    @PostMapping("/{id}/overlays")
    public ResponseEntity<Void> addOverlay(@PathVariable UUID id, @RequestBody OverlayRequest request) throws IOException {
        imageService.addTextOverlay(id, request.overlayText());
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{id}")
    public ImageDetailsResponse update(@PathVariable UUID id, @RequestBody ImageUpdateRequest request) {
        return imageMapper.toDetailsResponse(imageService.updateImageMetadata(id, request.title(), request.description()));
    }

    @DeleteMapping("/overlays/{overlayId}")
    public ResponseEntity<Void> deleteOverlay(@PathVariable UUID overlayId) throws IOException {
        imageService.deleteOverlay(overlayId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) throws IOException {
        imageService.deleteImage(id);
        return ResponseEntity.noContent().build();
    }
}