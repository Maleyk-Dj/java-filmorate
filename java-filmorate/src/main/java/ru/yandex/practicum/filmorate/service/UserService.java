package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserStorage userStorage;

    public User addFriend(Long userId, Long friendId) {
        log.debug("Попытка пользователя id={} добавить в друзья пользователя с id={}", userId, friendId);

        User user = getUserOrThrow(userId);
        log.debug("Найден пользователь с id={} метод addFriend", userId);

        User friend = getUserOrThrow(friendId);
        log.debug("Найден пользователь для добавления в друзья с id={}", friendId);

        user.getFriends().add(friendId);
        friend.getFriends().add(userId);

        log.debug("Пользователи {} и {} теперь друзья", userId, friendId);
        return user;
    }

    public User removeFriend(Long userId, Long friendId) {
        log.debug("Попытка пользователя id={} удалить из друзей пользователя с id={}", userId, friendId);

        User user = getUserOrThrow(userId);
        log.debug("Найден пользователь с id={} метод removeFriend", userId);

        User friend = getUserOrThrow(friendId);
        log.debug("Найден пользователь для удаления из друзей с id={}", friendId);


        user.getFriends().remove(friendId);
        friend.getFriends().remove(userId);

        log.info("Пользователи {} и {} больше не друзья", userId, friendId);
        return user;
    }

    public List<User> getFriends(Long userId) {
        log.debug("Получение списка друзей пользователя с id={}", userId);

        User user = getUserOrThrow(userId);
        log.debug("Найден пользователь с id={} метод getFriends", userId);

        List<User> friends = user.getFriends().stream()
                .map(userStorage::findById)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());

        log.debug("Найдено {} друзей для пользователя с id={}", friends.size(), userId);
        return friends;
    }

    public List<User> getCommonFriends(Long userId, Long otherId) {
        log.debug("Получение списка общих друзей пользователей с id={} и id={}", userId, otherId);

        User user = getUserOrThrow(userId);
        User other = getUserOrThrow(otherId);

        log.debug("Пользователи найдены: {} и {}", userId, otherId);

        Set<Long> commonIds = new HashSet<>(user.getFriends());
        commonIds.retainAll(other.getFriends());

        log.debug("Количество общих друзей: {}", commonIds.size());

        List<User> commonFriends = commonIds.stream()
                .map(userStorage::findById)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
        log.debug("Общих друзей получено", commonFriends.size());
        return commonFriends;
    }

    public Optional<User> findUserById(Long id) {
        return userStorage.findById(id);
    }

    private User getUserOrThrow(Long id) {
        log.debug("Попытка найти пользователя с id={}", id);
        User user = userStorage.findById(id).orElseThrow(() -> {
            log.warn("Пользователь с id={} не найден", id);
            return new NotFoundException("Пользователь с id= " + id + " не найден");
        });
        log.debug("Пользователь с id={} не найден", id);
        return user;
    }

    public User getUserById(Long id) {
        return getUserOrThrow(id);
    }
}
