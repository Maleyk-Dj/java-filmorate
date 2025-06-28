package ru.yandex.practicum.filmorate.dto;

import lombok.*;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class GenreLongDto extends GenreDto {
    private String name;

    public GenreLongDto(Integer id, String name) {
        super(id);
        this.name = name;
    }
}