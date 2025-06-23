package ru.yandex.practicum.filmorate.dao;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dao.mappers.FilmRowMapper;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Rating;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Repository
@Primary
@Qualifier("filmDbStorage")
@RequiredArgsConstructor
public class FilmDbStorage implements FilmStorage {
    private final JdbcTemplate jdbcTemplate;
    private final FilmRowMapper filmRowMapper;

    private static final String INSERT_FILM_SQL = "INSERT INTO films (name, description, release_date, duration, rating_id) VALUES (?, ?, ?, ?, ?)";
    private static final String UPDATE_FILM_SQL = "UPDATE films SET name = ?, description = ?, release_date = ?, duration = ?, rating_id = ? WHERE film_id = ?";
    private static final String DELETE_FILM_SQL = "DELETE FROM films WHERE film_id = ?";
    private static final String FIND_FILM_BY_ID_SQL = "SELECT * FROM films WHERE film_id = ?";

    @Override
    public Film save(Film film) {
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(INSERT_FILM_SQL, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, film.getName());
            ps.setString(2, film.getDescription());
            ps.setDate(3, Date.valueOf(film.getReleaseDate()));
            ps.setLong(4, film.getDuration());
            ps.setInt(5, film.getMpa().getId());
            return ps;
        }, keyHolder);

        long id = Optional.ofNullable(keyHolder.getKey())
                .map(Number::longValue)
                .orElseThrow(() -> new IllegalStateException("Не удалось получить ID созданного фильма"));

        film.setId(id);
        insertFilmGenres(id, film.getGenres());

        return film;
    }

    @Override
    public Film update(Film film) {
        int rows = jdbcTemplate.update(UPDATE_FILM_SQL,
                film.getName(),
                film.getDescription(),
                Date.valueOf(film.getReleaseDate()),
                film.getDuration(),
                film.getMpa().getId(),
                film.getId());

        if (rows == 0) {
            throw new NotFoundException("Фильм с id=" + film.getId() + " не найден");
        }

        deleteFilmGenres(film.getId());
        insertFilmGenres(film.getId(), film.getGenres());

        // Подгрузка полной информации
        return findFilmById(film.getId()).orElseThrow(() ->
                new IllegalStateException("Фильм обновлён, но не найден при повторной выборке"));
    }


    @Override
    public Optional<Film> findFilmById(Long id) {
        try {
            Film film = jdbcTemplate.queryForObject("SELECT * FROM films WHERE film_id = ?", filmRowMapper, id);
            if (film == null) return Optional.empty();

            Map<Long, Film> map = Map.of(id, film);
            loadGenres(map);
            loadRatings(map);
            loadLikes(map);

            return Optional.of(film);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public Collection<Film> getAllFilms() {
        String sql = "SELECT * FROM films";
        List<Film> films = jdbcTemplate.query(sql, (rs, rowNum) ->
                Film.builder()
                        .id(rs.getLong("film_id"))
                        .name(rs.getString("name"))
                        .description(rs.getString("description"))
                        .releaseDate(rs.getDate("release_date").toLocalDate())
                        .duration(rs.getLong("duration"))
                        .mpa(Rating.builder()
                                .id(rs.getInt("rating_id"))
                                .build()) // пока только id, заполним имя при сборке
                        .genres(new HashSet<>()) // заполним ниже
                        .build()
        );

        if (films.isEmpty()) {
            return films;
        }

        Map<Long, Film> filmMap = films.stream()
                .collect(Collectors.toMap(Film::getId, Function.identity()));

        // Подгружаем жанры одним запросом
        loadGenres(filmMap);

        // Подгружаем MPA одним запросом
        loadRatings(filmMap);

        // Подгружаем лайки, если нужно
        loadLikes(filmMap);

        return films;
    }
    @Override
    public void deleteFilmById(Long id) {
        jdbcTemplate.update(DELETE_FILM_SQL, id);
    }

    @Override
    public void addLike(Long filmId, Long userId) {
        String sql = "INSERT INTO film_likes (film_id, user_id) VALUES (?, ?) " +
                "ON DUPLICATE KEY UPDATE film_id = film_id";
        jdbcTemplate.update(sql, filmId, userId);
    }

    @Override
    public boolean removeLike(Long filmId, Long userId) {
        String sql = "DELETE FROM film_likes WHERE film_id = ? AND user_id = ?";
        int rows = jdbcTemplate.update(sql, filmId, userId);
        return rows > 0;
    }

    @Override
    public List<Film> getTopFilms(int count) {
        String sql = """
            SELECT f.*, COUNT(fl.user_id) AS likes_count
            FROM films f
            LEFT JOIN film_likes fl ON f.film_id = fl.film_id
            GROUP BY f.film_id
            ORDER BY likes_count DESC
            LIMIT ?
        """;

        List<Film> films = jdbcTemplate.query(sql, filmRowMapper, count);
        if (films.isEmpty()) return films;

        Map<Long, Film> filmMap = films.stream()
                .collect(Collectors.toMap(Film::getId, Function.identity()));

        loadGenres(filmMap);
        loadLikes(filmMap);
        loadRatings(filmMap);

        return films;
    }

    private void insertFilmGenres(Long filmId, Set<Genre> genres) {
        if (genres == null || genres.isEmpty()) return;

        String sql = "INSERT INTO film_genre (film_id, genre_id) VALUES (?, ?)";
        for (Genre genre : genres) {
            jdbcTemplate.update(sql, filmId, genre.getId());
        }
    }

    private void deleteFilmGenres(Long filmId) {
        jdbcTemplate.update("DELETE FROM film_genre WHERE film_id = ?", filmId);
    }

    private void loadGenres(Map<Long, Film> filmMap) {
        if (filmMap.isEmpty()) return;

        String sql = String.format("""
            SELECT fg.film_id, g.genre_id, g.name
            FROM film_genre fg
            JOIN genre g ON fg.genre_id = g.genre_id
            WHERE fg.film_id IN (%s)
        """, filmMap.keySet().stream().map(String::valueOf).collect(Collectors.joining(",")));

        jdbcTemplate.query(sql, rs -> {
            long filmId = rs.getLong("film_id");
            Genre genre = new Genre(rs.getInt("genre_id"), rs.getString("name"));
            filmMap.get(filmId).getGenres().add(genre);
        });
    }



    private void loadLikes(Map<Long, Film> filmMap) {
        String sql = String.format("""
            SELECT film_id, user_id
            FROM film_likes
            WHERE film_id IN (%s)
        """, filmMap.keySet().stream().map(String::valueOf).collect(Collectors.joining(",")));

        jdbcTemplate.query(sql, rs -> {
            long filmId = rs.getLong("film_id");
            long userId = rs.getLong("user_id");
            filmMap.get(filmId).getLikes().add(userId);
        });
    }
    private void loadRatings(Map<Long, Film> filmMap) {
        Set<Integer> ratingIds = filmMap.values().stream()
                .map(f -> f.getMpa().getId())
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        if (ratingIds.isEmpty()) return;

        String sql = String.format("""
            SELECT rating_id, name, description
            FROM rating
            WHERE rating_id IN (%s)
        """, ratingIds.stream().map(String::valueOf).collect(Collectors.joining(",")));

        Map<Integer, Rating> ratingMap = jdbcTemplate.query(sql, rs -> {
            Map<Integer, Rating> map = new HashMap<>();
            while (rs.next()) {
                map.put(rs.getInt("rating_id"),
                        new Rating(rs.getInt("rating_id"),
                                rs.getString("name"),
                                rs.getString("description")));
            }
            return map;
        });

        filmMap.values().forEach(f -> f.setMpa(ratingMap.get(f.getMpa().getId())));
    }

}


