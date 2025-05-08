package ru.yandex.practicum.controller;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import ru.yandex.practicum.model.Comment;
import ru.yandex.practicum.model.Paging;
import ru.yandex.practicum.model.Post;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Controller
public class PostsController {

    @GetMapping("/")
    public String root() {
        return "redirect:/posts";
    }

    @GetMapping("/posts")
    public String getPosts(Model model) {
        Post post = Post.builder()
                .id(1L)
                .text("Тестовый пост")
                .title("Star Wars")
                .likesCount(22)
                .imagePath("")
                .textParts("123")
                .textPreview("444")
                .comments(List.of(
                        Comment.builder().postId(1L).id(92L).text("May the Force be with you").build()
                ))
                .tags(List.of("starwars", "darthvader"))
                .build();

        model.addAttribute("paging", new Paging(1, 2, false, false));
        model.addAttribute("posts", List.of(post));
        model.addAttribute("search", "");
        return "posts";
    }

    @GetMapping("/images/{id}")
    @ResponseBody
    public ResponseEntity<byte[]> getTestImage(@PathVariable("id") Long id) throws IOException {
        try (InputStream in = getClass().getResourceAsStream("/images/vader.jpg")) {
            if (in == null) {
                return ResponseEntity.notFound().build();
            }
            byte[] imageBytes = in.readAllBytes();
            return ResponseEntity.ok()
                    .contentType(MediaType.IMAGE_JPEG)
                    .body(imageBytes);
        }
    }
}