package ru.yandex.practicum.filmorate.storage.user;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.Optional;

public interface UserStorage {
    User create(User user);

    User update(User user);

    Collection<User> getAllUsers();

    Optional<User> findById(Long id);

    void deleteById(Long id);

    Optional<User> findByEmail(String email);

}
