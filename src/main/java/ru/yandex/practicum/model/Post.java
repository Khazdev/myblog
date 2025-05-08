package ru.yandex.practicum.model;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@Data
public class Post {
    private Long id;
    private String title;
    private String text;
    private String textParts;
    private String textPreview;
    private String imagePath;
    private int likesCount;
    private List<Comment> comments;
    private List<String> tags;
}