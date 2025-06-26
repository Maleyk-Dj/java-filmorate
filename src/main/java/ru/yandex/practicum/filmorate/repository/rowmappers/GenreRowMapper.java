package ru.yandex.practicum.filmorate.repository.rowmappers;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dto.GenreIdAndNameDto;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class GenreRowMapper implements RowMapper<GenreIdAndNameDto> {

    @Override
    public GenreIdAndNameDto mapRow(ResultSet rs, int rowNum) throws SQLException {
        GenreIdAndNameDto genre = new GenreIdAndNameDto();
        genre.setId(rs.getInt("id"));
        genre.setName(rs.getString("name"));
        return genre;
    }
}
