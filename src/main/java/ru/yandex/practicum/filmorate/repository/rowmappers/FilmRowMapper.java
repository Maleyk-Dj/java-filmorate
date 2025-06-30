package ru.yandex.practicum.filmorate.repository.rowmappers;

import org.springframework.jdbc.core.RowMapper;
import ru.yandex.practicum.filmorate.dto.GenreDto;
import ru.yandex.practicum.filmorate.dto.GenreLongDto;
import ru.yandex.practicum.filmorate.dto.RatingLongDto;
import ru.yandex.practicum.filmorate.model.Film;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class FilmRowMapper implements RowMapper<Film> {

    @Override
    public Film mapRow(ResultSet rs, int rowNum) throws SQLException {

        Film film = new Film();

        film.setId(rs.getLong("id"));
        film.setName(rs.getString("name"));
        film.setDescription(rs.getString("description"));
        film.setReleaseDate(rs.getDate("release_date").toLocalDate());
        film.setDuration(rs.getInt("duration"));

        RatingLongDto rating = new RatingLongDto(rs.getInt("rating_id"), rs.getString("rating_name"));
        film.setMpa(rating);

        List<GenreLongDto> genreLongDtos = new ArrayList<>();

        if (rs.getInt("genre_id") > 0) {
            genreLongDtos.add(new GenreLongDto(rs.getInt("genre_id"), rs.getString("genre_name")));
        }

        List<GenreDto> genres = convertGenres(genreLongDtos);
        film.setGenres(genres);

        return film;
    }

    private List<GenreDto> convertGenres(List<GenreLongDto> genreLongDtos) {
        return genreLongDtos.stream()
                .map(genreLongDto -> new GenreDto(genreLongDto.getId())) // Преобразуем каждый GenreLongDto в GenreDto
                .collect(Collectors.toList());
    }
}
