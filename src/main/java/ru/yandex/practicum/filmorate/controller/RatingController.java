package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Rating;
import ru.yandex.practicum.filmorate.service.RatingService;

import java.util.Collection;

@Slf4j
@RestController
@RequestMapping("/ratings")
@RequiredArgsConstructor
public class RatingController {

    private final RatingService ratingService;

    @GetMapping
    public ResponseEntity<Collection<Rating>> getAllRatings() {
        log.debug("Вызов метода getAllRatings");
        return ResponseEntity.ok(ratingService.getAllRatings());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Rating> getRatingById(@PathVariable int id) {
        log.debug("Вызов метода getRatingById: id={}", id);
        return ResponseEntity.ok(ratingService.getRatingById(id));
    }
}
