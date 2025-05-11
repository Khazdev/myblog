package ru.yandex.practicum.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.model.Post;

import java.sql.PreparedStatement;
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

    public Optional<Post> findById(Long id) {
        String sql = "SELECT * FROM posts WHERE id = ?";

        List<Post> result = jdbcTemplate.query(sql, (rs, rowNum) -> Post.builder()
                .id(rs.getLong("id"))
                .title(rs.getString("title"))
                .text(rs.getString("text"))
                .imagePath(rs.getString("image_path"))
                .likesCount(rs.getInt("likes_count"))
                .build(), id);

        return result.stream().findFirst();
    }

    public Post save(Post post) {
        if (post.getId() == null) {
            return createPost(post);
        } else {
            return updatePost(post);
        }
    }

    private Post createPost(Post post) {
        String sql = """
        INSERT INTO posts (title, text, image_path, likes_count)
        VALUES (?, ?, ?, ?)
        """;

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id"});
            ps.setString(1, post.getTitle());
            ps.setString(2, post.getText());
            ps.setString(3, post.getImagePath());
            ps.setInt(4, post.getLikesCount());
            return ps;
        }, keyHolder);

        Number key = keyHolder.getKey();
        if (key == null) {
            throw new IllegalStateException("Failed to insert post");
        }

        post.setId(key.longValue());
        return post;
    }

    private Post updatePost(Post post) {
        String sql = """
            UPDATE posts
            SET title = ?, text = ?, image_path = ?, likes_count = ?
            WHERE id = ?
            """;

        int updated = jdbcTemplate.update(
                sql,
                post.getTitle(),
                post.getText(),
                post.getImagePath(),
                post.getLikesCount(),
                post.getId()
        );

        if (updated == 0) {
            throw new IllegalStateException("Post not found with id: " + post.getId());
        }

        return post;
    }

}
