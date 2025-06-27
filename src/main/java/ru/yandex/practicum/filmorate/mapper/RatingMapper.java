package ru.yandex.practicum.filmorate.mapper;


import ru.yandex.practicum.filmorate.dto.RatingDto;
import ru.yandex.practicum.filmorate.dto.RatingLongDto;
import ru.yandex.practicum.filmorate.model.Rating;

public class RatingMapper {
    public static RatingDto mapToDto(RatingLongDto ratingLongDto) {
        return new RatingDto(ratingLongDto.getId());
    }

    public static RatingLongDto mapToFullDto(Rating rating) {
        return new RatingLongDto(rating.getId(), rating.getName());
    }
}