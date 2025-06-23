package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Rating;
import ru.yandex.practicum.filmorate.storage.film.RatingStorage;

import java.util.Collection;

@Service
@RequiredArgsConstructor
public class RatingService {
    private final RatingStorage ratingStorage;

    public Rating getRatingById(int id) {
        return ratingStorage.findById(id)
                .orElseThrow(()->new NotFoundException("Рейтинг с id="+id+" не найден"));
    }

    public Collection<Rating> getAllRatings() {
        return ratingStorage.findAll();
    }
}
