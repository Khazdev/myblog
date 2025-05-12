package ru.yandex.practicum.model;

import java.util.List;

public record Page<T>(
        List<T> posts,
        String search,
        Paging paging
) {}