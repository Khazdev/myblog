package ru.yandex.practicum.service;

import ru.yandex.practicum.model.Page;
import ru.yandex.practicum.model.Post;

public interface PostService {

    Page<Post> getPosts(String search, int pageNumber, int pageSize);

    String getImagePathByPostId(long postId);

    Post findPostById(long postId);

    void updatePost(Post post);

    Post createPost(Post post);

    void deletePost(long postId);

    void incrementLikes(long postId);

    void decrementLikes(long postId);
}
