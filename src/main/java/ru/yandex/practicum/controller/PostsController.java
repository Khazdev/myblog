package ru.yandex.practicum.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import ru.yandex.practicum.model.Paging;
import ru.yandex.practicum.model.Post;
import ru.yandex.practicum.service.PostServiceImpl;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.NoSuchElementException;

@Slf4j
@Controller
@RequiredArgsConstructor
public class PostsController {

    private final PostService postService;

    @GetMapping("/")
    public String root() {
        return "redirect:/posts";
    }

    @GetMapping("/posts")
    public String getPosts(
            @RequestParam(name = "search", defaultValue = "") String search,
            @RequestParam(name = "pageNumber", defaultValue = "1") int pageNumber,
            @RequestParam(name = "pageSize", defaultValue = "10") int pageSize,
            Model model) {
        List<Post> posts = postService.getPosts(pageNumber, pageSize, search);

        model.addAttribute("paging", new Paging(1, 2, false, false));
        model.addAttribute("posts", posts);
        model.addAttribute("search", "");
        return "posts";
    }

    @GetMapping("/images/{id}")
    public ResponseEntity<Resource> getPostImage(@PathVariable("id") Long id) throws IOException {
        String imagePathByPostId;
        try {
            imagePathByPostId = postService.getImagePathByPostId(id);
            log.debug("Retrieved image path for post {}: {}", id, imagePathByPostId);

            // Добавляем проверку на пустую строку
            if (imagePathByPostId.isBlank()) {
                log.warn("Post {} has empty image_path", id);
                return ResponseEntity.notFound().build();
            }
        } catch (NoSuchElementException e) {
            log.error("Post {} not found or has no image_path", id, e);
            return ResponseEntity.notFound().build();
        }

        Path projectRoot = Paths.get("").toAbsolutePath(); // Абсолютный путь от того места, где лежит томкат
        Path imageFile = projectRoot.resolve(imagePathByPostId).normalize();

        if (!Files.exists(imageFile)) {
            return ResponseEntity.notFound().build();
        }

        if (!imageFile.startsWith(projectRoot)) {
            return ResponseEntity.notFound().build();
        }

        Resource image = new UrlResource(imageFile.toUri());

        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG)
                .body(image);
    }

    @GetMapping("posts/add")
    public String showAddForm() {
        return "add-post";
    }

    @GetMapping("posts/{id}")
    public String viewPost(@PathVariable("id") Long id, Model model) {
        model.addAttribute("post", postService.findPostById(id));
        return "post";
    }

}