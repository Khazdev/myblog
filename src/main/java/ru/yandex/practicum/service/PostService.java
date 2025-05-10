package ru.yandex.practicum.service;

import ru.yandex.practicum.model.Post;

import java.util.List;

public interface PostService {

    List<Post> getPosts(int pageNumber, int pageSize, String search);

    String getImagePathByPostId(long postId);

}
