package ru.yandex.practicum.Dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.yandex.practicum.model.Comment;

import java.util.Collections;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostDto {

    private Long id;
    private String title;
    private String text;
    private String tagsAsText;
    private List<String> textParts;
    private String textPreview;

    private String imagePath;
    private int likesCount;

    @Builder.Default
    private List<Comment> comments = Collections.emptyList();
    @Builder.Default
    private List<String> tags = Collections.emptyList();

}