package ru.yandex.practicum.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.yandex.practicum.model.Page;
import ru.yandex.practicum.model.Paging;
import ru.yandex.practicum.model.Post;
import ru.yandex.practicum.service.CommentService;
import ru.yandex.practicum.service.ImageService;
import ru.yandex.practicum.service.PostService;

import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PostsController.class)
class PostsControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private PostService postService;
    @MockitoBean
    private ImageService imageService;
    @MockitoBean
    private CommentService commentService;

    @Test
    void getPosts_shouldReturnHtmlWithPosts() throws Exception {
        Post post = Post.builder()
                .title("New Post")
                .text("New Content")
                .imagePath("/images/new.jpg")
                .build();
        when(postService.getPosts(anyString(), anyInt(), anyInt()))
                .thenReturn(new Page<>(List.of(post), "", new Paging(0, 5, true, false)));
        mockMvc.perform(get("/posts"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith("text/html"))
                .andExpect(view().name("posts"))
                .andExpect(model().attributeExists("posts"));
    }

    @Test
    void createPost_shouldAddPostToDbAndRedirect() throws Exception {
        MockMultipartFile imageFile = new MockMultipartFile(
                "image",
                "test.jpg",
                "image/jpeg",
                new byte[22]);
        when(postService.createPost(any(Post.class))).thenReturn(Post.builder()
                .id(1L).build());
        when(imageService.saveImage(imageFile))
                .thenReturn("image_path");
        mockMvc.perform(multipart("/posts")
                        .file(imageFile)
                        .param("title", "New Post")
                        .param("text", "New Content")
                        .param("tags", "tag1 tag2"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("/posts/*"));

        verify(postService, times(1)).createPost(any(Post.class));
        verify(imageService, times(1)).saveImage(imageFile);
    }

    @Test
    void deletePost_shouldRemovePostFromDatabaseAndRedirect() throws Exception {

        mockMvc.perform(post("/posts/1/delete"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/posts"));
        verify(postService, times(1)).deletePost(anyLong());
    }
}