package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.film.GenreStorage;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GenreService {
    private final GenreStorage genreStorage;

    public Genre getGenreById(int id) {
        return genreStorage.findById(id)
                .orElseThrow(() -> new NotFoundException("Жанр с id= " + id + " не найден"));
    }

    public Collection<Genre> getAllGenres() {
        return genreStorage.findAll();
    }

    public Set <Genre> getGenresByIds(Set <Integer> ids) {
        if (ids==null || ids.isEmpty()) {
            return Set.of();
        }
        return ids.stream()
                .map(this::getGenreById)
                .collect(Collectors.toSet());
    }
}
