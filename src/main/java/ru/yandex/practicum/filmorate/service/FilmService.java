package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dto.FilmDto;
import ru.yandex.practicum.filmorate.dto.NewFilmRequest;
import ru.yandex.practicum.filmorate.dto.UpdateFilmRequest;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.mapper.FilmMapper;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Rating;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
@Slf4j
@Service
@RequiredArgsConstructor
public class FilmService {
    @Qualifier("filmDbStorage")
    private final FilmStorage filmStorage;
    private final GenreService genreService;
    private final RatingService ratingService;

    public FilmDto createFilm(NewFilmRequest request) {
        log.debug("Создание фильма: {}", request);

        Rating rating = ratingService.getRatingById(request.getRatingId());
        Set<Genre> genres = genreService.getGenresByIds(request.getGenreIds());

        Film film = FilmMapper.mapToFilm(request, rating, genres);
        Film savedFilm = filmStorage.save(film);

        // Загружаем полные данные для возврата
        Film fullFilm = filmStorage.findFilmById(savedFilm.getId())
                .orElseThrow(() -> new IllegalStateException("Фильм сохранён, но не найден"));

        return FilmMapper.mapToFilmDto(fullFilm);
    }

    public FilmDto updateFilm(Long id, UpdateFilmRequest request) {
        log.debug("Попытка обновить фильм с id={} данными: {}", id, request);

        Film film = filmStorage.findFilmById(id)
                .orElseThrow(() -> new NotFoundException("Фильм с id=" + id + " не найден"));

        Rating rating = request.hasRatingId()
                ? ratingService.getRatingById(request.getRatingId())
                : film.getMpa();

        Set<Genre> genres = request.hasGenreIds()
                ? genreService.getGenresByIds(request.getGenreIds())
                : film.getGenres();

        FilmMapper.updateFilmFields(film, request, rating, genres);
        Film updated = filmStorage.update(film);

        log.info("Фильм с id={} успешно обновлён", updated.getId());
        return FilmMapper.mapToFilmDto(updated);
    }

    public Collection<FilmDto> getAllFilms() {
        log.debug("Получение всех фильмов");
        return filmStorage.getAllFilms().stream()
                .map(FilmMapper::mapToFilmDto)
                .collect(Collectors.toList());
    }

    public FilmDto findFilmById(Long id) {
        log.debug("Поиск фильма с id={}", id);
        return filmStorage.findFilmById(id)
                .map(FilmMapper::mapToFilmDto)
                .orElseThrow(() -> new NotFoundException("Фильм с id=" + id + " не найден"));
    }

    public void deleteFilmById(Long id) {
        log.debug("Удаление фильма с id={}", id);
        filmStorage.deleteFilmById(id);
        log.info("Фильм с id={} удалён", id);
    }

    public void addLike(Long filmId, Long userId) {
        log.debug("Попытка добавить лайк: фильм={}, пользователь={}", filmId, userId);

        if (filmStorage.findFilmById(filmId).isEmpty()) {
            throw new NotFoundException("Фильм с id=" + filmId + " не найден");
        }

        filmStorage.addLike(filmId, userId);
        log.info("Лайк добавлен: фильм={}, пользователь={}", filmId, userId);
    }

    public void removeLike(Long filmId, Long userId) {
        log.debug("Попытка удалить лайк: фильм={}, пользователь={}", filmId, userId);

        if (filmStorage.findFilmById(filmId).isEmpty()) {
            throw new NotFoundException("Фильм с id=" + filmId + " не найден");
        }

        boolean removed = filmStorage.removeLike(filmId, userId);
        if (!removed) {
            throw new NotFoundException("Лайк пользователя с id=" + userId + " на фильме с id=" + filmId + " не найден");
        }

        log.info("Лайк удалён: фильм={}, пользователь={}", filmId, userId);
    }

    public List<FilmDto> getTopFilms(int count) {
        log.debug("Запрос топ-{} фильмов по лайкам", count);
        return filmStorage.getTopFilms(count).stream()
                .map(FilmMapper::mapToFilmDto)
                .collect(Collectors.toList());
    }
}


