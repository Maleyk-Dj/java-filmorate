package ru.yandex.practicum.filmorate.mapper;


import ru.yandex.practicum.filmorate.dto.RatingIdDto;
import ru.yandex.practicum.filmorate.dto.RatingIdAndNameDto;
import ru.yandex.practicum.filmorate.model.Rating;

public class RatingMapper {
    public static RatingIdDto mapToRatingIdDto(RatingIdAndNameDto ratingIdAndNameDto) {
        return new RatingIdDto(ratingIdAndNameDto.getId());
    }

    public static RatingIdAndNameDto mapToRatingIdAndNameDto(Rating rating) {
        return new RatingIdAndNameDto(rating.getId(), rating.getName());
    }
}