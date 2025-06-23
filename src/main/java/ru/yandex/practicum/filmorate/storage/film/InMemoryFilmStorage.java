package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Component
public class InMemoryFilmStorage implements FilmStorage {

   private final Map<Long, Film> films = new HashMap<>();

    @Override
    public Film save(Film film) {
        film.setId(generateId());
        films.put(film.getId(), film);
        log.debug("Фильм сохранён: {}", film);
        return film;
    }

    @Override
    public Film update(Film film) {
        films.put(film.getId(), film);
        log.debug("Фильм с id={} обновлён в хранилище", film.getId());
        return film;
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

    @Override
    public void addLike(Long filmId, Long userId) {
        Film film = films.get(filmId);
        if (film == null) {
            throw new NotFoundException("Фильм с id=" + filmId + " не найден");
        }
        film.getLikes().add(userId);
        log.debug("Добавлен лайк от пользователя с id={} фильму с id={}", userId, filmId);
    }

    @Override
    public boolean removeLike(Long filmId, Long userId) {
        Film film = films.get(filmId);
        if (film == null) {
            throw new NotFoundException("Фильм с id=" + filmId + " не найден");
        }
        boolean removed = film.getLikes().remove(userId);
        if (removed) {
            log.debug("Удалён лайк от пользователя с id={} фильму с id={}", userId, filmId);
        }
        return removed;
    }

    @Override
    public List<Film> getTopFilms(int count) {
        return films.values().stream()
                .sorted(Comparator.comparingInt((Film f) -> f.getLikes().size()).reversed())
                .limit(count)
                .collect(Collectors.toList());
    }

}

