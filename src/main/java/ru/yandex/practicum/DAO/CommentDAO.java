package ru.yandex.practicum.DAO;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Data
@Builder
@Table("COMMENTS")
public class CommentDAO {
    @Id
    private Long id;
    private String text;
    @Column("POST_ID")
    private Long postId;
    @Column("CREATED_AT")
    private LocalDateTime createdAt;
}