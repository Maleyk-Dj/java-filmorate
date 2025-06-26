package ru.yandex.practicum.filmorate.mapper;

import lombok.NoArgsConstructor;
import ru.yandex.practicum.filmorate.dto.GenreIdAndNameDto;
import ru.yandex.practicum.filmorate.dto.GenreIdDto;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor
public class GenreMapper {

    public static GenreIdDto mapToGenreIdDto(GenreIdAndNameDto genreIdAndNameDto) {
        GenreIdDto genreIdDto = new GenreIdDto();
        genreIdDto.setId(genreIdAndNameDto.getId());
        return genreIdDto;
    }

    public static GenreIdAndNameDto mapToIdAndNameDto(Genre genre) {
        GenreIdAndNameDto dto = new GenreIdAndNameDto();
        dto.setId(genre.getId());
        dto.setName(genre.getName());
        return dto;
    }

    public static List<GenreIdDto> mapToListGenreIdDto(List<GenreIdAndNameDto> genreIdAndNameDtos) {
        return genreIdAndNameDtos.stream().map(GenreMapper::mapToGenreIdDto).collect(Collectors.toList());
    }
}


