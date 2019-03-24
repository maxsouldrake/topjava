package ru.javawebinar.topjava.repository.jdbc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapperResultSetExtractor;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.UserRepository;

import javax.persistence.OrderBy;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

@Repository
@Transactional(readOnly = true)
public class JdbcUserRepositoryImpl implements UserRepository {

    private static final BeanPropertyRowMapper<User> ROW_MAPPER = BeanPropertyRowMapper.newInstance(User.class);

    private final JdbcTemplate jdbcTemplate;

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private final SimpleJdbcInsert insertUser;

    @Autowired
    public JdbcUserRepositoryImpl(JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.insertUser = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("users")
                .usingGeneratedKeyColumns("id");

        this.jdbcTemplate = jdbcTemplate;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @Override
    @Transactional
    public User save(User user) {
        BeanPropertySqlParameterSource parameterSource = new BeanPropertySqlParameterSource(user);

        if (user.isNew()) {
            Number newKey = insertUser.executeAndReturnKey(parameterSource);
            user.setId(newKey.intValue());
            Set<Role> roles = user.getRoles();
            if (!CollectionUtils.isEmpty(roles)) {
                jdbcTemplate.batchUpdate("INSERT INTO user_roles (user_id, role) VALUES (?, ?)", roles, roles.size(),
                        (ps, role) -> {
                            ps.setInt(1, user.getId());
                            ps.setString(2, role.name());
                        });
            }
        } else if (namedParameterJdbcTemplate.update(
                "UPDATE users SET name=:name, email=:email, password=:password, " +
                        "registered=:registered, enabled=:enabled, calories_per_day=:caloriesPerDay WHERE id=:id", parameterSource) == 0) {
            return null;
        }
        return user;
    }

    @Override
    @Transactional
    public boolean delete(int id) {
        return jdbcTemplate.update("DELETE FROM users WHERE id=?", id) != 0;
    }

    @Override
    public User get(int id) {
        List<User> users = jdbcTemplate.query("SELECT * FROM users u LEFT JOIN user_roles ur ON u.id=ur.user_id WHERE u.id=?",
        getResultSetExtractor(), id);
        return DataAccessUtils.singleResult(users);
    }

    @Override
    public User getByEmail(String email) {
//        return jdbcTemplate.queryForObject("SELECT * FROM users WHERE email=?", ROW_MAPPER, email);
        List<User> users = jdbcTemplate.query("SELECT * FROM users u LEFT JOIN user_roles ur ON u.id=ur.user_id WHERE u.email=?",
                getResultSetExtractor(), email);
        return DataAccessUtils.singleResult(users);
    }

    @Override
    public List<User> getAll() {
        return jdbcTemplate.query("SELECT * FROM users u LEFT JOIN user_roles ur ON u.id=ur.user_id",
                getResultSetExtractor());
    }

    private ResultSetExtractor<List<User>> getResultSetExtractor() {
        return rs -> {
            Map<Integer, User> userMap = new HashMap<>();
            Map<Integer, Set<Role>> roleMap = new HashMap<>();
            User user;
            Set<Role> roles;
            while (rs.next()) {
                int id = rs.getInt("id");
                user = userMap.get(id);
                roles = roleMap.get(id);
                Role role = Role.valueOf(rs.getString("role"));
                if (roles == null) {
                    roles = new HashSet<>();
                }
                if (user == null) {
                    user = new User();
                    user.setId(id);
                    user.setCaloriesPerDay(rs.getInt("calories_per_day"));
                    user.setEmail(rs.getString("email"));
                    user.setEnabled(rs.getBoolean("enabled"));
                    user.setPassword(rs.getString("password"));
                    user.setRegistered(rs.getDate("registered"));
                    user.setName(rs.getString("name"));
                    user.setRoles(Collections.singleton(Role.valueOf(rs.getString("role"))));
                    userMap.put(id, user);
                }
                roles.add(role);
                roleMap.put(id, roles);
            }
            userMap.keySet().forEach(key -> userMap.get(key).setRoles(roleMap.get(key)));
            return userMap.values().stream().sorted(Comparator.comparing(User::getName)
                    .thenComparing(Comparator.comparing(User::getEmail))).collect(Collectors.toList());
        };
    }
}
