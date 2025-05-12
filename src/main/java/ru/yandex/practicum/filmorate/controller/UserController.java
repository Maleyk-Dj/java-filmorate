package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {

    private final Map<Long, User> users = new HashMap<>();


    private long generateId() {
        long maxId = users.keySet().stream().mapToLong(id -> id).max().orElse(0);
        return ++maxId;
    }

    @PostMapping
    public User createUser(@Valid @RequestBody User user) {
        log.info("Создаем пользователя: {}", user);
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        user.setId(generateId());
        users.put(user.getId(), user);

        log.info("Пользователь успешно создан с ID={}", user.getId());

        return user;
    }

    @PutMapping
    public User updateUser(@Valid @RequestBody User user) {
        log.info("Запрос на обновления пользователя ID={}", user.getId(), user);

        if (!users.containsKey(user.getId())) {
            log.error("Пользователь с ID=" + user.getId() + " не найден.");
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Пользователь не найден");
        }

        users.put(user.getId(), user);

        log.info("Пользователь с ID={} успешно обновлен", user.getId());
        return user;
    }

    @GetMapping
    public Collection<User> getAllUsers() {
        log.info("Запрос на получение всех пользователей ({} шт.)", users.size());
        return users.values();
    }
}
