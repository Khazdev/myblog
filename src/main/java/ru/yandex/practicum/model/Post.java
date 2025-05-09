package ru.yandex.practicum.model;

import lombok.Builder;
import lombok.Data;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Builder
@Data
public class Post {
    private Long id;
    private String title;
    private String text;
    private String imagePath;
    private int likesCount;
    private List<Comment> comments;
    private List<String> tags;

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