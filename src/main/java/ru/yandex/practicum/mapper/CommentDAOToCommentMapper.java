package ru.yandex.practicum.mapper;

import lombok.experimental.UtilityClass;
import ru.yandex.practicum.DAO.CommentDAO;
import ru.yandex.practicum.model.Comment;

import java.util.List;
import java.util.stream.Collectors;

import static org.apache.commons.collections4.CollectionUtils.emptyIfNull;

@UtilityClass
public class CommentDAOToCommentMapper {

    public static Comment  map(CommentDAO from) {
        return Comment.builder()
                .text(from.getText())
                .id(from.getId())
                .postId(from.getPostId())
                .build();
    }

    public static List<Comment> mapList(List<CommentDAO> from) {
        return emptyIfNull(from).stream().map(CommentDAOToCommentMapper::map).collect(Collectors.toList());
    }
}
