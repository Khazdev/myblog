package ru.yandex.practicum.mapper;

import lombok.experimental.UtilityClass;
import ru.yandex.practicum.DAO.PostDAO;
import ru.yandex.practicum.DAO.TagDAO;
import ru.yandex.practicum.model.Post;

import java.util.Optional;
import java.util.stream.Collectors;

@UtilityClass
public class PostToPostDAOMapper {

    public static PostDAO map(Post from) {
        return Optional.ofNullable(from).map(post -> PostDAO.builder()
                        .title(post.getTitle())
                        .text(post.getText())
                        .comments(CommentToCommentDAOMapper.mapList(post.getComments()))
                        .tags(post.getTags().stream().map(PostToPostDAOMapper::mapTagToTagDAO).collect(Collectors.toList()))
                        .imagePath(post.getImagePath())
                        .likesCount(post.getLikesCount())
                        .id(post.getId())
                        .build())
                .orElse(null);
    }

    private static TagDAO mapTagToTagDAO(String tag) {
        return TagDAO.builder().name(tag).build();
    }

}
