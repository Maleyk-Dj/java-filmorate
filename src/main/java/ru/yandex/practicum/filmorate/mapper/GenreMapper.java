package ru.yandex.practicum.filmorate.mapper;

import lombok.NoArgsConstructor;
import ru.yandex.practicum.filmorate.dto.GenreLongDto;
import ru.yandex.practicum.filmorate.dto.GenreDto;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor
public class GenreMapper {

    public static GenreDto mapToGenreIdDto(GenreLongDto genreIdAndNameDto) {
        GenreDto genreDto = new GenreDto();
        genreDto.setId(genreIdAndNameDto.getId());
        return genreDto;
    }

    public static GenreLongDto mapToIdAndNameDto(Genre genre) {
        GenreLongDto dto = new GenreLongDto();
        dto.setId(genre.getId());
        dto.setName(genre.getName());
        return dto;
    }

    public static List<GenreDto> mapToListGenreIdDto(List<GenreLongDto> genreIdAndNameDtos) {
        return genreIdAndNameDtos.stream().map(GenreMapper::mapToGenreIdDto).collect(Collectors.toList());
    }
}


