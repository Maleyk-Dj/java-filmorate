package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;


@Data
@EqualsAndHashCode(of = {"email"})
public class User {

    private Long id;

    @NotBlank(message = "Электронная почта не может быть пустой")
    @Email(message = "Некорректный формат электронной почты")
    String email;

    @NotBlank(message = "Логин не может быть пустым")
    @Pattern(regexp = "\\S+", message = "Логин не может содержать пробелы")
    String login;

    String name;

    @NotNull(message = "Дата рождения должна быть указана")
    @Past(message = "Дата рождения не может быть в будущем")
    LocalDate birthday;
}

