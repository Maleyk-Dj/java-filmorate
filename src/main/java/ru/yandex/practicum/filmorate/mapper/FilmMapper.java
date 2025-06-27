package ru.yandex.practicum.filmorate.mapper;

import lombok.NoArgsConstructor;
import ru.yandex.practicum.filmorate.dto.*;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

@NoArgsConstructor
public class FilmMapper {

    public static FilmResponseDto buildResponse(Film film) {
        FilmResponseDto result = new FilmResponseDto();
        result.setId(film.getId());
        result.setName(film.getName());
        result.setDescription(film.getDescription());
        result.setDuration(film.getDuration());
        result.setReleaseDate(film.getReleaseDate());
        return result;
    }

    public static Film mapToFilm(NewFilmRequest dto) {
        Film film = new Film();
        film.setName(dto.getName());
        film.setDescription(dto.getDescription());
        film.setReleaseDate(dto.getReleaseDate());
        film.setDuration(dto.getDuration());
        film.setMpa(dto.getMpa());
        film.setGenres(dto.getGenres());
        return film;
    }
}
