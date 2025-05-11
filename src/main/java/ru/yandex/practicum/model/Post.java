package ru.yandex.practicum.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Arrays;
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

//TODO все что ниже перевести в DTO
    public String getTagsAsText() {
        if (tags == null || tags.isEmpty()) {
            return "";
        }
        return String.join(" ", tags);
    }

    public List<String> getTextParts() {
        return text != null
                ? Arrays.asList(text.split("\n"))
                : Collections.emptyList();
    }

    public String getTextPreview() {
        if (text == null || text.isEmpty()) {
            return "";
        }

        int previewLength = Math.min(text.length(), 100);
        return text.substring(0, previewLength) + (text.length() > 100 ? "..." : "");
    }
}