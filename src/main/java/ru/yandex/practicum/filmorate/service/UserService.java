package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import ru.yandex.practicum.filmorate.dto.NewUserRequest;
import ru.yandex.practicum.filmorate.dto.UpdateUserRequest;
import ru.yandex.practicum.filmorate.dto.UserDto;
import ru.yandex.practicum.filmorate.exception.ConditionsNotMetException;
import ru.yandex.practicum.filmorate.exception.DuplicatedDataException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.mapper.UserMapper;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;


import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    @Qualifier("userDbStorage")
    private final UserStorage userStorage;

    public UserDto createUser(NewUserRequest request) {
        log.debug("Создание нового пользователя с email={}", request.getEmail());

        if (request.getEmail() == null || request.getEmail().isEmpty()) {
            throw new ConditionsNotMetException("Email должен быть указан");
        }
        User user = UserMapper.mapToUser(request);
        user=userStorage.create(user);
        log.info("Пользователь с id={} создан успешно", user.getId());
        return UserMapper.mapToUserDto(user);

    }
    public UserDto updateUser(UpdateUserRequest request) {
        if (request.getId() == null) {
            throw new ValidationException("ID пользователя должен быть указан при обновлении");
        }

        User existing = userStorage.findById(request.getId())
                .orElseThrow(() -> new NotFoundException("Пользователь с id=" + request.getId() + " не найден"));

        // Обновляем только переданные поля
        if (request.hasEmail()) existing.setEmail(request.getEmail());
        if (request.hasLogin()) existing.setLogin(request.getLogin());
        if (request.hasUsername()) existing.setName(request.getName());
        if (request.hasBirthday()) existing.setBirthday(request.getBirthday());

        User updated = userStorage.update(existing);
        log.info("Пользователь с id={} обновлён", updated.getId());
        return UserMapper.mapToUserDto(updated);
    }

public Collection<UserDto> getAllUsers() {

    return userStorage.getAllUsers()
            .stream()
            .map(UserMapper::mapToUserDto)
            .collect(Collectors.toList());
}

public UserDto addFriend(Long userId, Long friendId) {
    if (userId.equals(friendId)) {
        throw new ValidationException("Нельзя добавить себя в друзья");
    }
    User user=getUserById(userId);
    User friend=getUserById(friendId);
    user.getFriends().add(friendId);
    userStorage.update(user);
    return UserMapper.mapToUserDto(user);
}

public UserDto removeFriend(Long userId, Long friendId) {
    User user = getUserById(userId);
    User friend = getUserById(friendId);
    user.getFriends().remove(friendId);
    userStorage.update(user);
    return UserMapper.mapToUserDto(user);
}

public Set<UserDto> getFriends(Long userId) {
    User user = getUserById(userId);

    return user.getFriends().stream()
            .map(friendId -> userStorage.findById(friendId)
                    .map(UserMapper::mapToUserDto)
                    .orElseThrow(() -> new NotFoundException("Друг не найден с ID: " + friendId)))
            .collect(Collectors.toSet());
}
public List<UserDto> getCommonFriends(Long userId, Long otherId) {
    User user = getUserById(userId);
    User other = getUserById(otherId);

    Set<Long> commonIds = new HashSet<>(user.getFriends());
    commonIds.retainAll(other.getFriends());

    return commonIds.stream()
            .map(friendId -> userStorage.findById(friendId)
                    .map(UserMapper::mapToUserDto)
                    .orElseThrow(() -> new NotFoundException("Друг не найден с ID: " + friendId)))
            .collect(Collectors.toList());
}


public UserDto findUserById(Long id) {

    return userStorage.findById(id)
            .map(UserMapper::mapToUserDto)
            .orElseThrow(()-> new NotFoundException("Пользователь не найден с ID: " + id));
}

public User getUserById(Long id) {
    return userStorage.findById(id)
            .orElseThrow(() -> new NotFoundException("Пользователь с id= " + id + " не найден"));
}
}
