package com.mohammed.image_manager.repositories;

import com.mohammed.image_manager.entities.ImageOverlay;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface ImageOverlayRepository extends JpaRepository<ImageOverlay, UUID> {

}
