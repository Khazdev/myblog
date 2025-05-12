package ru.yandex.practicum.mapper;

import lombok.experimental.UtilityClass;
import ru.yandex.practicum.DAO.PostDAO;
import ru.yandex.practicum.DAO.TagDAO;
import ru.yandex.practicum.model.Post;

import java.util.Optional;
import java.util.stream.Collectors;

@UtilityClass
public class PostDAOToPostMapper {

    public static Post map(PostDAO from) {
        return Optional.ofNullable(from).map(postDAO -> Post.builder()
                        .title(postDAO.getTitle())
                        .text(postDAO.getText())
                        .comments(CommentDAOToCommentMapper.mapList(from.getComments()))
                        .tags(postDAO.getTags().stream().map(TagDAO::getName).collect(Collectors.toList()))
                        .imagePath(postDAO.getImagePath())
                        .likesCount(postDAO.getLikesCount())
                        .id(postDAO.getId())
                        .build())
                .orElse(null);
    }
}
