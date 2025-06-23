package ru.yandex.practicum.filmorate.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dao.mappers.RatingRowMapper;
import ru.yandex.practicum.filmorate.model.Rating;
import ru.yandex.practicum.filmorate.storage.film.RatingStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Optional;

@Repository
@Qualifier ("ratingDbStorage")
@RequiredArgsConstructor
public class RatingDbStorage implements RatingStorage {
    private final JdbcTemplate jdbcTemplate;
    private static final String FIND_ALL_SQL="SELECT * FROM rating";
    private static final String FIND_BY_ID_SQL = "SELECT * FROM rating WHERE rating_id = ?";

    @Override
    public Optional<Rating> findById(int id) {
        try {
            Rating rating=jdbcTemplate.queryForObject(FIND_BY_ID_SQL,new RatingRowMapper(),id);
            return Optional.ofNullable(rating);
        } catch (EmptyResultDataAccessException e){
            return Optional.empty();
        }
    }
    @Override
    public Collection<Rating> findAll() {
        return jdbcTemplate.query(FIND_ALL_SQL, new RatingRowMapper());
    }

}
