package ru.yandex.practicum.filmorate.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Rating;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class RatingDbStorage {

    private final JdbcTemplate jdbcTemplate;

    public Optional<Rating> findById(int id) {
        String sql = "SELECT id, name FROM rating WHERE id = ?";
        List<Rating> ratings = jdbcTemplate.query(sql, this::mapRowToRating, id);
        return ratings.stream().findFirst();
    }

    public List<Rating> findAll() {
        String sql = "SELECT id, name FROM rating ORDER BY id";
        return jdbcTemplate.query(sql, this::mapRowToRating);
    }

    private Rating mapRowToRating(ResultSet rs, int rowNum) throws SQLException {
        return new Rating(rs.getInt("id"), rs.getString("name"));
    }
}

