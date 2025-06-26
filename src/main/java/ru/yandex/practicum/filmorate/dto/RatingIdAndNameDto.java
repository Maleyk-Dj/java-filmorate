package ru.yandex.practicum.filmorate.dto;

import lombok.*;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class RatingIdAndNameDto extends RatingIdDto {

    private String name;

    public RatingIdAndNameDto(Integer id, String name) {
        super(id);
        this.name = name;
    }
}