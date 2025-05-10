package ru.yandex.practicum.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.model.Post;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class JdbcNativePostRepository implements PostRepository {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<Post> findAll(int pageNumber, int pageSize, String search) {
        int offset = (pageNumber - 1) * pageSize;
        String sql;

        if (search != null && !search.isEmpty()) {
            // если тэг есть? TODO
        } else {
            sql = "SELECT * FROM posts ORDER BY id DESC LIMIT ? OFFSET ?";
            return jdbcTemplate.query(sql, (rs, rowNum)->Post.builder()
                    .id(rs.getLong("id"))
                    .title(rs.getString("title"))
                    .text(rs.getString("text"))
                    .imagePath(rs.getString("image_path"))
                    .likesCount(rs.getInt("likes_count"))
                    .tags(Collections.emptyList())
                    .comments(Collections.emptyList())
                    .build(), pageSize, offset);
        }
        return List.of();
    }

    @Override
    public Optional<String> findImagePathById(long postId) {
        String sql = "SELECT image_path FROM posts WHERE id = ?";
        List<String> result = jdbcTemplate.query(sql, (rs, rowNum) -> rs.getString("image_path"), postId);
        return result.isEmpty() ? Optional.empty() : Optional.ofNullable(result.getFirst());
    }

}
