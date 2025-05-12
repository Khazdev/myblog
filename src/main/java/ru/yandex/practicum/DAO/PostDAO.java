package ru.yandex.practicum.DAO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table("POSTS")
public class PostDAO {
    @Id
    private Long id;
    private String title;
    private String text;
    @Column("IMAGE_PATH")
    private String imagePath;
    @Column("LIKES_COUNT")
    private int likesCount;
    @Column("CREATED_AT")
    private LocalDateTime createdAt;

    @Builder.Default
    @Transient
    private List<CommentDAO> comments = Collections.emptyList();
    @Builder.Default
    @Transient
    private List<TagDAO> tags = Collections.emptyList();

}