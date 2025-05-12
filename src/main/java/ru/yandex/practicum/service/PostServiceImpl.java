package ru.yandex.practicum.service;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.DAO.CommentDAO;
import ru.yandex.practicum.DAO.PostDAO;
import ru.yandex.practicum.DAO.TagDAO;
import ru.yandex.practicum.mapper.PostDAOToPostMapper;
import ru.yandex.practicum.mapper.PostToPostDAOMapper;
import ru.yandex.practicum.model.*;
import ru.yandex.practicum.repository.PostRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {
    private final PostRepository postRepository;

    public Page<Post> getPosts(String search, int pageNumber, int pageSize) {

        int offset = (pageNumber - 1) * pageSize;

        long total = 0;
        List<PostDAO> postDAOList = new ArrayList<>();
        if (search.isEmpty()) {
            postDAOList = postRepository.findAll(offset, pageSize);
            total = postRepository.count();
        } else {
            postDAOList = postRepository.findByTag("%" + search + "%", offset, pageSize);
            total = postRepository.countByTag("%" + search + "%");
        }

        postDAOList.forEach(post -> {
            List<CommentDAO> comments = postRepository.findCommentsByPostId(post.getId());
            post.setComments(comments);
            List<TagDAO> tags = postRepository.findTagsByPostId(post.getId());
            post.setTags(tags);
        });
        List<Post> posts = postDAOList.stream().map(PostDAOToPostMapper::map).collect(Collectors.toList());
        boolean hasNext = (offset + pageSize) < total;
        boolean hasPrevious = pageNumber > 1;

        return new Page<>(posts, search, new Paging(pageNumber, pageSize, hasNext, hasPrevious));
    }

    public String getImagePathByPostId(long postId) {
        return postRepository.findImagePathById(postId)
                .orElseThrow(() -> new NoSuchElementException("Пост с id " + postId + " не найден или не содержит image_path"));
    }

    @Override
    public Post findPostById(long postId) {
        PostDAO postDAO = postRepository.findById(postId).map(post -> {
                    List<CommentDAO> comments = postRepository.findCommentsByPostId(post.getId());
                    post.setComments(comments);
                    List<TagDAO> tags = postRepository.findTagsByPostId(post.getId());
                    post.setTags(tags);
                    return post;
                })
                .orElseThrow(() -> new NoSuchElementException("Пост с id " + postId + " не найден"));
        return PostDAOToPostMapper.map(postDAO);
    }

    @Override
    @Transactional
    public void updatePost(Post post) {
        PostDAO existing = postRepository.findById(post.getId()).orElseThrow();
        if (post.getImagePath() == null) {
            post.setImagePath(existing.getImagePath());
        }
        postRepository.save(PostToPostDAOMapper.map(post));
    }

    @Override
    @Transactional
    public Post createPost(Post post) {
        PostDAO save = postRepository.save(PostToPostDAOMapper.map(post));
        return PostDAOToPostMapper.map(save);
    }

    @Override
    @Transactional
    public void deletePost(long postId) {
        postRepository.deletePostCascade(postId);
    }

    @Override
    public void incrementLikes(long postId) {
        postRepository.incrementLikes(postId);
    }

    @Override
    public void decrementLikes(long postId) {
        postRepository.decrementLikes(postId);
    }

}
