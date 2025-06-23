package ru.yandex.practicum.filmorate.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NewFilmRequest {

    @NotBlank(message = "Название фильма не может быть пустым")
    private String name;

    @Size(max = 200, message = "Описание не может быть длиннее 200 символов")
    private String description;

    @NotNull(message = "Дата релиза обязательна")
    @PastOrPresent(message = "Дата релиза не может быть в будущем")
    private LocalDate releaseDate;

    @NotNull
    @Positive(message = "Продолжительность должна быть положительной")
    private Long duration;

    @Positive(message = "ID рейтинга должен быть положительным")
    @NotNull(message = "ID рейтинга должен быть указан")
    private Integer ratingId;

    private Set<Integer> genreIds;
}
