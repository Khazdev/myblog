package ru.yandex.practicum.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PostsController {

    @GetMapping("/")
    public String root() {
        return "redirect:/posts";
    }

    @GetMapping("/posts")
    public String getPosts() {
        return "posts";
    }
}