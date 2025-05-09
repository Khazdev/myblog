package ru.yandex.practicum.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.model.Post;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class JdbcNativePostRepository implements PostRepository {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<Post> findAll() {
        jdbcTemplate.query("select id, title, text, image_path, likes_count from posts",
                (rs, rowNum)->Post.builder()
                        .id(rs.getLong("id"))
                        .title(rs.getString("title"))
                        .text(rs.getString("text"))
                        .imagePath(rs.getString("image_path"))
                        .likesCount(rs.getInt("likes_count"))
                        .build());
        return List.of();
    }
}
