package ru.yandex.practicum.repository;

import ru.yandex.practicum.model.Post;

import java.util.List;
import java.util.Optional;

public interface PostRepository {

    List<Post> findAll(int pageNumber, int pageSize, String search);

    Optional<String> findImagePathById(long postId);

    Optional<Post> findById(Long id);

}