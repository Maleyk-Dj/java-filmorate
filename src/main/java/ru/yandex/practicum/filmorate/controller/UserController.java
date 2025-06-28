package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.dto.NewUserRequest;
import ru.yandex.practicum.filmorate.dto.UpdateUserRequest;
import ru.yandex.practicum.filmorate.dto.UserDto;
import ru.yandex.practicum.filmorate.mapper.UserMapper;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;

    @PostMapping
    public ResponseEntity<UserDto> createUser(@RequestBody @Valid NewUserRequest request) {
        User user = userMapper.fromNewRequest(request);
        User created = userService.createUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(userMapper.toDto(created));
    }

    @PutMapping
    public ResponseEntity<UserDto> updateUser(@RequestBody @Valid UpdateUserRequest request) {
        User user = userMapper.fromUpdateRequest(request);
        User updated = userService.updateUser(user);
        return ResponseEntity.ok(userMapper.toDto(updated));
    }

    @GetMapping
    public ResponseEntity<Collection<UserDto>> getAllUsers() {
        Collection<UserDto> users = userService.getAllUsers().stream()
                .map(userMapper::toDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(userMapper.toDto(userService.getUserById(id)));
    }

    @PutMapping("/{id}/friends/{friendId}")
    public ResponseEntity<Void> addFriend(@PathVariable Long id, @PathVariable Long friendId) {
        userService.addFriend(id, friendId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public ResponseEntity<Void> removeFriend(@PathVariable Long id, @PathVariable Long friendId) {
        userService.removeFriend(id, friendId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}/friends")
    public ResponseEntity<Set<UserDto>> getFriends(@PathVariable Long id) {
        Set<UserDto> friends = userService.getFriends(id).stream()
                .map(userMapper::toDto)
                .collect(Collectors.toSet());
        return ResponseEntity.ok(friends);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public ResponseEntity<Set<UserDto>> getCommonFriends(@PathVariable Long id, @PathVariable Long otherId) {
        Set<UserDto> friends = userService.getCommonFriends(id, otherId).stream()
                .map(userMapper::toDto)
                .collect(Collectors.toSet());
        return ResponseEntity.ok(friends);
    }
}

