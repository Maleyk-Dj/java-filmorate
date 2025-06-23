package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import jakarta.validation.constraints.*;
import lombok.NoArgsConstructor;
import ru.yandex.practicum.filmorate.annotations.ValidReleaseDate;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Film {

    private Long id;

    @NotBlank(message = "Название фильма не может быть пустым")
    private String name;

    @Size(max = 255, message = "Описание фильма не может быть длинее 200 символов")
    private String description;

    @NotNull(message = "Дата релиза должна быть указана")
    @PastOrPresent(message = "Дата релиза не может быть в будущем")
    @ValidReleaseDate
    private LocalDate releaseDate;

    @NotNull(message = "Продолжительность фильма должна быть указана")
    @Positive(message = "Продолжительность фильма должна быть положительной")
    private long duration;


    private Set<Genre> genres = new HashSet<>();

    @NotNull(message = "Рейтинг должен быть указан")
    private Rating mpa;

    private Set<Long> likes = new HashSet<>();
}