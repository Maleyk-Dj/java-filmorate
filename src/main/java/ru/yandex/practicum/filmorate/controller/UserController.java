package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;

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
        log.trace("Создаем пользователя: {}", user);
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
        log.info("Запрос на обновление пользователя ID={}", user.getId());

        if (user.getId() == 0) {
            throw new ValidationException("ID не указан");
        }

        if (!users.containsKey(user.getId())) {
            log.error("Пользователь с ID={} не найден", user.getId());
            throw new ValidationException("Пользователь не найден");
        }

        User oldUser = users.get(user.getId());
        oldUser.setName(user.getName());
        oldUser.setBirthday(user.getBirthday());
        oldUser.setEmail(user.getEmail());
        oldUser.setLogin(user.getLogin());
        log.info("Пользователь с ID={} успешно обновлён", user.getId());

        return oldUser;
    }

    @GetMapping
    public List<User> getAllUsers() {
        log.info("Запрос на получение всех пользователей ({} шт.)", users.size());
        return new ArrayList<>(users.values());
    }
}
