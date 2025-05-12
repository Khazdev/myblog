package ru.yandex.practicum.service;

import ru.yandex.practicum.model.Comment;

public interface CommentService {

    Comment addComment(Comment comment);

    void editComment(Long commentId, String text);

    void deleteComment(Long commentId);
}
