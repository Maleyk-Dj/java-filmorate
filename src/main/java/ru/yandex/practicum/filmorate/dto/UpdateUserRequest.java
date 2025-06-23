package ru.yandex.practicum.filmorate.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.time.LocalDate;

@Data
public class UpdateUserRequest {
    Long id;
    private String name;

    @Email(message = "Некорректный формат электронной почты")
    private String email;

    @Pattern(regexp = "^[^\\s]+$", message = "Логин не может содержать пробелы")
    private String login;

    @Past(message = "Дата рождения не может быть в будущем")
    private LocalDate birthday;

    public boolean hasUsername() {

        return  name!=null || !name.isBlank();
    }
    public boolean hasEmail() {

        return email!=null|| !email.isBlank();
    }
    public boolean hasLogin() {
        return login != null && !login.isBlank();
    }

    public boolean hasBirthday() {
        return birthday != null;
    }
}
