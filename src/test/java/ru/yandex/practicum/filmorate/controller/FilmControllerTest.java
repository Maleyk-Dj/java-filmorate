package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

class FilmControllerTest {

    private FilmController controller;

    @BeforeEach
    void setUp() {
        controller = new FilmController();
    }

    @Test
    void shouldAddFilm() {
        Film newFilm = new Film();
        newFilm.setName("New Name");
        newFilm.setDescription("New Desc");
        newFilm.setReleaseDate(LocalDate.of(2001,12,14));
        newFilm.setDuration(90);

        Film result = controller.addFilm(newFilm);
        assertNotNull(result.getId());
        assertEquals("New Name", result.getName());
    }

    @Test
    void shouldUpdateFilm() {
        Film film = new Film();
        film.setName("Name");
        film.setDescription("Desc");
        film.setReleaseDate(LocalDate.of(1945,12,2));
        film.setDuration(120);

        Film added = controller.addFilm(film);

        added.setName("Titanic");

        Film result = controller.updateFilm(added);
        assertEquals("Titanic", result.getName());
    }

    @Test
    void shouldGetAllFilms() {
        Film film1 = new Film();
        film1.setName("Run");
        film1.setDescription("Desc Cool");
        film1.setReleaseDate(LocalDate.of(2014,2,23));
        film1.setDuration(60);

        controller.addFilm(film1);

        Film film2 = new Film();
        film2.setName("Stoop");
        film2.setDescription("Desc Very Cool");
        film2.setReleaseDate(LocalDate.of(1996,7,16));
        film2.setDuration(110);

        controller.addFilm(film2);

        Collection<Film> films = controller.getAllFilms();

        assertEquals(2, films.size());
    }
}











