package ru.yandex.practicum.filmorate.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dao.mappers.GenreRowMapper;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.film.GenreStorage;

import java.util.Collection;
import java.util.Optional;

@Repository
@Qualifier ("genreDbStorage")
@RequiredArgsConstructor
public class GenreDbStorage implements GenreStorage {
    private final JdbcTemplate jdbcTemplate;

    private static final String FIND_ALL_SQL = "SELECT * FROM genre";
    private static final String FIND_BY_ID_SQL = "SELECT * FROM genre WHERE genre_id = ?";

    @Override
    public Optional<Genre> findById(int id) {
       try {
           Genre genre=jdbcTemplate.queryForObject(FIND_BY_ID_SQL,new GenreRowMapper(),id);
           return Optional.ofNullable(genre);
       } catch (EmptyResultDataAccessException e){
           return Optional.empty();
       }
    }

    @Override
    public Collection<Genre> findAll() {
       return jdbcTemplate.query(FIND_ALL_SQL, new GenreRowMapper());
    }
}
