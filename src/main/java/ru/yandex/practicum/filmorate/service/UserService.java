package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FriendshipStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.stream.Collectors;
import java.util.Collection;
import java.util.Set;


@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    @Qualifier("userDbStorage")
    private final UserStorage userStorage;
    private final FriendshipStorage friendshipStorage;

    public User createUser(User user) {
        return userStorage.save(user);
    }

    public User updateUser(User user) {
        return userStorage.update(user);
    }

    public Collection<User> getAllUsers() {
        return userStorage.getAllUsers();
    }

    public User getUserById(Long id) {
        return userStorage.findById(id)
                .orElseThrow(() -> new NotFoundException("Пользователь с id=" + id + " не найден"));
    }

    public void addFriend(Long userId, Long friendId) {
        getUserById(userId);
        getUserById(friendId);
        friendshipStorage.addFriend(userId, friendId);
    }

    public void removeFriend(Long userId, Long friendId) {
        getUserById(userId);
        getUserById(friendId);
        friendshipStorage.removeFriend(userId, friendId);
    }

    public Set<User> getFriends(Long userId) {
        getUserById(userId);
        Set<Long> friendIds = friendshipStorage.getFriends(userId);
        return friendIds.stream()
                .map(this::getUserById)
                .collect(Collectors.toSet());
    }

    public Set<User> getCommonFriends(Long userId, Long otherId) {
        getUserById(userId);
        getUserById(otherId);
        Set<Long> commonIds = friendshipStorage.getCommonFriends(userId, otherId);
        return commonIds.stream()
                .map(this::getUserById)
                .collect(Collectors.toSet());
    }
}
