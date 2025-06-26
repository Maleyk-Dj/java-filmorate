package ru.yandex.practicum.filmorate.dto;

import lombok.*;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class GenreIdAndNameDto extends GenreIdDto {
    private String name;

    public GenreIdAndNameDto(Integer id, String name) {
        super(id);
        this.name = name;
    }
}