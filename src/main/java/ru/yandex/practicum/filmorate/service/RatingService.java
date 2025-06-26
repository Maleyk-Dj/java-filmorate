package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dto.RatingIdAndNameDto;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.mapper.RatingMapper;
import ru.yandex.practicum.filmorate.model.Rating;
import ru.yandex.practicum.filmorate.repository.RatingDbStorage;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RatingService {

    private final RatingDbStorage ratingStorage;

    public RatingIdAndNameDto getById(Integer id) {
        Rating rating = ratingStorage.findById(id)
                .orElseThrow(() -> new NotFoundException("Рейтинг с id = " + id + " не найден"));

        return new RatingIdAndNameDto(rating.getId(), rating.getName());
    }

    public List<RatingIdAndNameDto> getAll() {
        List<Rating> ratings = ratingStorage.findAll();
        return ratings.stream()
                .map(RatingMapper::mapToRatingIdAndNameDto)
                .collect(Collectors.toList());
    }
}

