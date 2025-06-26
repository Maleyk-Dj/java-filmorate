package ru.yandex.practicum.filmorate.repository.rowmappers;

import org.springframework.jdbc.core.RowMapper;
import ru.yandex.practicum.filmorate.dto.RatingIdAndNameDto;

import java.sql.ResultSet;
import java.sql.SQLException;

public class RatingRowMapper implements RowMapper<RatingIdAndNameDto> {

    @Override
    public RatingIdAndNameDto mapRow(ResultSet rs, int rowNum) throws SQLException {
        RatingIdAndNameDto mpa = new RatingIdAndNameDto();
        mpa.setId(rs.getInt("id"));
        mpa.setName(rs.getString("name"));
        return mpa;
    }
}
