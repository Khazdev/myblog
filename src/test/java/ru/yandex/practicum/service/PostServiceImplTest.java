package ru.yandex.practicum.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.TestPropertySource;
import ru.yandex.practicum.DAO.CommentDAO;
import ru.yandex.practicum.DAO.TagDAO;
import ru.yandex.practicum.model.Comment;
import ru.yandex.practicum.model.Page;
import ru.yandex.practicum.model.Paging;
import ru.yandex.practicum.model.Post;
import ru.yandex.practicum.repository.PostRepository;

import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@TestPropertySource(locations = "classpath:test-application.properties")
class PostServiceImplTest {

    @Autowired
    private PostService postService;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        jdbcTemplate.execute("DELETE FROM post_tag");
        jdbcTemplate.execute("DELETE FROM comments");
        jdbcTemplate.execute("DELETE FROM posts");
        jdbcTemplate.execute("DELETE FROM tags");

        jdbcTemplate.execute("INSERT INTO posts (id, title, text, image_path, likes_count, created_at) " +
                "VALUES (1, 'Post 1', 'Content 1', '/images/post1.jpg', 5, CURRENT_TIMESTAMP)");
        jdbcTemplate.execute("INSERT INTO posts (id, title, text, image_path, likes_count, created_at) " +
                "VALUES (2, 'Post 2', 'Content 2', '/images/post2.jpg', 3, CURRENT_TIMESTAMP)");
        jdbcTemplate.execute("INSERT INTO comments (id, post_id, text, created_at) " +
                "VALUES (1, 1, 'Comment 1', CURRENT_TIMESTAMP)");
        jdbcTemplate.execute("INSERT INTO tags (id, name) VALUES (1, 'tech')");
        jdbcTemplate.execute("INSERT INTO post_tag (post_id, tag_id) VALUES (1, 1)");
        jdbcTemplate.execute("INSERT INTO post_tag (post_id, tag_id) VALUES (2, 1)");
    }

    @Test
    void getPosts_withoutSearch_shouldReturnPaginatedPosts() {
        Page<Post> page = postService.getPosts("", 1, 2);

        assertNotNull(page, "Страница не должна быть null");
        assertEquals(2, page.posts().size(), "Должно вернуться 2 поста");
        assertEquals("Post 1", page.posts().get(0).getTitle(), "Заголовок первого поста должен совпадать");
        assertEquals(1, page.posts().get(0).getComments().size(), "Первый пост должен иметь 1 комментарий");
        assertEquals(List.of("tech"), page.posts().get(0).getTags(), "Первый пост должен иметь тег 'tech'");
        assertEquals("", page.search(), "Поисковый запрос должен быть пустым");
        assertEquals(new Paging(1, 2, false, false), page.paging(), "Пагинация должна совпадать");
    }

    @Test
    void getImagePathByPostId_shouldReturnImagePath() {
        String imagePath = postService.getImagePathByPostId(1L);
        assertEquals("/images/post1.jpg", imagePath, "Путь к изображению должен совпадать");
    }

    @Test
    void getImagePathByPostId_nonExistentPost_shouldThrowException() {
        assertThrows(NoSuchElementException.class, () -> postService.getImagePathByPostId(999L),
                "Должно выбросить исключение для несуществующего поста");
    }

    @Test
    void findPostById_shouldReturnPostWithCommentsAndTags() {
        Post post = postService.findPostById(1L);

        assertNotNull(post, "Пост не должен быть null");
        assertEquals("Post 1", post.getTitle(), "Заголовок поста должен совпадать");
        assertEquals(1, post.getComments().size(), "Пост должен иметь 1 комментарий");
        assertEquals("Comment 1", post.getComments().get(0).getText(), "Текст комментария должен совпадать");
        assertEquals(List.of("tech"), post.getTags(), "Пост должен иметь тег 'tech'");
    }

    @Test
    void createPost_shouldSaveAndReturnPost() {
        //надо очищать бд, потому что h2 не понимает с какого айдишника начинать генерацию, если там уже есть сущности
        jdbcTemplate.execute("DELETE FROM post_tag");
        jdbcTemplate.execute("DELETE FROM comments");
        jdbcTemplate.execute("DELETE FROM posts");
        jdbcTemplate.execute("DELETE FROM tags");
        Post post = Post.builder()
                .title("New Post")
                .text("New Content")
                .imagePath("/images/new.jpg")
                .build();

        Post savedPost = postService.createPost(post);

        assertNotNull(savedPost.getId(), "Сохраненный пост должен иметь ID");
        assertEquals("New Post", savedPost.getTitle(), "Заголовок поста должен совпадать");
        assertEquals("/images/new.jpg", savedPost.getImagePath(), "Путь к изображению должен совпадать");
    }

    @Test
    void updatePost_shouldUpdateExistingPost() {
        Post post = Post.builder()
                .id(1L)
                .title("Updated Post")
                .text("Updated Content")
                .imagePath(null)
                .likesCount(10)
                .comments(List.of(Comment.builder().text("Updated Comment").build()))
                .tags(List.of("updated"))
                .build();

        postService.updatePost(post);
        Post updatedPost = postService.findPostById(1L);

        assertEquals("Updated Post", updatedPost.getTitle(), "Пост обновлен");
        assertEquals("/images/post1.jpg", updatedPost.getImagePath(), "Изображение не перезатерлось");
        assertEquals(10, updatedPost.getLikesCount(), "Обновились лайки");
    }

    @Test
    void updatePost_nonExistentPost_shouldThrowException() {
        Post post = Post.builder()
                .id(999L)
                .title("Non-existent")
                .text("Content")
                .imagePath("/images/test.jpg")
                .likesCount(0)
                .build();

        assertThrows(NoSuchElementException.class, () -> postService.updatePost(post),
                "Апдейт несуществущего поста не возможен");
    }

    @Test
    void deletePost_shouldRemovePostAndRelatedData() {
        postService.deletePost(1L);
        assertThrows(NoSuchElementException.class, () -> postService.findPostById(1L),
                "Удаление несуществущего поста не возможно");

        List<CommentDAO> comments = postRepository.findCommentsByPostId(1L);
        assertTrue(comments.isEmpty(), "Коментарии удалены");
        List<TagDAO> tags = postRepository.findTagsByPostId(1L);
        assertTrue(tags.isEmpty(), "Тэги удалены");
    }

    @Test
    void incrementLikes_shouldIncreaseLikesCount() {
        postService.incrementLikes(1L);
        Post post = postService.findPostById(1L);
        assertEquals(6, post.getLikesCount(), "Лайки увеличились на 1");
    }

    @Test
    void decrementLikes_shouldDecreaseLikesCount() {
        postService.decrementLikes(1L);
        Post post = postService.findPostById(1L);
        assertEquals(4, post.getLikesCount(), "Лайки уменьшились на 1");
    }

    @Test
    void decrementLikes_zeroLikes_shouldNotGoNegative() {
        jdbcTemplate.execute("UPDATE posts SET likes_count = 0 WHERE id = 1");
        postService.decrementLikes(1L);
        Post post = postService.findPostById(1L);
        assertEquals(0, post.getLikesCount(), "Количество лайков должно остаться 0");
    }
}