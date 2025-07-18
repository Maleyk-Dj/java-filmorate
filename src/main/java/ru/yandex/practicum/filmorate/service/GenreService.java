package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dto.GenreLongDto;
import ru.yandex.practicum.filmorate.dto.GenreDto;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.mapper.GenreMapper;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.repository.GenreDbStorage;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


import java.util.*;

@Service
@RequiredArgsConstructor
public class GenreService {

    private final GenreDbStorage genreDbStorage;

    public List<GenreLongDto> getAll() {
        List<Genre> genres = genreDbStorage.findAll();
        return genres.stream()
                .map(GenreMapper::mapToFullDto)
                .collect(Collectors.toList());
    }

    public GenreLongDto getById(int id) {
        Genre genre = genreDbStorage.findById(id)
                .orElseThrow(() -> new NotFoundException("Жанр с id = " + id + " не найден"));
        return GenreMapper.mapToFullDto(genre);
    }

    public List<GenreLongDto> getByIds(Collection<GenreDto> dtos) {
        if (dtos == null || dtos.isEmpty()) return Collections.emptyList();

        Set<Integer> ids = dtos.stream()
                .map(GenreDto::getId)
                .collect(Collectors.toSet());

        List<Genre> foundGenres = genreDbStorage.findByIds(ids);

        if (foundGenres.size() != ids.size()) {
            Set<Integer> foundIds = foundGenres.stream().map(Genre::getId).collect(Collectors.toSet());
            Set<Integer> missing = new HashSet<>(ids);
            missing.removeAll(foundIds);
            throw new NotFoundException("Жанры с id " + missing + " не найдены");
        }

        return foundGenres.stream()
                .map(GenreMapper::mapToFullDto)
                .collect(Collectors.toList());
    }
}
