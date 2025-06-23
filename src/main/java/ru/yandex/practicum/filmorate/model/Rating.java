package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Rating {
    private Integer id;
    @NotBlank(message = "Название рейтинга не может быть пустым")
    private String name;

    @NotBlank(message = "Описание рейтинга не может быть пустым")
    private String description;
}
