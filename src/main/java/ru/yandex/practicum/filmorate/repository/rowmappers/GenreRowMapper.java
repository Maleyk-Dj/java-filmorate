package ru.yandex.practicum.filmorate.repository.rowmappers;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dto.GenreLongDto;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class GenreRowMapper implements RowMapper<GenreLongDto> {

    @Override
    public GenreLongDto mapRow(ResultSet rs, int rowNum) throws SQLException {
        GenreLongDto genre = new GenreLongDto();
        genre.setId(rs.getInt("id"));
        genre.setName(rs.getString("name"));
        return genre;
    }
}
