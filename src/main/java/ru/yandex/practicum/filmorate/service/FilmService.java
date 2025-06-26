package ru.yandex.practicum.filmorate.service;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dto.*;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.mapper.FilmMapper;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FilmService {

    private final FilmStorage filmStorage;
    private final GenreService genreService;
    private final RatingService ratingService;

    public FilmResponseDto createFilm(NewFilmRequest request) {
        // Преобразование запроса в модель
        Film film = FilmMapper.mapToFilm(request);

        // Получение полной информации о рейтинге и жанрах
        RatingIdAndNameDto mpa = ratingService.getById(request.getMpa().getId());
        List<GenreIdAndNameDto> genres = genreService.getByIds(request.getGenres());

        // Сохраняем фильм
        Film saved = filmStorage.save(film);

        // Строим и возвращаем DTO ответа
        return FilmMapper.buildResponse(saved, mpa, genres);
    }

    public FilmResponseDto updateFilm(UpdateFilmRequest request) {
        Film existing = filmStorage.findFilmById(request.getId())
                .orElseThrow(() -> new NotFoundException("Фильм не найден"));

        RatingIdAndNameDto mpa = ratingService.getById(request.getMpa().getId());

        List<GenreIdAndNameDto> genres = (request.getGenres() == null || request.getGenres().isEmpty())
                ? Collections.emptyList()
                : genreService.getByIds(request.getGenres());

        Film updated = FilmMapper.updateFields(existing, request, mpa, genres);

        filmStorage.update(updated);

        return FilmMapper.buildResponse(updated, mpa, genres);
    }


    public List<FilmResponseDto> getAllFilms() {
        return filmStorage.getAllFilms().stream()
                .map(film -> {
                    RatingIdAndNameDto mpa = filmStorage.getRatingByFilmId(film.getId());
                    List<GenreIdAndNameDto> genres = filmStorage.getGenresByFilmId(film.getId());
                    return FilmMapper.buildResponse(film, mpa, new ArrayList<>(genres));
                })
                .collect(Collectors.toList());
    }

    public FilmResponseDto getFilmById(Long id) {
        Film film = filmStorage.findFilmById(id)
                .orElseThrow(() -> new NotFoundException("Фильм не найден"));

        RatingIdAndNameDto mpa = filmStorage.getRatingByFilmId(id);
        List<GenreIdAndNameDto> genres = filmStorage.getGenresByFilmId(id);

        return FilmMapper.buildResponse(film, mpa, new ArrayList<>(genres));
    }

    public void addLike(Long filmId, Long userId) {
        filmStorage.addLike(filmId, userId);
    }

    public void removeLike(Long filmId, Long userId) {
        filmStorage.removeLike(filmId, userId);
    }

    public List<FilmResponseDto> getMostPopularFilms(int count) {
        return filmStorage.getMostPopularFilms(count).stream()
                .map(film -> {
                    RatingIdAndNameDto mpa = filmStorage.getRatingByFilmId(film.getId());
                    List<GenreIdAndNameDto> genres = filmStorage.getGenresByFilmId(film.getId());
                    return FilmMapper.buildResponse(film, mpa, new ArrayList<>(genres));
                })
                .collect(Collectors.toList());
    }
}
