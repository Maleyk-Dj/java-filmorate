package ru.yandex.practicum.filmorate.dto;

import lombok.*;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class RatingLongDto extends RatingDto {

    private String name;

    public RatingLongDto(Integer id, String name) {
        super(id);
        this.name = name;
    }
}