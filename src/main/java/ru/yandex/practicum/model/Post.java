package ru.yandex.practicum.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collections;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Post {

    private Long id;
    private String title;
    private String text;
    private String imagePath;
    private int likesCount;
    @Builder.Default
    private List<Comment> comments = Collections.emptyList();
    @Builder.Default
    private List<String> tags = Collections.emptyList();

}