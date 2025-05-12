package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.Duration;
import java.time.Instant;

/**
 * Film.
 */
@Data
@EqualsAndHashCode(of = {"id"})
public class Film {

    private long id;

    @NotBlank(message = "Название фильма не может быть пустым")
    String name;

    @Size(max = 200, message = "Описание фильма не может быть длинее 200 символов")
    String description;

    @NotNull(message = "Дата релиза должна быть указана")
    @PastOrPresent(message = "Дата релиза не может быть в будущем")
    Instant releaseDate;

    @NotNull(message = "Продолжительность фильма должна быть указана")
    @Positive(message = "Продожительность фильма должна быть положительной")
    Duration duration;

}
