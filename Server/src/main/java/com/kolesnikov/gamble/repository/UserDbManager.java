package com.kolesnikov.gamble.repository;

import com.kolesnikov.gamble.model.UserEntity;

import javax.sql.DataSource;
import java.sql.*;

public class UserDbManager {
    private final DataSource dataSource;

    private final String CREATE_QUERY = "CREATE TABLE user(" +
            "id BIGINT auto_increment, " +
            "name varchar NOT NULL, " +
            "balance INT NOT NULL)";

    private final String PUT_QUERY = "INSERT INTO user(name, balance) VALUES(?,?) ";
    private final String GET_QUERY = "SELECT * from user where id = ?";

    public UserDbManager(DataSource dataSource) {
        this.dataSource = dataSource;
        try (Connection connection = dataSource.getConnection(); // todo может можно получше сделать(сделать специальный класс для этого)
             Statement statement = connection.createStatement()) {
            statement.execute(CREATE_QUERY);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public UserEntity put(UserEntity userEntity) { // todo поменять параметр
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(PUT_QUERY, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, userEntity.getName());
            statement.setInt(2, userEntity.getBalance());
            statement.executeUpdate();
            final ResultSet generatedKeys = statement.getGeneratedKeys();
            generatedKeys.next();

            return new UserEntity(
                    generatedKeys.getLong(1),
                    userEntity.getName(),
                    userEntity.getBalance()
            );
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return null; // todo нормально обработать
        }
    }

    public UserEntity get(long id) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(GET_QUERY)) {
            statement.setLong(1, id);
            final ResultSet resultSet = statement.executeQuery();
            if (!resultSet.next()) {
                return null;
            }
            return new UserEntity(
                    resultSet.getLong(1),
                    resultSet.getString(2),
                    resultSet.getInt(3));
        } catch (SQLException e) {
            e.printStackTrace();
            return null; //todo поправить
        }
    }

//    public UserEntity update(UserEntity userEntity) {
//
//    }
}