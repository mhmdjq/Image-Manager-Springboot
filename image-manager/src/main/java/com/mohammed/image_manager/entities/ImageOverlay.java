package com.mohammed.image_manager.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "image_overlays")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ImageOverlay {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String systemFileName;
    private String overlayText;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_image_id")
    private Image parentImage;

    private LocalDateTime createdAt = LocalDateTime.now();
}
