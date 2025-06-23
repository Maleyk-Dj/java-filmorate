package ru.yandex.practicum.filmorate.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.Set;

@Data
public class UpdateFilmRequest {
    private String name;
    private String description;
    private LocalDate releaseDate;
    private Long duration;
    private Integer ratingId;
    private Set<Integer> genreIds;

    public boolean hasName() {
        return (name != null && !name.isBlank());
    }

    public boolean hasDescription() {
        return (description != null && !description.isBlank());
    }

    public boolean hasReleaseDate() {
        return releaseDate != null;
    }
    public boolean hasDuration() {
        return duration != null;
    }

    public boolean hasGenreIds (){
        return genreIds != null && !genreIds.isEmpty();
    }
    public boolean hasRatingId() {
        return ratingId != null;
    }
}
