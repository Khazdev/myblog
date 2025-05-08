package ru.yandex.practicum.model;

public record Paging(int pageNumber, int pageSize, boolean hasNext, boolean hasPrevious) {
}