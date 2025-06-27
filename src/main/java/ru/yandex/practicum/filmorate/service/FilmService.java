package ru.yandex.practicum.filmorate.service;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dto.*;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.mapper.FilmMapper;
import ru.yandex.practicum.filmorate.mapper.GenreMapper;
import ru.yandex.practicum.filmorate.mapper.RatingMapper;
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
        Film film = FilmMapper.mapToFilm(request);

        RatingLongDto mpa = ratingService.getById(request.getMpa().getId());
        List<GenreLongDto> genres = genreService.getByIds(request.getGenres());

        Film saved = filmStorage.save(film);

        FilmResponseDto responseDto = FilmMapper.toDto(saved);
        responseDto.setMpa(mpa);
        responseDto.setGenres(genres);

        return responseDto;
    }

    public FilmResponseDto updateFilm(UpdateFilmRequest request) {
        Film existing = filmStorage.findFilmById(request.getId())
                .orElseThrow(() -> new NotFoundException("Фильм не найден"));

        RatingLongDto mpa = ratingService.getById(request.getMpa().getId());
        List<GenreLongDto> genres = (request.getGenres() == null || request.getGenres().isEmpty())
                ? Collections.emptyList()
                : genreService.getByIds(request.getGenres());

        Film updated = updateFields(existing, request, mpa, genres);

        filmStorage.update(updated);

        FilmResponseDto responseDto = FilmMapper.toDto(updated);
        responseDto.setMpa(mpa);
        responseDto.setGenres(genres);

        return responseDto;
    }

    public static Film updateFields(Film film,
                                    UpdateFilmRequest dto,
                                    RatingLongDto mpa,
                                    List<GenreLongDto> genres) {
        film.setName(dto.getName());
        film.setDescription(dto.getDescription());
        film.setReleaseDate(dto.getReleaseDate());
        film.setDuration(dto.getDuration());
        film.setMpa(RatingMapper.mapToDto(mpa));
        film.setGenres(GenreMapper.mapToListDto(genres));
        return film;
    }

    public List<FilmResponseDto> getAllFilms() {
        return filmStorage.getAllFilms().stream()
                .map(film -> {
                    RatingLongDto mpa = filmStorage.getRatingByFilmId(film.getId());
                    List<GenreLongDto> genres = filmStorage.getGenresByFilmId(film.getId());

                    FilmResponseDto responseDto = FilmMapper.toDto(film);
                    responseDto.setMpa(mpa);
                    responseDto.setGenres(new ArrayList<>(genres));

                    return responseDto;
                })
                .collect(Collectors.toList());
    }

    public FilmResponseDto getFilmById(Long id) {
        Film film = filmStorage.findFilmById(id)
                .orElseThrow(() -> new NotFoundException("Фильм не найден"));

        RatingLongDto mpa = filmStorage.getRatingByFilmId(id);
        List<GenreLongDto> genres = filmStorage.getGenresByFilmId(id);

        FilmResponseDto responseDto = FilmMapper.toDto(film);
        responseDto.setMpa(mpa);
        responseDto.setGenres(new ArrayList<>(genres));

        return responseDto;
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
                    RatingLongDto mpa = filmStorage.getRatingByFilmId(film.getId());
                    List<GenreLongDto> genres = filmStorage.getGenresByFilmId(film.getId());

                    FilmResponseDto responseDto = FilmMapper.toDto(film);

                    responseDto.setMpa(mpa);
                    responseDto.setGenres(new ArrayList<>(genres));

                    return responseDto;
                })
                .collect(Collectors.toList());
    }

}
