package ru.yandex.practicum.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.DAO.CommentDAO;
import ru.yandex.practicum.mapper.CommentDAOToCommentMapper;
import ru.yandex.practicum.mapper.CommentToCommentDAOMapper;
import ru.yandex.practicum.model.Comment;
import ru.yandex.practicum.repository.CommentRepository;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;

    public Comment addComment(Comment comment) {
        CommentDAO save = commentRepository.save(CommentToCommentDAOMapper.map(comment));
        return CommentDAOToCommentMapper.map(save);
    }

    @Override
    @Transactional
    public void editComment(Long commentId, String newText) {
        CommentDAO comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new NoSuchElementException("Comment not found with id: " + commentId));
        comment.setText(newText);
        commentRepository.save(comment);
    }

    @Override
    @Transactional
    public void deleteComment(Long commentId) {
        if (!commentRepository.existsById(commentId)) {
            throw new NoSuchElementException("Comment not found with id: " + commentId);
        }
        commentRepository.deleteById(commentId);
    }


}