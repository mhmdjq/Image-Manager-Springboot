package com.mohammed.image_manager.dtos;

public record ImageUpdateRequest(
        String title,
        String description
) {}