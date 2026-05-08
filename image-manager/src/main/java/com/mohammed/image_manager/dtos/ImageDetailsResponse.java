package com.mohammed.image_manager.dtos;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record ImageDetailsResponse(
        UUID id,
        String title,
        String description,
        String originalFileName,
        String format,
        String dimensions, // e.g., "1920x1080"
        String fileSizeFormatted,
        String imageUrl,
        int overlayCount,
        List<OverlayDto> overlays
) {

    public record OverlayDto(
            UUID id,
            String overlayText,
            String imageUrl,
            LocalDateTime createdAt
    ) {}
}