package com.kolesnikov.gamble.repository;

import com.kolesnikov.gamble.exception.repository.GambleDbException;
import com.kolesnikov.gamble.model.UserEntity;

import javax.sql.DataSource;
import java.sql.*;
import java.util.Optional;

public class UserDbManager {
    private final DataSource dataSource;

    private final String PUT_QUERY = "INSERT INTO users(name, balance) VALUES(?,?) ";
    private final String GET_QUERY = "SELECT * from users where id = ?";

    public UserDbManager(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public synchronized Optional<UserEntity> put(UserEntity userEntity) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(PUT_QUERY, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, userEntity.getName());
            statement.setInt(2, userEntity.getBalance());
            statement.executeUpdate();
            final ResultSet generatedKeys = statement.getGeneratedKeys();
            if (!generatedKeys.next()) {
                return Optional.empty();
            }
            return Optional.of(new UserEntity(
                    generatedKeys.getLong(1),
                    userEntity.getName(),
                    userEntity.getBalance()
            ));
        } catch (SQLException e) {
            throw new GambleDbException(e.getMessage(), e.getCause());
        }
    }

    public synchronized Optional<UserEntity> get(long id) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(GET_QUERY)) {
            statement.setLong(1, id);
            final ResultSet resultSet = statement.executeQuery();
            if (!resultSet.next()) {
                return Optional.empty();
            }
            return Optional.of(new UserEntity(
                    resultSet.getLong(1),
                    resultSet.getString(2),
                    resultSet.getInt(3)));
        } catch (SQLException e) {
            throw new GambleDbException(e.getMessage(), e.getCause());
        }
    }
}