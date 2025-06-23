package ru.yandex.practicum.filmorate.dao;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dal.mappers.UserRowMapper;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.util.Collection;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@Repository
@Primary
@Qualifier("userDbStorage")
@RequiredArgsConstructor
public class UserDbStorage implements UserStorage {
    private final JdbcTemplate jdbcTemplate;

    private static final String INSERT_USER_SQL = "INSERT INTO users (email, login, name, birthday) VALUES (?, ?, ?, ?)";
    private static final String FIND_USER_BY_ID_SQL = "SELECT * FROM users WHERE user_id=?";
    private static final String UPDATE_USER_SQL = "UPDATE users SET email = ?, login = ?, name = ?, birthday = ? WHERE user_id = ?";
    private static final String DELETE_USER_SQL = "DELETE FROM users WHERE user_id=?";

    @Override
    public User create(User user) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(con -> {
            PreparedStatement ps =con.prepareStatement(INSERT_USER_SQL,new String[]{"user_id"});
            ps.setString(1, user.getEmail());
            ps.setString(2, user.getLogin());
            ps.setString(3, user.getName());
            ps.setDate(4, Date.valueOf(user.getBirthday()));
            return ps;
        },keyHolder);
        Long id = Objects.requireNonNull(keyHolder.getKey().longValue());
        user.setId(id);
        return user;
    }

    @Override
    public User update(User user) {
        log.debug("Начало update для пользователя с id={}: email={}, login={}, name={}, birthday={}",
                user.getId(), user.getEmail(), user.getLogin(), user.getName(), user.getBirthday());

        int rowAffected = jdbcTemplate.update(UPDATE_USER_SQL,
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                Date.valueOf(user.getBirthday()),
                user.getId());

        log.debug("Количество затронутых строк после UPDATE: {}", rowAffected);

        if (rowAffected == 0) {
            log.warn("UPDATE не затронул ни одной строки — пользователь с id={} не найден", user.getId());
            throw new NotFoundException("Пользователь с id=" + user.getId() + " не найден");
        }

        User result = findById(user.getId())
                .orElseThrow(() -> {
                    log.error("Пользователь с id={} не найден после успешного UPDATE", user.getId());
                    return new NotFoundException("Пользователь с id=" + user.getId() + " не найден после обновления");
                });

        log.debug("Данные пользователя после UPDATE и повторного чтения: {}", result);
        return result;
    }


    @Override
    public Collection<User> getAllUsers() {
        String sql = "SELECT * FROM users";
        return jdbcTemplate.query(sql, new UserRowMapper());
    }

    @Override
    public Optional<User> findById(Long id) {
        try {
            User user=jdbcTemplate.queryForObject(FIND_USER_BY_ID_SQL,new UserRowMapper(),id);
            return Optional.ofNullable(user);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public void deleteById(Long id) {
        jdbcTemplate.update(DELETE_USER_SQL,id);

    }
    @Override
    public Optional<User> findByEmail(String email) {
        String sql = "SELECT * FROM users WHERE email = ?";
        try {
            User user = jdbcTemplate.queryForObject(sql, new UserRowMapper(), email);
            return Optional.ofNullable(user);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

}
