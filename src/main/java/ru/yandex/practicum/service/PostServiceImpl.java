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

    @Override
    public Post findPostById(long postId) {
        return postRepository.findById(postId)
                .orElseThrow(() -> new NoSuchElementException("Пост с id " + postId + " не найден"));
    }

    @Override
    public void updatePost(Post post) {
        postRepository.save(post);
    }

    @Override
    public Post createPost(Post post) {
        return postRepository.save(post);
    }

    @Override
    public void deletePost(long postId) {
        postRepository.deletePost(postId);
    }

}
