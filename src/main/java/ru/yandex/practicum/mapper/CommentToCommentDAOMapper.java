package ru.yandex.practicum.mapper;

import lombok.experimental.UtilityClass;
import ru.yandex.practicum.DAO.CommentDAO;
import ru.yandex.practicum.model.Comment;

import java.util.List;
import java.util.stream.Collectors;

import static org.apache.commons.collections4.CollectionUtils.emptyIfNull;

@UtilityClass
public class CommentToCommentDAOMapper {

    public static CommentDAO map(Comment from) {
        return CommentDAO.builder()
                .text(from.getText())
                .id(from.getId())
                .postId(from.getPostId())
                .build();
    }

    public static List<CommentDAO> mapList(List<Comment> from) {
        return emptyIfNull(from).stream().map(CommentToCommentDAOMapper::map).collect(Collectors.toList());
    }
}
