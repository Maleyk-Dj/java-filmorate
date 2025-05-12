package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

class UserControllerTest {

    private UserController controller;

    @BeforeEach
    void setUp() {
        controller = new UserController();
    }

    @Test
    void createUser_validUser() {
        User user1 = new User();
        user1.setName("Name");
        user1.setEmail("@email");
        user1.setBirthday(Instant.now().minusSeconds(20000));
        user1.setLogin("malika");

        User result = controller.createUser(user1);
        assertNotNull(user1.getId());
        assertEquals("malika", user1.getLogin());
    }

    @Test
    void createUser_invalidEmail_throws() {
        User user2 = new User();
        user2.setEmail("go-to-home");
        user2.setLogin("login");
        user2.setName("Name");
        user2.setBirthday(Instant.now().minusSeconds(10000));

        ValidationException ex = assertThrows(ValidationException.class, () -> controller.createUser(user2));
        assertTrue(ex.getMessage().contains("Некорректная электронная почта: " + user2.getEmail()));
    }

    @Test
    void createUser_loginWithSpaces_throws() {
        User user3 = new User();
        user3.setEmail("go@home.com");
        user3.setLogin("bad login");
        user3.setName("Name");
        user3.setBirthday(Instant.now().minusSeconds(10000));

        ValidationException ex = assertThrows(ValidationException.class, () -> controller.createUser(user3));
        assertTrue(ex.getMessage().contains("Логин не может быть пустым или содержать пробелы: " + user3.getLogin()));
    }

    @Test
    void createUser_EmptyName() {
        User user4 = new User();
        user4.setEmail("@email2");
        user4.setLogin("mylogin");
        user4.setName(" ");
        user4.setBirthday(Instant.now().minusSeconds(8000));

        User result = controller.createUser(user4);
        assertEquals("mylogin", user4.getLogin());
    }

    @Test
    void createUser_futureBirthday() {
        User user5 = new User();
        user5.setEmail("@email2");
        user5.setLogin("mylogin");
        user5.setName("Name");
        user5.setBirthday(Instant.now().plusSeconds(10));

        ValidationException ex = assertThrows(ValidationException.class, () -> controller.createUser(user5));
        assertTrue(ex.getMessage().contains("Дата рождения не может быть в будущем: " + user5.getBirthday()));
    }
}



