package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.dto.NewUserRequest;
import ru.yandex.practicum.filmorate.dto.UpdateUserRequest;
import ru.yandex.practicum.filmorate.dto.UserDto;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import java.util.Collection;
import java.util.Set;

@Slf4j
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<UserDto> createUser(@Valid @RequestBody  NewUserRequest request) {
        return ResponseEntity.ok(userService.createUser(request));
    }

    @PutMapping
    public ResponseEntity<UserDto> updateUser(@Valid @RequestBody UpdateUserRequest request) {
        return ResponseEntity.ok(userService.updateUser(request));
    }
    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable Long id) {
        log.debug("Получение пользователя по id={}", id);
        return ResponseEntity.ok(userService.findUserById(id));
    }

    @GetMapping
    public ResponseEntity<Collection<UserDto>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @PutMapping("/{id}/friends/{friendId}")
    public ResponseEntity<UserDto> addFriend(@PathVariable Long id, @PathVariable Long friendId) {
        return ResponseEntity.ok(userService.addFriend(id, friendId));
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public ResponseEntity<UserDto> removeFriend(@PathVariable Long id, @PathVariable Long friendId) {
        log.debug("Вызов метода removeFriend c параметрами:id={},friendId={}", id, friendId);
        UserDto result = userService.removeFriend(id, friendId);
        log.debug("Пользователь с id={} удалил из друзей пользователя с id={} ", id, friendId);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{id}/friends")
    public ResponseEntity<Collection<UserDto>> getFriends(@PathVariable Long id) {
        log.debug("Вызов метода getFriends c параметрами:id={}", id);
        Set<UserDto> result = userService.getFriends(id);
        log.debug("Возвращен список друзей пользователя с id={} ", id);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public ResponseEntity<Collection<UserDto>> getCommonFriends(@PathVariable Long id, @PathVariable Long otherId) {
        log.debug("Вызов метода getCommonFriends c параметрами:id={},otherId={}", id, otherId);
        Collection<UserDto> result = userService.getCommonFriends(id, otherId);
        log.debug("Возвращен список общих друзей пользователей с id={},otherId={} ", id, otherId);
        return ResponseEntity.ok(result);
    }

}
