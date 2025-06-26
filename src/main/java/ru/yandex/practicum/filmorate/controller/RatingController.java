package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.dto.RatingIdAndNameDto;
import ru.yandex.practicum.filmorate.service.RatingService;

import java.util.List;

@RestController
@RequestMapping("/mpa")
@RequiredArgsConstructor
@Slf4j
public class RatingController {

    private final RatingService ratingService;

    @GetMapping
    public ResponseEntity<List<RatingIdAndNameDto>> getAllRatings() {
        return ResponseEntity.ok(ratingService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<RatingIdAndNameDto> getRatingById(@PathVariable int id) {
        return ResponseEntity.ok(ratingService.getById(id));
    }
}
