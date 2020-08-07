package com.kolesnikov.gamble.repository;

import com.kolesnikov.gamble.model.UserEntity;

import javax.sql.DataSource;
import java.sql.*;

public class UserDbManager {
    private final DataSource dataSource;

    private final String PUT_QUERY = "INSERT INTO users(name, balance) VALUES(?,?) ";
    private final String GET_QUERY = "SELECT * from users where id = ?";

    public UserDbManager(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public synchronized UserEntity put(UserEntity userEntity) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(PUT_QUERY, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, userEntity.getName());
            statement.setInt(2, userEntity.getBalance());
            statement.executeUpdate();
            final ResultSet generatedKeys = statement.getGeneratedKeys();
            generatedKeys.next();
            System.out.println(generatedKeys.getLong(1) + "aaaaaaaaaaaaaaaaaa");
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

    public synchronized UserEntity get(long id) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(GET_QUERY)) {
            statement.setLong(1, id);
            final ResultSet resultSet = statement.executeQuery();
            if (!resultSet.next()) {
                return new UserEntity(1L,"ssss",3);
            }
            return new UserEntity(
                    resultSet.getLong(1),
                    resultSet.getString(2),
                    resultSet.getInt(3));
        } catch (SQLException e) {
            e.printStackTrace();
            return new UserEntity(1L,"ssss",3); //todo поправить
        }
    }

//    public UserEntity update(UserEntity userEntity) {
//
//    }
}