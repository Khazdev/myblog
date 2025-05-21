package ru.yandex.practicum.repository;

import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.DAO.CommentDAO;
import ru.yandex.practicum.DAO.PostDAO;
import ru.yandex.practicum.DAO.TagDAO;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostRepository extends
        CrudRepository<PostDAO, Long>,
        PagingAndSortingRepository<PostDAO, Long> {

    @Query("SELECT image_path FROM posts WHERE id = :id")
    Optional<String> findImagePathById(@Param("id") long postId);

    @Query("SELECT * FROM posts ORDER BY id LIMIT :limit OFFSET :offset")
    List<PostDAO> findAll(@Param("offset") long offset, @Param("limit") int limit);

    @Query("SELECT * FROM comments WHERE post_id = :postId")
    List<CommentDAO> findCommentsByPostId(@Param("postId") Long postId);

    @Query("SELECT t.* FROM tags t JOIN post_tag pt ON t.id = pt.tag_id WHERE pt.post_id = :postId")
    List<TagDAO> findTagsByPostId(@Param("postId") Long postId);

    @Modifying
    @Query("DELETE FROM comments WHERE post_id = :postId")
    void deleteCommentsByPostId(@Param("postId") Long postId);

    @Transactional
    default void deletePostCascade(Long postId) {
        deleteCommentsByPostId(postId);
        deleteById(postId);
    }

    @Modifying
    @Query("UPDATE posts p SET p.likes_count = p.likes_count + 1 WHERE p.id = :postId")
    void incrementLikes(@Param("postId") long postId);

    @Modifying
    @Query("UPDATE posts p SET p.likes_count = p.likes_count - 1 WHERE p.id = :postId AND p.likes_count > 0")
    void decrementLikes(@Param("postId") long postId);

    @Query("""
            SELECT p.* FROM posts p
            JOIN post_tag pt ON p.id = pt.post_id
            JOIN tags t ON pt.tag_id = t.id
            WHERE t.name LIKE :tag
            ORDER BY p.created_at DESC
            LIMIT :limit OFFSET :offset
            """)
    List<PostDAO> findByTag(@Param("tag") String tag,
                            @Param("offset") int offset,
                            @Param("limit") int limit);

    @Query("""
            SELECT COUNT(DISTINCT p.id) FROM posts p
            JOIN post_tag pt ON p.id = pt.post_id
            JOIN tags t ON pt.tag_id = t.id
            WHERE t.name LIKE :tag
            """)
    int countByTag(@Param("tag") String tag);
}