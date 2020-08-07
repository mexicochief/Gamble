package com.kolesnikov.gamble.repository;

import com.kolesnikov.gamble.model.BetEntity;

import javax.sql.DataSource;
import java.sql.*;

public class BetHistoryDbManager {
    private final DataSource dataSource;

    private final String PUT_QUERY = "INSERT INTO bets(bet, balance_change, user_id) VALUES(?,?,?)";
    private final String UPDATE_BALANCE_QUERY = "UPDATE users SET balance = balance + ? " +
            "WHERE id = ?";
    private final String GET_QUERY = "SELECT * from bets where user_id = ?";

    public BetHistoryDbManager(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public synchronized BetEntity put(BetEntity betEntity) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement put = connection.prepareStatement(PUT_QUERY, Statement.RETURN_GENERATED_KEYS);
             PreparedStatement update = connection.prepareStatement(UPDATE_BALANCE_QUERY)) {
            connection.setAutoCommit(false);

            put.setLong(1, betEntity.getBet());
            put.setLong(2, betEntity.getChangeOfBalance());
            put.setLong(3, betEntity.getUserId());
            put.executeUpdate();

            update.setLong(1, betEntity.getChangeOfBalance());
            update.setLong(2, betEntity.getUserId());
            update.executeUpdate();
            connection.commit();
            connection.setAutoCommit(true);

            final ResultSet generatedKeys = put.getGeneratedKeys();
            if (!generatedKeys.next()) {
                return null;
            }

            return new BetEntity(
                    generatedKeys.getLong(1),
                    betEntity.getBet(),
                    betEntity.getChangeOfBalance(),
                    betEntity.getUserId()
            );
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return null; // todo нормально обработать
        }
    }

    public synchronized BetEntity get(long id) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(GET_QUERY)) {

            statement.setLong(1, id);
            final ResultSet resultSet = statement.executeQuery();
            if (!resultSet.next()) {
                return null;
            }
            return new BetEntity(
                    resultSet.getLong(1),
                    resultSet.getLong(2),
                    resultSet.getLong(3),
                    resultSet.getLong(4));
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return null; //todo поправить
        }
    }
}
