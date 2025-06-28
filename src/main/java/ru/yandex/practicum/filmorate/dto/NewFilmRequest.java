package ru.yandex.practicum.filmorate.dto;

import jakarta.validation.constraints.*;
import lombok.Data;
import ru.yandex.practicum.filmorate.annotations.ValidReleaseDate;

import java.time.LocalDate;
import java.util.List;

@Data
public class NewFilmRequest {
    @NotBlank
    private String name;
    @Size(max = 200)
    private String description;
    @NotNull
    @PastOrPresent
    @ValidReleaseDate
    private LocalDate releaseDate;
    @NotNull
    @Positive
    private Integer duration;

    private RatingDto mpa;
    private List<GenreDto> genres;
}