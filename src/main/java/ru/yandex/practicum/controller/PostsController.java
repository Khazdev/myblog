package ru.yandex.practicum.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import ru.yandex.practicum.mapper.PostToPostDtoMapper;
import ru.yandex.practicum.model.Comment;
import ru.yandex.practicum.model.Page;
import ru.yandex.practicum.model.Paging;
import ru.yandex.practicum.model.Post;
import ru.yandex.practicum.service.CommentService;
import ru.yandex.practicum.service.ImageService;
import ru.yandex.practicum.service.PostService;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;

@Slf4j
@Controller
@RequiredArgsConstructor
public class PostsController {

    private final PostService postService;
    private final ImageService imageService;
    private final CommentService commentService;

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
        Page<Post> page = postService.getPosts(search, pageNumber, pageSize);
        Paging paging = page.paging();
        model.addAttribute("paging", new Paging(paging.pageNumber(), paging.pageSize(), paging.hasNext(), paging.hasPrevious()));
        model.addAttribute("posts", PostToPostDtoMapper.mapList(page.posts()));
        model.addAttribute("search", search);
        return "posts";
    }

    @GetMapping("/images/{id}")
    public ResponseEntity<Resource> getPostImage(@PathVariable("id") Long id) throws IOException {
        String imagePathByPostId;
        try {
            imagePathByPostId = postService.getImagePathByPostId(id);
            log.debug("Retrieved image path for post {}: {}", id, imagePathByPostId);

            if (imagePathByPostId == null || imagePathByPostId.isBlank()) {
                log.warn("Post {} has empty image_path", id);
                return ResponseEntity.notFound().build();
            }
        } catch (NoSuchElementException e) {
            log.error("Post {} not found or has no image_path", id, e);
            return ResponseEntity.notFound().build();
        }

        try {
            Resource image = imageService.getImageAsResource(imagePathByPostId);
            return ResponseEntity.ok()
                    .contentType(MediaType.IMAGE_JPEG)
                    .body(image);
        } catch (FileNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("posts/add")
    public String showAddForm() {
        return "add-post";
    }

    @GetMapping("posts/{id}")
    public String viewPost(@PathVariable("id") Long id, Model model) {
        model.addAttribute("post", PostToPostDtoMapper.map(postService.findPostById(id)));
        return "post";
    }

    @GetMapping("posts/{id}/edit")
    public String editPost(@PathVariable("id") Long id, Model model) {
        model.addAttribute("post", PostToPostDtoMapper.map(postService.findPostById(id)));
        return "add-post";
    }

    @PostMapping("/posts/{id}")
    public String updatePost(
            @PathVariable("id") Long id,
            @RequestParam("title") String title,
            @RequestParam("text") String text,
            @RequestParam("tags") String tagsText,
            @RequestParam("image") MultipartFile image
    ) throws IOException {
        List<String> tags = Arrays.stream(tagsText.trim().split("\\s+"))
                .filter(s -> !s.isEmpty())
                .toList();

        Post post = Post.builder()
                .title(title)
                .text(text)
                .tags(tags)
                .id(id)
                .build();

        if (!image.isEmpty()) {
            String imagePath = imageService.saveImage(image);
            post.setImagePath(imagePath);
        }

        postService.updatePost(post);
        return "redirect:/posts/" + id;
    }

    @PostMapping("/posts")
    public String createPost(
            @RequestParam("title") String title,
            @RequestParam("text") String text,
            @RequestParam("tags") String tagsText,
            @RequestParam("image") MultipartFile image
    ) throws IOException {
        List<String> tags = Arrays.stream(tagsText.trim().split("\\s+"))
                .filter(s -> !s.isEmpty())
                .toList();

        Post post = Post.builder()
                .title(title)
                .text(text)
                .tags(tags)
                .build();

        if (!image.isEmpty()) {
            String imagePath = imageService.saveImage(image);
            post.setImagePath(imagePath);
        }

        Post createdPost = postService.createPost(post);
        return "redirect:/posts/" + createdPost.getId();
    }

    @PostMapping("posts/{id}/delete")
    public String deletePost(@PathVariable("id") Long id) {
        postService.deletePost(id);
        return "redirect:/posts";
    }

    @PostMapping("/posts/{id}/comments")
    public String addComment(@PathVariable("id") Long id,
                             @RequestParam("text") String text) {
        commentService.addComment(Comment.builder().postId(id).text(text).build());
        return "redirect:/posts/" + id;
    }

    @PostMapping("/posts/{id}/comments/{commentId}")
    public String editComment(@PathVariable("id") Long id,
                              @PathVariable("commentId") Long commentId,
                              @RequestParam("text") String text) {
        commentService.editComment(commentId, text);
        return "redirect:/posts/" + id;
    }

    @PostMapping("/posts/{id}/comments/{commentId}/delete")
    public String deleteComment(@PathVariable("id") Long id,
                                @PathVariable("commentId") Long commentId) {
        commentService.deleteComment(commentId);
        return "redirect:/posts/" + id;
    }

    @PostMapping("posts/{id}/like")
    public String handlePostLike(
            @PathVariable("id") Long id,
            @RequestParam("like") boolean like) {
        if (like) {
            postService.incrementLikes(id);
        } else {
            postService.decrementLikes(id);
        }
        return "redirect:/posts/" + id;
    }
}