package ru.yandex.practicum.filmorate.mapper;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dto.NewUserRequest;
import ru.yandex.practicum.filmorate.dto.UpdateUserRequest;
import ru.yandex.practicum.filmorate.dto.UserDto;
import ru.yandex.practicum.filmorate.model.User;

@Component
public class UserMapper {

    public UserDto toDto(User user) {
        UserDto dto = new UserDto();
        dto.setId(user.getId());
        dto.setEmail(user.getEmail());
        dto.setLogin(user.getLogin());
        dto.setName(user.getName());
        dto.setBirthday(user.getBirthday());
        return dto;
    }

    public User fromNewRequest(NewUserRequest req) {
        User user = new User();
        user.setEmail(req.getEmail());
        user.setLogin(req.getLogin());
        user.setName(req.getName());
        user.setBirthday(req.getBirthday());
        return user;
    }

    public User fromUpdateRequest(UpdateUserRequest req) {
        User user = fromNewRequest(req);
        user.setId(req.getId());
        return user;
    }
}


