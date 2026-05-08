package com.mohammed.image_manager.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "images")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String title;
    private String description;
    private String originalFileName;
    private String systemFileName;
    private String format;
    private Long fileSize;
    private int width;
    private int height;

    @OneToMany(mappedBy = "parentImage", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ImageOverlay> overlays = new ArrayList<>();

    @Column(updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();
}
