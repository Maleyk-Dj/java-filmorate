package ru.yandex.practicum.filmorate.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.yandex.practicum.filmorate.dto.NewUserRequest;
import ru.yandex.practicum.filmorate.dto.UpdateUserRequest;
import ru.yandex.practicum.filmorate.dto.UserDto;
import ru.yandex.practicum.filmorate.model.User;

@NoArgsConstructor (access = AccessLevel.PRIVATE)
public class UserMapper {
    public static UserDto mapToUserDto(User user) {
        UserDto dto = new UserDto();
        dto.setId(user.getId());
        dto.setName(user.getName());
        dto.setEmail(user.getEmail());
        dto.setLogin(user.getLogin());
        dto.setBirthday(user.getBirthday());
        return dto;
    }
    public static User mapToUser (NewUserRequest request){
        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setLogin(request.getLogin());
        user.setBirthday(request.getBirthday());
        return user;
    }

    public static User updateUserFields(User user, UpdateUserRequest request) {
        if (request.hasEmail()){
            user.setEmail(request.getEmail());
        }
        if (request.hasLogin()){
            user.setLogin(request.getLogin());
        }
        if (request.hasUsername()){
            user.setName(request.getName());
        }
        return user;

    }
}
