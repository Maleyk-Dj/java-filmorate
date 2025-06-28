package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.dto.FilmResponseDto;
import ru.yandex.practicum.filmorate.dto.NewFilmRequest;
import ru.yandex.practicum.filmorate.dto.UpdateFilmRequest;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.List;

@RestController
@RequestMapping("/films")
@RequiredArgsConstructor
public class FilmController {

    private final FilmService filmService;

    @PostMapping
    public ResponseEntity<FilmResponseDto> createFilm(@RequestBody @Valid NewFilmRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(filmService.createFilm(request));
    }

    @PutMapping
    public ResponseEntity<FilmResponseDto> updateFilm(@RequestBody @Valid UpdateFilmRequest request) {
        FilmResponseDto updated = filmService.updateFilm(request);
        return ResponseEntity.ok(updated);
    }

    @GetMapping
    public ResponseEntity<List<FilmResponseDto>> getAllFilms() {
        return ResponseEntity.ok(filmService.getAllFilms());
    }

    @GetMapping("/{id}")
    public ResponseEntity<FilmResponseDto> getFilmById(@PathVariable Long id) {
        return ResponseEntity.ok(filmService.getFilmById(id));
    }

    @PutMapping("/{id}/like/{userId}")
    public ResponseEntity<Void> addLike(@PathVariable Long id, @PathVariable Long userId) {
        filmService.addLike(id, userId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}/like/{userId}")
    public ResponseEntity<Void> removeLike(@PathVariable Long id, @PathVariable Long userId) {
        filmService.removeLike(id, userId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/popular")
    public ResponseEntity<List<FilmResponseDto>> getPopularFilms(
            @RequestParam(defaultValue = "10") int count) {
        return ResponseEntity.ok(filmService.getMostPopularFilms(count));
    }
}
