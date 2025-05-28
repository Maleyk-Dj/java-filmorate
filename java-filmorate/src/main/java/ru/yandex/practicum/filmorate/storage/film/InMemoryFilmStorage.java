package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.*;

@Slf4j
@Component
public class InMemoryFilmStorage implements FilmStorage {

    private final Map<Long, Film> films = new HashMap<>();

    @Override
    public Film addFilm(Film film) {
        log.debug("Попытка добавить фильм: {}", film);
        if (validate(film)) {
            log.debug("Валидация фильма не пройдена: {}", film);
            film.setId(generateId());
            films.put(film.getId(), film);
            log.debug("Фильм успешно добавлен с id={}", film.getId());
        }
        return film;
    }

    @Override
    public Film updateFilm(Film film) {
        log.debug("Попытка обновить данные пользователя с id {}", film.getId());
        if (film.getId() == 0) {
            log.warn("Поле id не задано", film.getId());
            throw new NotFoundException("Поле id обязательно при обновлении фильма.");
        }
        Film oldFilm = films.get(film.getId());
        if (validate(oldFilm)) {
            oldFilm.setDuration(film.getDuration());
            oldFilm.setName(film.getName());
            oldFilm.setDescription(film.getDescription());
            oldFilm.setReleaseDate(film.getReleaseDate());
            log.debug("Фильм с id={} успешно обновлён", film.getId());
        }
        return oldFilm;
    }

    @Override
    public Collection<Film> getAllFilms() {
        log.debug("Попытка вызвать список всех фильмов");
        return films.values();
    }

    @Override
    public Optional<Film> findFilmById(Long id) {
        log.debug("Попытка найти фильм с id={}", id);
        return Optional.ofNullable(films.get(id));
    }

    @Override
    public void deleteFilmById(Long id) {
        log.debug("Попытка удалить фильм с id={}", id);
        if (!films.containsKey(id)) {
            log.debug("Фильм с ID {} не найден", id);
            return;
        }
        log.debug("Фильм с ID {} удален", id);
        films.remove(id);
    }

    private long generateId() {
        log.debug("Попытка сгенерировать ID");
        long maxId = films.keySet().stream().mapToLong(id -> id).max().orElse(0);
        log.debug("Сгенерировано ID  пользователя {}", maxId);
        return ++maxId;
    }

    private boolean validate(Film film) {
        log.debug("Попытка валидация фильма с id {}", film.getId());
        LocalDate startDateRelease = LocalDate.of(1895, 12, 28);
        if (film.getReleaseDate().isBefore(startDateRelease))
            throw new ValidationException("Дата релиза должна быть не ранее 28.12.1895");
        return true;
    }
}

