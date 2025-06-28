package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.dto.GenreLongDto;
import ru.yandex.practicum.filmorate.dto.RatingLongDto;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface FilmStorage {

    Film save(Film film);

    Film update(Film film);

    public Optional<Film> findFilmById(Long id);

    public Collection<Film> getAllFilms();

    public List<GenreLongDto> getGenresByFilmId(Long filmId);

    public RatingLongDto getRatingByFilmId(Long filmId);

    void deleteFilmById(Long id);

    void addLike(Long filmId, Long userId);

    void removeLike(Long filmId, Long userId);

    List<Film> getMostPopularFilms(int count);

}
