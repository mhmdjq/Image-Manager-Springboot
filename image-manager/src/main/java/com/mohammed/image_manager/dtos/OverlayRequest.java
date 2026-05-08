package com.mohammed.image_manager.dtos;

import jakarta.validation.constraints.NotBlank;

public record OverlayRequest(
        @NotBlank(message = "Overlay text cannot be empty")
        String overlayText
) {}