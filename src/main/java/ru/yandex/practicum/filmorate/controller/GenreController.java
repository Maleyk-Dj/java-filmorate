package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.dto.GenreLongDto;
import ru.yandex.practicum.filmorate.service.GenreService;

import java.util.List;

@RestController
@RequestMapping("/genres")
@RequiredArgsConstructor
@Slf4j
public class GenreController {

    private final GenreService genreService;

    @GetMapping
    public ResponseEntity<List<GenreLongDto>> getAllGenres() {
        return ResponseEntity.ok(genreService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<GenreLongDto> getGenreById(@PathVariable int id) {
        return ResponseEntity.ok(genreService.getById(id));
    }
}



