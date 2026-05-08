package com.mohammed.image_manager.mappers;

import com.mohammed.image_manager.dtos.ImageDetailsResponse;
import com.mohammed.image_manager.entities.Image;
import com.mohammed.image_manager.entities.ImageOverlay;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class ImageMapper {

    public ImageDetailsResponse toDetailsResponse(Image image) {
        return new ImageDetailsResponse(
                image.getId(),
                image.getTitle(),
                image.getDescription(),
                image.getOriginalFileName(),
                image.getFormat(),
                image.getWidth() + "x" + image.getHeight(),
                formatFileSize(image.getFileSize()),
                "/uploads/" + image.getSystemFileName(),
                image.getOverlays().size(),
                image.getOverlays().stream().map(this::toOverlayDto).collect(Collectors.toList())
        );
    }

    private ImageDetailsResponse.OverlayDto toOverlayDto(ImageOverlay overlay) {
        return new ImageDetailsResponse.OverlayDto(
                overlay.getId(),
                overlay.getOverlayText(),
                "/uploads/" + overlay.getSystemFileName(),
                overlay.getCreatedAt()
        );
    }

    private String formatFileSize(long bytes) {
        if (bytes < 1024) return bytes + " B";
        int exp = (int) (Math.log(bytes) / Math.log(1024));
        String pre = "KMGTPE".charAt(exp - 1) + "B";
        return String.format("%.1f %s", bytes / Math.pow(1024, exp), pre);
    }
}