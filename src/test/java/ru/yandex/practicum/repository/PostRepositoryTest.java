package ru.yandex.practicum.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.TestPropertySource;
import ru.yandex.practicum.DAO.PostDAO;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestPropertySource(locations = "classpath:test-application.properties")
class PostRepositoryTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private PostRepository postRepository;

    @BeforeEach
    void setUp() {

        jdbcTemplate.execute("DELETE FROM comments");
        jdbcTemplate.execute("DELETE FROM posts");

        jdbcTemplate.execute("INSERT INTO posts (id, title, text, image_path, likes_count, created_at) " +
                "VALUES (1, 'Post 1', 'Content 1', '/images/post1.jpg', 5, CURRENT_TIMESTAMP)");
        jdbcTemplate.execute("INSERT INTO posts (id, title, text, image_path, likes_count, created_at) " +
                "VALUES (2, 'Post 2', 'Content 2', '/images/post2.jpg', 3, CURRENT_TIMESTAMP)");
        jdbcTemplate.execute("INSERT INTO posts (id, title, text, image_path, likes_count, created_at) " +
                "VALUES (3, 'Post 3', 'Content 3', '/images/post3.jpg', 0, CURRENT_TIMESTAMP)");

    }

    @Test
    void findAll_shouldReturnAllPosts() {
        List<PostDAO> posts = (List<PostDAO>) postRepository.findAll();

        assertNotNull(posts);
        assertEquals(3, posts.size());

        PostDAO post = posts.get(0);
        assertEquals(1L, post.getId());
        assertEquals("Post 1", post.getTitle());
    }

    @Test
    void deleteById_shouldRemovePostFromDatabase() {
        postRepository.deletePostCascade(1L);

        List<PostDAO> posts = (List<PostDAO>) postRepository.findAll();

        PostDAO deletedPost = posts.stream()
                .filter(p -> p.getId().equals(1L))
                .findFirst()
                .orElse(null);
        assertNull(deletedPost);
    }

    @Test
    void save_shouldAddPostToDatabase() {
        jdbcTemplate.execute("DELETE FROM comments");
        jdbcTemplate.execute("DELETE FROM posts");
        PostDAO post = PostDAO.builder()
                .title("Космическое путешествие")
                .text("В 2247 году человечество открыло гиперпространственные врата. Но что ждёт нас по ту сторону?")
                .imagePath("1.jpg")
                .likesCount(2)
                .build();
        PostDAO save = postRepository.save(post);
        List<PostDAO> postDAOList = (List<PostDAO>) postRepository.findAll();
        PostDAO savedPost = postDAOList.stream()
                .filter(p -> p.getId().equals(save.getId()))
                .findFirst()
                .orElse(null);

        assertNotNull(savedPost);
        assertEquals("Космическое путешествие", savedPost.getTitle());
        assertEquals("В 2247 году человечество открыло гиперпространственные врата. Но что ждёт нас по ту сторону?", savedPost.getText());
    }
}