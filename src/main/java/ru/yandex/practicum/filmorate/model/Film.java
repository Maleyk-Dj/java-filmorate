package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import jakarta.validation.constraints.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
public class Film {

    private Long id;

    @NotBlank(message = "Название фильма не может быть пустым")
    private String name;

    @Size(max = 200, message = "Описание фильма не может быть длинее 200 символов")
    private String description;

    @NotNull(message = "Дата релиза должна быть указана")
    @PastOrPresent(message = "Дата релиза не может быть в будущем")
    private LocalDate releaseDate;

    @NotNull(message = "Продолжительность фильма должна быть указана")
    @Positive(message = "Продолжительность фильма должна быть положительной")
    private long duration;

    @AssertTrue(message = "Дата релиза должна быть не ранее 28.12.1895")
    private boolean isReleaseDateValid() {
        LocalDate startReleaseDate = LocalDate.of(1895, 12, 28);
        return releaseDate == null || !(releaseDate.isBefore(startReleaseDate));
    }

    private Set<Long> likes = new HashSet<>();
}