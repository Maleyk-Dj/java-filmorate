package ru.yandex.practicum.filmorate.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class GenreDbStorage {

    private final JdbcTemplate jdbcTemplate;

    public Optional<Genre> findById(int id) {
        String sql = "SELECT id, name FROM genre WHERE id = ?";
        List<Genre> genres = jdbcTemplate.query(sql, this::mapRowToGenre, id);
        return genres.stream().findFirst();
    }

    public List<Genre> findAll() {
        String sql = "SELECT id, name FROM genre ORDER BY id";
        return jdbcTemplate.query(sql, this::mapRowToGenre);
    }

    private Genre mapRowToGenre(ResultSet rs, int rowNum) throws SQLException {
        return new Genre(rs.getInt("id"), rs.getString("name"));
    }

    public List<Genre> findByIds(Set<Integer> ids) {
        if (ids == null || ids.isEmpty()) return Collections.emptyList();

        String inSql = ids.stream()
                .map(id -> "?")
                .collect(Collectors.joining(", "));

        String sql = String.format("SELECT id, name FROM genre WHERE id IN (%s)", inSql);

        return jdbcTemplate.query(sql, this::mapRowToGenre, ids.toArray());
    }

}


