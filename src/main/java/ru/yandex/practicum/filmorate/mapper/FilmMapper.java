package ru.yandex.practicum.filmorate.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.yandex.practicum.filmorate.dto.FilmDto;
import ru.yandex.practicum.filmorate.dto.NewFilmRequest;
import ru.yandex.practicum.filmorate.dto.UpdateFilmRequest;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Rating;

import java.util.Set;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class FilmMapper {

    public static FilmDto mapToFilmDto(Film film) {
        FilmDto dto = new FilmDto();
        dto.setId(film.getId());
        dto.setName(film.getName());
        dto.setDescription(film.getDescription());
        dto.setReleaseDate(film.getReleaseDate());
        dto.setDuration(film.getDuration());
        dto.setMpa(film.getMpa());
        dto.setGenres(film.getGenres());
        return dto;
    }

    public static Film mapToFilm(NewFilmRequest request, Rating rating, Set<Genre>genres) {
        Film film = new Film();
        film.setName(request.getName());
        film.setDescription(request.getDescription());
        film.setReleaseDate(request.getReleaseDate());
        film.setDuration(request.getDuration());
        film.setMpa(rating);
        film.setGenres(genres);
        return film;
    }

    public static void updateFilmFields(Film film, UpdateFilmRequest request, Rating rating, Set<Genre> genres) {
        if (request.hasName()){
            film.setName(request.getName());
        }
        if (request.hasDescription()){
            film.setDescription(request.getDescription());
        }
        if (request.hasReleaseDate()){
            film.setReleaseDate(request.getReleaseDate());
        }
        if (request.hasGenreIds()){
            film.setGenres(genres);
        }
    }
}
