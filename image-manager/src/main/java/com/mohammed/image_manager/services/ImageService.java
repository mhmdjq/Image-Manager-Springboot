package com.mohammed.image_manager.services;

import com.mohammed.image_manager.entities.Image;
import com.mohammed.image_manager.entities.ImageOverlay;
import com.mohammed.image_manager.exceptions.ImageNotFoundException;
import com.mohammed.image_manager.repositories.ImageRepository;
import com.mohammed.image_manager.repositories.ImageOverlayRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class ImageService {

    private final ImageRepository imageRepository;
    private final ImageOverlayRepository overlayRepository;
    private final Path root = Paths.get("uploads");

    public ImageService(ImageRepository imageRepository, ImageOverlayRepository overlayRepository) {
        this.imageRepository = imageRepository;
        this.overlayRepository = overlayRepository;
        try { Files.createDirectories(root); } catch (IOException e) { throw new RuntimeException("Could not initialize folder for upload!"); }
    }

    public List<Image> getAllImages() {
        return imageRepository.findAll();
    }

    public Image getImageById(UUID id) {
        return imageRepository.findById(id).orElseThrow(() -> new RuntimeException("Image not found"));
    }

    @Transactional
    public Image uploadImage(MultipartFile file, String title, String description) throws IOException {
        String systemName = UUID.randomUUID() + "_" + file.getOriginalFilename();
        Files.copy(file.getInputStream(), this.root.resolve(systemName));

        BufferedImage bi = ImageIO.read(this.root.resolve(systemName).toFile());

        Image image = new Image();
        image.setTitle(title);
        image.setDescription(description);
        image.setOriginalFileName(file.getOriginalFilename());
        image.setSystemFileName(systemName);
        image.setFileSize(file.getSize());
        image.setFormat(file.getContentType().split("/")[1]);
        image.setWidth(bi.getWidth());
        image.setHeight(bi.getHeight());

        return imageRepository.save(image);
    }

    @Transactional
    public void addTextOverlay(UUID imageId, String text) throws IOException {
        Image parent = getImageById(imageId);

        BufferedImage original = ImageIO.read(root.resolve(parent.getSystemFileName()).toFile());
        BufferedImage edited = new BufferedImage(original.getWidth(), original.getHeight(), BufferedImage.TYPE_INT_RGB);

        Graphics2D g = edited.createGraphics();
        // Enable Anti-Aliasing for smoother text
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g.drawImage(original, 0, 0, null);

        int padding = 40;
        int maxWidth = original.getWidth() - (padding * 2);
        int fontSize = original.getHeight() / 15;
        g.setFont(new Font("Arial", Font.BOLD, fontSize));
        FontMetrics metrics = g.getFontMetrics();

        List<String> lines = wrapText(text, metrics, maxWidth);

        // Shrink Font if too many lines
        while (lines.size() * metrics.getHeight() > original.getHeight() * 0.9) {
            if (fontSize <= 12) {
                throw new IllegalArgumentException("Text is too long for this small image! Try shorter text.");
            }
            fontSize -= 2;
            g.setFont(new Font("Arial", Font.BOLD, fontSize));
            metrics = g.getFontMetrics();
            lines = wrapText(text, metrics, maxWidth);
        }

        // Draw Lines Centered Vertically
        int lineHeight = metrics.getHeight();
        int totalHeight = lines.size() * lineHeight;
        int y = (original.getHeight() - totalHeight) / 2 + metrics.getAscent();

        for (String line : lines) {
            int x = (original.getWidth() - metrics.stringWidth(line)) / 2;

            // Shadow for readability
            g.setColor(new Color(0, 0, 0, 150));
            g.drawString(line, x + 2, y + 2);

            // Main Text
            g.setColor(Color.WHITE);
            g.drawString(line, x, y);
            y += lineHeight;
        }

        g.dispose();

        String overlayName = "overlay_" + UUID.randomUUID() + ".jpg";
        ImageIO.write(edited, "jpg", root.resolve(overlayName).toFile());

        ImageOverlay overlay = new ImageOverlay();
        overlay.setOverlayText(text);
        overlay.setSystemFileName(overlayName);
        overlay.setParentImage(parent);
        overlayRepository.save(overlay);
    }

    // Helper method to split text into lines based on maximum pixel width
    private List<String> wrapText(String text, FontMetrics metrics, int maxWidth) {
        String[] words = text.split(" ");
        List<String> lines = new ArrayList<>();
        StringBuilder currentLine = new StringBuilder();

        for (String word : words) {
            if (metrics.stringWidth(currentLine + word) < maxWidth) {
                currentLine.append(word).append(" ");
            } else {
                lines.add(currentLine.toString().trim());
                currentLine = new StringBuilder(word).append(" ");
            }
        }
        lines.add(currentLine.toString().trim());
        return lines;
    }

    @Transactional
    public Image updateImageMetadata(UUID id, String title, String description) {
        Image image = imageRepository.findById(id)
                .orElseThrow(() -> new ImageNotFoundException("Image not found with id: " + id));

        image.setTitle(title);
        image.setDescription(description);

        return imageRepository.save(image);
    }

    // Allows deleting a specific overlay from the nested list
    @Transactional
    public void deleteOverlay(UUID overlayId) throws IOException {
        ImageOverlay overlay = overlayRepository.findById(overlayId)
                .orElseThrow(() -> new ImageNotFoundException("Overlay version not found"));

        // 1. Delete physical file from 'uploads' folder
        Files.deleteIfExists(root.resolve(overlay.getSystemFileName()));

        // 2. Remove record from database
        overlayRepository.delete(overlay);
    }

    @Transactional
    public void deleteImage(UUID id) throws IOException {
        Image image = getImageById(id);
        // Physical file cleanup
        Files.deleteIfExists(root.resolve(image.getSystemFileName()));
        for (ImageOverlay ov : image.getOverlays()) {
            Files.deleteIfExists(root.resolve(ov.getSystemFileName()));
        }
        imageRepository.delete(image);
    }
}