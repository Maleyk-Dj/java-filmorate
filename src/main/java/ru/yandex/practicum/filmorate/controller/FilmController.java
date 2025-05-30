package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.*;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {

    private final Map<Long, Film> films = new HashMap<>();

    private long generateId() {
        long maxId = films.keySet().stream().mapToLong(id -> id).max().orElse(0);
        return ++maxId;
    }

    private boolean validate(Film film) {
        LocalDate startDateRelease = LocalDate.of(1895, 12, 28);
        if (film.getReleaseDate().isBefore(startDateRelease))
            throw new ValidationException("Дата релиза должна быть не ранее 28.12.1895");
        return true;
    }

    @PostMapping
    public Film addFilm(@Valid @RequestBody Film film) {
        log.trace("Добавляем фильм: {}", film);

        if (validate(film)) {
            film.setId(generateId());
            films.put(film.getId(), film);
            log.info("Фильм успешно добавлен с ID={}", film.getId());
        }
        return film;
    }

    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film film) {
        log.trace("Запрос на обновление фильма ID={}", film.getId());
        if (film.getId() == 0) {
            throw new ValidationException("Поле id обязательно при обновлении пользователя");
        }
        Film oldFilm = films.get(film.getId());
        if (validate(oldFilm)) {
            oldFilm.setDuration(film.getDuration());
            oldFilm.setName(film.getName());
            oldFilm.setDescription(film.getDescription());
            oldFilm.setReleaseDate(film.getReleaseDate());
        }
        log.info("Фильм с ID={} успешно обновлён", film.getId());
        return oldFilm;
    }

    @GetMapping
    public List<Film> getAllFilms() {
        log.info("Запрос на получение всех фильмов ({} шт.)", films.size());
        return new ArrayList<>(films.values());
    }
}
