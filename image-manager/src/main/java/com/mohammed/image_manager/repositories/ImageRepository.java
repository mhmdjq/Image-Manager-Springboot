package com.mohammed.image_manager.repositories;

import com.mohammed.image_manager.entities.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface ImageRepository extends JpaRepository<Image, UUID> {

}
