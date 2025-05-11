package ru.yandex.practicum.service;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ImageService {

    String saveImage(MultipartFile image) throws IOException;

    Resource getImageAsResource(String relativePath) throws IOException;
}
