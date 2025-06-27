package ru.yandex.practicum.filmorate.repository.rowmappers;

import org.springframework.jdbc.core.RowMapper;
import ru.yandex.practicum.filmorate.dto.RatingLongDto;

import java.sql.ResultSet;
import java.sql.SQLException;

public class RatingRowMapper implements RowMapper<RatingLongDto> {

    @Override
    public RatingLongDto mapRow(ResultSet rs, int rowNum) throws SQLException {
        RatingLongDto mpa = new RatingLongDto();
        mpa.setId(rs.getInt("id"));
        mpa.setName(rs.getString("name"));
        return mpa;
    }
}
