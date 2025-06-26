package ru.yandex.practicum.filmorate.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dto.GenreIdAndNameDto;
import ru.yandex.practicum.filmorate.dto.RatingIdAndNameDto;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.dto.GenreIdDto;
import ru.yandex.practicum.filmorate.repository.rowmappers.FilmRowMapper;
import ru.yandex.practicum.filmorate.repository.rowmappers.GenreRowMapper;
import ru.yandex.practicum.filmorate.repository.rowmappers.RatingRowMapper;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.sql.*;
import java.sql.Date;
import java.util.*;
import java.util.stream.Collectors;

@Repository
@Qualifier("filmDbStorage")
@RequiredArgsConstructor
public class FilmDbStorage implements FilmStorage {

    private final JdbcTemplate jdbcTemplate;
    private final FilmRowMapper filmRowMapper = new FilmRowMapper();

    @Override
    public Film save(Film film) {
        String sql = "INSERT INTO film (name, description, release_date, duration, rating_id) " +
                "VALUES (?, ?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, film.getName());
            ps.setString(2, film.getDescription());
            ps.setDate(3, Date.valueOf(film.getReleaseDate()));
            ps.setInt(4, (int) film.getDuration());
            ps.setInt(5, film.getMpa().getId());
            return ps;
        }, keyHolder);

        Map<String, Object> keys = keyHolder.getKeys();
        if (keys != null && keys.containsKey("ID")) {
            film.setId(((Number) keys.get("ID")).longValue());
        } else {
            throw new IllegalStateException("Не удалось получить сгенерированный ID фильма");
        }

        saveFilmGenres(film); // вставка жанров
        return film;
    }

    @Override
    public Film update(Film film) {
        String sql = "UPDATE film SET name = ?, description = ?, release_date = ?, duration = ?, rating_id = ? WHERE id = ?";
        jdbcTemplate.update(sql,
                film.getName(),
                film.getDescription(),
                Date.valueOf(film.getReleaseDate()),
                (int) film.getDuration(),
                film.getMpa().getId(),
                film.getId()
        );

        deleteFilmGenres(film.getId());
        saveFilmGenres(film);
        return film;
    }

    @Override
    public Optional<Film> findFilmById(Long id) {
        String sql = "SELECT * FROM film WHERE id = ?";
        List<Film> films = jdbcTemplate.query(sql, filmRowMapper, id);
        return films.stream().findFirst();
    }

    @Override
    public Collection<Film> getAllFilms() {
        String sql = "SELECT * FROM film";
        return jdbcTemplate.query(sql, filmRowMapper);
    }

    @Override
    public void deleteFilmById(Long id) {
        jdbcTemplate.update("DELETE FROM film WHERE id = ?", id);
    }

    // ===== Работа с жанрами =====

    private void saveFilmGenres(Film film) {
        if (film.getGenres() == null) return;

        String sql = "INSERT INTO film_genre (film_id, genre_id) VALUES (?, ?)";

        // Убираем дубликаты жанров по id
        Set<Integer> uniqueGenreIds = film.getGenres().stream()
                .map(GenreIdDto::getId)
                .collect(Collectors.toSet());

        for (Integer genreId : uniqueGenreIds) {
            jdbcTemplate.update(sql, film.getId(), genreId);
        }
    }


    private void deleteFilmGenres(Long filmId) {
        jdbcTemplate.update("DELETE FROM film_genre WHERE film_id = ?", filmId);
    }

    @Override
    public List<GenreIdAndNameDto> getGenresByFilmId(Long filmId) {
        String sql = "SELECT g.id, g.name FROM genre g " +
                "JOIN film_genre fg ON g.id = fg.genre_id " +
                "WHERE fg.film_id = ?" + "ORDER BY g.id";
        return jdbcTemplate.query(sql, new GenreRowMapper(), filmId);
    }

    @Override
    public RatingIdAndNameDto getRatingByFilmId(Long filmId) {
        String sql = "SELECT r.id, r.name FROM rating r " +
                "JOIN film f ON f.rating_id = r.id " +
                "WHERE f.id = ?";
        return jdbcTemplate.queryForObject(sql, new RatingRowMapper(), filmId);
    }

    // ===== Лайки =====

    @Override
    public void addLike(Long filmId, Long userId) {
        String sql = "INSERT INTO film_likes (film_id, user_id) VALUES (?, ?)";
        jdbcTemplate.update(sql, filmId, userId);
    }

    @Override
    public void removeLike(Long filmId, Long userId) {
        String sql = "DELETE FROM film_likes WHERE film_id = ? AND user_id = ?";
        jdbcTemplate.update(sql, filmId, userId);
    }

    @Override
    public List<Film> getMostPopularFilms(int count) {
        String sql = """
                SELECT f.* FROM film f
                LEFT JOIN film_likes fl ON f.id = fl.film_id
                GROUP BY f.id
                ORDER BY COUNT(fl.user_id) DESC
                LIMIT ?
                """;
        return jdbcTemplate.query(sql, filmRowMapper, count);
    }
}
