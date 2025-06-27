package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.repository.FilmDbStorage;
import ru.yandex.practicum.filmorate.repository.UserDbStorage;
import ru.yandex.practicum.filmorate.repository.rowmappers.FilmRowMapper;
import ru.yandex.practicum.filmorate.repository.rowmappers.UserRowMapper;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Import({UserDbStorage.class, UserRowMapper.class, FilmDbStorage.class, FilmRowMapper.class})
class FilmoRateApplicationTests {
    private final UserDbStorage userStorage;

    @Test
    public void testFindUserById() {
        // Создаем тестового пользователя, если его нет в базе данных
        User user = new User("test@example.com", "testuser", "Test User", LocalDate.of(1990, 1, 1));
        user = userStorage.save(user);  // Сохраняем пользователя в базе данных

        // Пытаемся найти пользователя по ID
        Optional<User> userOptional = userStorage.findById(user.getId());

        // Проверяем, что пользователь был найден
        User finalUser = user;
        assertThat(userOptional)
                .isPresent()
                .hasValueSatisfying(u ->
                        assertThat(u).hasFieldOrPropertyWithValue("id", finalUser.getId())
                );
    }

    @Test
    void testSaveUser() {
        // Создаём нового пользователя
        User user = new User("test@example.com", "testuser", "Test User", LocalDate.of(1990, 1, 1));

        // Сохраняем пользователя в базе данных
        User savedUser = userStorage.save(user);

        // Проверяем, что ID был назначен и пользователь не null
        assertThat(savedUser).isNotNull();
        assertThat(savedUser.getId()).isGreaterThan(0);  // ID должен быть больше нуля

        // Проверяем, что сохранённые данные правильные
        assertThat(savedUser.getEmail()).isEqualTo("test@example.com");
        assertThat(savedUser.getLogin()).isEqualTo("testuser");
        assertThat(savedUser.getName()).isEqualTo("Test User");
        assertThat(savedUser.getBirthday()).isEqualTo(LocalDate.of(1990, 1, 1));
    }

    @Test
    void testUpdateUser() {
        // Сначала создаём пользователя
        User user = new User("test@example.com", "testuser", "Test User", LocalDate.of(1990, 1, 1));
        user = userStorage.save(user);  // Сохраняем пользователя в базе данных

        // Обновляем данные пользователя
        user.setEmail("updated@example.com");
        user.setLogin("updateduser");
        user.setName("Updated User");

        // Обновляем пользователя через метод update
        User updatedUser = userStorage.update(user);

        // Проверяем, что пользователь был обновлён
        assertThat(updatedUser.getEmail()).isEqualTo("updated@example.com");
        assertThat(updatedUser.getLogin()).isEqualTo("updateduser");
        assertThat(updatedUser.getName()).isEqualTo("Updated User");
    }

}