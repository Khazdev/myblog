package ru.yandex.practicum;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import ru.yandex.practicum.service.CommentService;
import ru.yandex.practicum.service.ImageService;
import ru.yandex.practicum.service.PostService;

import static org.mockito.Mockito.mock;

@TestConfiguration
public class TestConfig {
    @Bean
    @Primary
    public PostService postService() {
        return mock(PostService.class);
    }

    @Bean
    @Primary
    public ImageService imageService() {
        return mock(ImageService.class);
    }

    @Bean
    @Primary
    public CommentService commentService() {
        return mock(CommentService.class);
    }
}