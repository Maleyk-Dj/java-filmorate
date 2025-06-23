package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.dto.FilmDto;
import ru.yandex.practicum.filmorate.dto.NewFilmRequest;
import ru.yandex.practicum.filmorate.dto.UpdateFilmRequest;
import ru.yandex.practicum.filmorate.exception.ConditionsNotMetException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.mapper.FilmMapper;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.Collection;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/films")
@RequiredArgsConstructor
public class FilmController {

    private final FilmService filmService;

    @PostMapping
    public ResponseEntity<FilmDto> createFilm(@Valid @RequestBody NewFilmRequest request) {
        FilmDto filmDto = filmService.createFilm(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(filmDto);
    }



    @PutMapping("/{id}")
    public ResponseEntity<FilmDto> updateFilm(@PathVariable Long id, @Valid @RequestBody UpdateFilmRequest request) {
        log.debug("Вызов метода updateFilm: id={}, request={}", id, request);
        FilmDto updatedFilm = filmService.updateFilm(id, request);
        return ResponseEntity.ok(updatedFilm);
    }

    @GetMapping
    public ResponseEntity<Collection<FilmDto>> getAllFilms() {
        log.debug("Вызов метода getAllFilms");
        return ResponseEntity.ok(filmService.getAllFilms());
    }

    @GetMapping("/{id}")
    public ResponseEntity<FilmDto> getFilmById(@PathVariable Long id) {
        log.debug("Вызов метода getFilmById: id={}", id);
        return ResponseEntity.ok(filmService.findFilmById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFilmById(@PathVariable Long id) {
        log.debug("Вызов метода deleteFilmById: id={}", id);
        filmService.deleteFilmById(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/like/{userId}")
    public ResponseEntity<Void> addLike(@PathVariable Long id, @PathVariable Long userId) {
        log.debug("Вызов метода addLike: filmId={}, userId={}", id, userId);
        filmService.addLike(id, userId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}/like/{userId}")
    public ResponseEntity<Void> removeLike(@PathVariable Long id, @PathVariable Long userId) {
        log.debug("Вызов метода removeLike: filmId={}, userId={}", id, userId);
        filmService.removeLike(id, userId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/popular")
    public ResponseEntity<List<FilmDto>> getPopularFilms(@RequestParam(defaultValue = "10") int count) {
        log.debug("Вызов метода getPopularFilms: count={}", count);
        if (count <= 0) {
            throw new ConditionsNotMetException("Параметр count должен быть положительным");
        }
        return ResponseEntity.ok(filmService.getTopFilms(count));
    }
}

