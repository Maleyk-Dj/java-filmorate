package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;

@Slf4j
@Component
public class InMemoryUserStorage implements UserStorage {

    private final Map<Long, User> users = new HashMap<>();

    @Override
    public User createUser(User user) {
        log.debug("Попытка создать пользователя с именем {}", user.getName());

        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
            log.debug("Имя пользователя не задано, установлено имя логина {}", user.getLogin());
        }
        user.setId(generateId());

        users.put(user.getId(), user);
        log.debug("Пользователь с id={} успешно создан", user.getId());

        return user;
    }

    @Override
    public User updateUser(User user) {
        log.debug("Попытка обновить данные пользователя с id {}", user.getId());
        if (!users.containsKey(user.getId())) {
            log.warn("Пользователь с id={} не найден", user.getId());
            throw new NotFoundException("Пользователь с id = " + user.getId() + " не найден");
        }
        User oldUser = users.get(user.getId());
        oldUser.setName(user.getName());
        oldUser.setBirthday(user.getBirthday());
        oldUser.setEmail(user.getEmail());
        oldUser.setLogin(user.getLogin());
        log.debug("Пользователь с id={} успешно обновлён", user.getId());

        return oldUser;
    }

    @Override
    public Collection<User> getAllUsers() {
        log.debug("Попытка вызвать список всех пользователей");
        return users.values();
    }

    @Override
    public Optional<User> findById(Long id) {
        log.debug("Попытка найти пользователя с id={}", id);
        return Optional.ofNullable(users.get(id));
    }

    @Override
    public void deleteById(Long id) {
        log.debug("Попытка удалить пользователя с id={}", id);
        users.remove(id);
    }

    private long generateId() {
        log.debug("Попытка сгенерировать ID");
        long maxId = users.keySet().stream().mapToLong(id -> id).max().orElse(0);
        log.debug("Сгенерировано ID  пользователя {}", maxId);
        return ++maxId;
    }
}
