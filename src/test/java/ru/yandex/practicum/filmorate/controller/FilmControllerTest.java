package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;

import static org.junit.jupiter.api.Assertions.*;

class FilmControllerTest {

    private FilmController controller;
    private Instant earliest;

    @BeforeEach
    void setUp() {
        controller = new FilmController();
        earliest = LocalDate.of(1895, 12, 28).atStartOfDay(ZoneId.systemDefault()).toInstant();
    }

    @Test
    void addFilm_validFilm() {
        Film newFilm = new Film();
        newFilm.setName("Name");
        newFilm.setDescription("Desc");
        newFilm.setReleaseDate(earliest.plusSeconds(1));
        newFilm.setDuration(Duration.ofMinutes(90));

        Film result = controller.addFilm(newFilm);
        assertNotNull(result.getId());
        assertEquals("Name", result.getName());
    }

    @Test
    void addFilm_validFilm_earliestRelease() {
        Film newFilm1 = new Film();
        newFilm1.setName("Name");
        newFilm1.setDescription("Desc");
        newFilm1.setReleaseDate(earliest.minusSeconds(1));
        newFilm1.setDuration(Duration.ofMinutes(90));

        ValidationException ex = assertThrows(ValidationException.class, () -> controller.addFilm(newFilm1));
        assertTrue(ex.getMessage().contains("Дата релиза не может быть раньше 28 декабря 1895 года."));

    }

    @Test
    void addFilm_blankName_throws() {
        Film film2 = new Film();
        film2.setName(" ");
        film2.setDescription("Desc");
        film2.setReleaseDate(earliest.plusSeconds(1));
        film2.setDuration(Duration.ofMinutes(15));

        ValidationException ex = assertThrows(ValidationException.class, () -> controller.addFilm(film2));
        assertTrue(ex.getMessage().contains("Название фильма не может быть пустым."));
    }

    @Test
    void addFilm_tooLongDescription_throws() {
        Film film3 = new Film();
        film3.setName("Name");
        film3.setDescription("x".repeat(201));
        film3.setReleaseDate(earliest.plusSeconds(1));
        film3.setDuration(Duration.ofMinutes(10));

        ValidationException ex = assertThrows(ValidationException.class, () -> controller.addFilm(film3));
        assertTrue(ex.getMessage().contains("Описание фильма не может быть длинее 200 символов."));
    }

    @Test
    void addFilm_nonPositiveDuration_throws() {
        Film film4 = new Film();
        film4.setName("Name");
        film4.setDescription("Desc");
        film4.setReleaseDate(earliest.plusSeconds(1));
        film4.setDuration(Duration.ZERO);

        ValidationException ex = assertThrows(ValidationException.class, () -> controller.addFilm(film4));
        assertTrue(ex.getMessage().contains("Продолжительность фильма должна быть положительной."));
    }
}




