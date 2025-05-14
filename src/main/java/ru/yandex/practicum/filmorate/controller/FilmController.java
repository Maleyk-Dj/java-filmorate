package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {

    private final Map<Long, Film> films = new HashMap<>();

    private long generateId() {
        long maxId = films.keySet().stream().mapToLong(id -> id).max().orElse(0);
        return ++maxId;
    }

    @PostMapping
    public Film addFilm(@Valid @RequestBody Film film) {
        log.info("Добавляем фильм: {}", film);

        film.setId(generateId());
        films.put(film.getId(), film);

        log.info("Фильм успешно добавлен с ID={}", film.getId());
        return film;
    }

    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film film) {
        log.info("Запрос на обновление фильма ID={}", film.getId(), film);
        if (!films.containsKey(film.getId())) {
            String msg = "Фильм с ID=" + film.getId() + " не найден";
            log.error(msg);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, msg);
        }
        films.put(film.getId(), film);

        log.info("Фильм с ID={} успешно обновлён", film.getId());
        return film;
    }

    @GetMapping
    public Collection<Film> getAllFilms() {
        log.info("Запрос на получение всех фильмов ({} шт.)", films.size());
        return films.values();
    }
}
