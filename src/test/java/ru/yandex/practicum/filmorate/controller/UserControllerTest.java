package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.User;

import java.time.Instant;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

class UserControllerTest {

    private UserController controller;

    @BeforeEach
    void setUp() {
        controller = new UserController();
    }

    @Test
    void createUser() {
        User user1 = new User();
        user1.setName("Name");
        user1.setEmail("@email");
        user1.setBirthday(Instant.now().minusSeconds(20000));
        user1.setLogin("malika");

        User result = controller.createUser(user1);
        assertNotNull(user1.getId());
        assertEquals("malika", result.getLogin());
    }


    @Test
    void shouldCreateUser_EmptyName() {
        User user4 = new User();
        user4.setEmail("@email2");
        user4.setLogin("mylogin");
        user4.setName("");
        user4.setBirthday(Instant.now().minusSeconds(8000));

        User result = controller.createUser(user4);
        assertEquals("mylogin", result.getLogin());
    }

    @Test
    void sholdCreateUser_futureBirthday() {
        User user5 = new User();
        user5.setEmail("@email2");
        user5.setLogin("mylogin");
        user5.setName("Name");
        user5.setBirthday(Instant.now().plusSeconds(20000));

        User result = controller.createUser(user5);
        assertTrue(result.getLogin().contains("mylogin"));
    }

    @Test
    void shouldGetAllUsers() {
        User user2 = new User();
        user2.setEmail("@com");
        user2.setLogin("loginNew");
        user2.setName("Name2");
        user2.setBirthday(Instant.now().minusSeconds(25000));

        User user3 = new User();
        user3.setEmail("@email.com");
        user3.setLogin("myNot");
        user3.setName("Name3");
        user3.setBirthday(Instant.now().minusSeconds(17000));

        controller.createUser(user2);
        controller.createUser(user3);

        Collection<User> users = controller.getAllUsers();

        assertEquals(2, users.size());
    }

    @Test
    void shouldUpdateUser() {
        User user6 = new User();
        user6.setEmail("@jfjhfj");
        user6.setLogin("lobbbbb");
        user6.setName("Name6");
        user6.setBirthday(Instant.now().minusSeconds(40000));

        User result = controller.createUser(user6);
        long id = result.getId();

        User updated = new User();
        updated.setId(id);
        updated.setEmail("@esssss");
        updated.setLogin("loglog");
        updated.setName("Updated");
        updated.setBirthday(Instant.now().minusSeconds(37485));

        User newResult = controller.updateUser(updated);

        assertEquals("@esssss", newResult.getEmail());
        assertEquals(id, newResult.getId());
    }

    @Test
    void shouldThrowIfUserNotFoundOnUpdate() {
        User user = new User();
        user.setId(999L); // несущий ID
        user.setEmail("test@example.com");
        user.setLogin("login");
        user.setName("Name");
        user.setBirthday(Instant.parse("2000-01-01T00:00:00Z"));

        RuntimeException ex = assertThrows(RuntimeException.class, () -> controller.updateUser(user));
        assertTrue(ex.getMessage().contains("Пользователь не найден"));
    }
}








