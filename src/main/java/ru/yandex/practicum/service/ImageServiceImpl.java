package ru.yandex.practicum.service;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import ru.yandex.practicum.utils.FileUtils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Objects;
import java.util.UUID;

@Service
public class ImageServiceImpl implements ImageService {

    private static final String IMAGE_ROOT_PATH = "uploads/images";

    @Override
    public String saveImage(MultipartFile image) throws IOException {
        String contentType = image.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            throw new IllegalArgumentException("Only image files are allowed");
        }

        String originalFilename = StringUtils.cleanPath(Objects.requireNonNull(image.getOriginalFilename()));
        String fileExtension = FileUtils.getFileExtension(originalFilename);
        if (!FileUtils.isImageExtensionAllowed(fileExtension)) {
            throw new IllegalArgumentException("Unsupported file format");
        }

        Path imagesDir = Paths.get(IMAGE_ROOT_PATH);
        Files.createDirectories(imagesDir);

        String filename = UUID.randomUUID() + fileExtension;
        Path filePath = imagesDir.resolve(filename).normalize();

        if (!filePath.startsWith(imagesDir)) {
            throw new SecurityException("Invalid file path");
        }

        try (InputStream inputStream = image.getInputStream()) {
            Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);

        }
        return filename;
    }

    @Override
    public Resource getImageAsResource(String relativePath) throws IOException {
        Path imageStorageRoot = Paths.get(IMAGE_ROOT_PATH).toAbsolutePath().normalize();
        Path filePath = imageStorageRoot.resolve(relativePath).normalize();

        if (!Files.exists(filePath)) {
            throw new FileNotFoundException("Image not found: " + relativePath);
        }

        if (!filePath.startsWith(imageStorageRoot)) {
            throw new SecurityException("Access denied to file: " + relativePath);
        }

        return new UrlResource(filePath.toUri());
    }
}
