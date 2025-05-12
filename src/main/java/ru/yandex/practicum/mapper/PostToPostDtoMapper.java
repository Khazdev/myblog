package ru.yandex.practicum.mapper;

import lombok.experimental.UtilityClass;
import ru.yandex.practicum.model.Post;
import ru.yandex.practicum.Dto.PostDto;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@UtilityClass
public class PostToPostDtoMapper {

    public static List<PostDto> mapList(List<Post> posts) {
        return posts.stream().map(PostToPostDtoMapper::map).collect(Collectors.toList());
    }

    public static PostDto map(Post from) {
        return Optional.ofNullable(from).map(post -> PostDto.builder()
                        .title(post.getTitle())
                        .text(post.getText())
                        .comments(from.getComments())
                        .tags(from.getTags())
                        .imagePath(post.getImagePath())
                        .likesCount(post.getLikesCount())
                        .id(post.getId())
                        .tagsAsText(getTagsAsText(from.getTags()))
                        .textParts(getTextParts(from.getText()))
                        .textPreview(getTextPreview(from.getText()))
                        .build())
                .orElse(null);
    }

    public String getTagsAsText(List<String> tags) {
        if (tags == null || tags.isEmpty()) {
            return "";
        }
        return String.join(" ", tags);
    }

    public List<String> getTextParts(String text) {
        return text != null
                ? Arrays.asList(text.split("\n"))
                : Collections.emptyList();
    }

    public String getTextPreview(String text) {
        if (text == null || text.isEmpty()) {
            return "";
        }

        int previewLength = Math.min(text.length(), 100);
        return text.substring(0, previewLength) + (text.length() > 100 ? "..." : "");
    }
}
