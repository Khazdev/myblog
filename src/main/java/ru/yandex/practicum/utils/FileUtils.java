package ru.yandex.practicum.utils;

import lombok.experimental.UtilityClass;
import org.springframework.util.StringUtils;

import java.util.Set;

@UtilityClass
public final class FileUtils {

    private static final Set<String> ALLOWED_IMAGE_EXTENSIONS = Set.of(".jpg", ".jpeg", ".png", ".gif");

    public static String getFileExtension(String filename) {
        if (!StringUtils.hasText(filename)) {
            return "";
        }
        int dotIndex = filename.lastIndexOf('.');
        return (dotIndex > 0) ? filename.substring(dotIndex).toLowerCase() : "";
    }

    public static boolean isExtensionAllowed(String extension, Set<String> allowedExtensions) {
        return allowedExtensions.contains(extension.toLowerCase());
    }

    public static boolean isImageExtensionAllowed(String extension) {
        return isExtensionAllowed(extension, ALLOWED_IMAGE_EXTENSIONS);
    }
}