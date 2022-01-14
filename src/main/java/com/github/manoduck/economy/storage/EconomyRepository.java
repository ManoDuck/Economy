package com.github.manoduck.economy.storage;

import com.github.manoduck.economy.objects.BalanceTop;
import com.github.manoduck.economy.objects.User;
import com.google.common.collect.Lists;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class EconomyRepository {

    private Connection connection;

    public EconomyRepository(HikariDataSource dataSource) {

        try {

            this.connection = dataSource.getConnection();

        }

        catch (SQLException ex) { ex.printStackTrace(); }

    }

    public Boolean userExists(String name) {

        try {

            String QUERY = "SELECT `id` FROM `economy_users` WHERE `name`=?";
            PreparedStatement statement = connection.prepareStatement(QUERY);

            statement.setString(1, name);

            ResultSet resultSet = statement.executeQuery();

            return resultSet.next();

        }

        catch (SQLException exception) { exception.printStackTrace(); }

        return false;

    }

    public User getUser(String name) {

        try {

            String QUERY = "SELECT `balance` FROM `economy_users` WHERE `name`=?";
            PreparedStatement statement = connection.prepareStatement(QUERY);

            statement.setString(1, name);

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {

                return User
                        .builder()
                        .name(name)
                        .balance(resultSet.getDouble("balance"))
                        .build();

            }
        }

        catch (SQLException exception) { exception.printStackTrace(); }

        return User
                .builder()
                .name(name)
                .balance(0.0)
                .build();

    }

    public Double getBalance(String name) {

        try {

            String QUERY = "SELECT `balance` FROM `economy_users` WHERE `name`=?";
            PreparedStatement statement = connection.prepareStatement(QUERY);

            statement.setString(1, name);

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) return resultSet.getDouble("balance");

        }

        catch (SQLException exception) { exception.printStackTrace(); }

        return null;

    }

    public void createUser(User user) {

        try {

            String QUERY = "INSERT INTO `economy_users` (`name`, `balance`) VALUES (?,?)";
            PreparedStatement statement = connection.prepareStatement(QUERY);

            statement.setString(1, user.getName());
            statement.setDouble(2, user.getBalance());
            statement.executeUpdate();

        }

        catch (SQLException exception) { exception.printStackTrace(); }

    }

    public void updateUser(User user) {

        try {

            String QUERY = "UPDATE `economy_users` set `balance`=? WHERE `name`=?";
            PreparedStatement statement = connection.prepareStatement(QUERY);

            statement.setDouble(1, user.getBalance());
            statement.setString(2, user.getName());
            statement.executeUpdate();

        }

        catch (SQLException exception) { exception.printStackTrace(); }

    }

    public void addBalance(String name, Double balance) {

        try {

            String QUERY = "UPDATE `economy_users` set `balance`=? WHERE `name`=?";
            PreparedStatement statement = connection.prepareStatement(QUERY);

            statement.setDouble(1, getBalance(name) + balance);
            statement.setString(2, name);
            statement.executeUpdate();

        }

        catch (SQLException exception) { exception.printStackTrace(); }

    }

    public void setBalance(String name, Double balance) {

        try {

            String QUERY = "UPDATE `economy_users` set `balance`=? WHERE `name`=?";
            PreparedStatement statement = connection.prepareStatement(QUERY);

            statement.setDouble(1, balance);
            statement.setString(2, name);
            statement.executeUpdate();

        }

        catch (SQLException exception) { exception.printStackTrace(); }

    }

    public List<BalanceTop> getBalanceTop() {

        try {

            String QUERY = "SELECT `name`, `balance` FROM `economy_users` ORDER BY `balance` DESC LIMIT 10";
            PreparedStatement statement = connection.prepareStatement(QUERY);
            ResultSet resultSet = statement.executeQuery();
            List<BalanceTop> balanceTopList = Lists.newArrayList();

            while (resultSet.next()) {

                balanceTopList.add(BalanceTop
                        .builder()
                        .name(resultSet.getString("name"))
                        .balance(resultSet.getDouble("balance"))
                        .build());

            }

            resultSet.close();
            return balanceTopList;

        }

        catch (SQLException exception) { exception.printStackTrace(); }

        return null;

    }
}