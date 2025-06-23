package ru.yandex.practicum.filmorate.dao.mappers;

import org.springframework.jdbc.core.RowMapper;
import ru.yandex.practicum.filmorate.model.Rating;

import java.sql.ResultSet;
import java.sql.SQLException;

public class RatingRowMapper implements RowMapper <Rating> {
    @Override
    public Rating mapRow(ResultSet rs, int rowNum) throws SQLException {
        return Rating.builder()
                .id(rs.getInt("rating_id")) // Убедитесь, что имя колонки указано правильно
                .name(rs.getString("name"))    // Убедитесь, что имя колонки указано правильно
                .description(rs.getString("description")) // Убедитесь, что имя колонки указано правильно
                .build();
    }
}
