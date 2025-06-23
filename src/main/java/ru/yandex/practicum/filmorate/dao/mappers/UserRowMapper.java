package ru.yandex.practicum.filmorate.dal.mappers;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;


@Component
public class UserRowMapper implements RowMapper <User> {

    @Override
    public User mapRow(ResultSet rs, int rowNum) throws SQLException {
     User user = new User();
     user.setId(rs.getLong("user_id"));
     user.setName(rs.getString("name"));
     user.setEmail(rs.getString("email"));
     user.setLogin(rs.getString("login"));

     Date birthday = rs.getDate("birthday");
     if (birthday!=null) {
         user.setBirthday(birthday.toLocalDate());
     }
     return user;
    }
}
