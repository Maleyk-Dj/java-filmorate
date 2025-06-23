package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;

@Component
public class InMemoryUserStorage implements UserStorage {

    private final Map<Long, User> users = new HashMap<>();

    @Override
    public User create(User user) {
        user.setId(generateId());
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public User update(User user) {
        if (!users.containsKey(user.getId())) {
            users.put(user.getId(), user);
        }
        return user;
    }

    @Override
    public Collection<User> getAllUsers() {
        return users.values();
    }

    @Override
    public Optional<User> findById(Long id) {
        return Optional.ofNullable(users.get(id));
    }

    @Override
    public void deleteById(Long id) {
        users.remove(id);
    }

    private long generateId() {
        long maxId = users.keySet().stream().mapToLong(id -> id).max().orElse(0);
        return ++maxId;
    }

    @Override
    public Optional<User> findByEmail(String email) {
        for (User user : users.values()) {
            if (user.getEmail().equals(email)) {
                return Optional.of(user);
            }
        }
        return Optional.empty();
    }
}
