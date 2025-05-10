package ru.yandex.practicum.service;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.model.Post;
import ru.yandex.practicum.repository.PostRepository;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {
    private final PostRepository postRepository;

    public List<Post> getPosts(int pageNumber, int pageSize, String search) {
        return postRepository.findAll(pageNumber, pageSize, search);
    }

    public String getImagePathByPostId(long postId) {
        return postRepository.findImagePathById(postId)
                .orElseThrow(() -> new NoSuchElementException("Пост с id " + postId + " не найден или не содержит image_path"));
    }

}
