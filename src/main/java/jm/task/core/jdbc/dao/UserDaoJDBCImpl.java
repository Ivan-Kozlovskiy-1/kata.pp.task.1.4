package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDaoJDBCImpl implements UserDao {


    public UserDaoJDBCImpl() {

    }

    public void createUsersTable() {
        try (Connection connection = Util.getConnection();
             Statement statement = connection.createStatement()) {

            statement.execute("CREATE TABLE IF NOT EXISTS task (id INT NOT NULL AUTO_INCREMENT " +
                    "PRIMARY KEY,name VARCHAR(99) NOT NULL,last_name VARCHAR(99) NOT NULL, age INT);");
            connection.commit();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }


    public void dropUsersTable() {
        String drop = "DROP TABLE IF EXISTS task";
        try (Connection connection = Util.getConnection();
             Statement statement = connection.createStatement()) {

            statement.executeUpdate(drop);
            connection.commit();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }


    public void saveUser(String name, String lastName, byte age) {
        try (Connection connection = Util.getConnection();
             PreparedStatement prepareStatement = connection.prepareStatement("INSERT INTO task (name, last_name, age) VALUES  (?, ?, ?);")) {

            prepareStatement.setString(1, name);
            prepareStatement.setString(2, lastName);
            prepareStatement.setByte(3, age);
            prepareStatement.executeUpdate();
            System.out.println("User named - " + name + " added in database");

            connection.commit();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }


    public void removeUserById(long id) {
        String sqlQuery = "DELETE FROM task WHERE id = ?";
        try (Connection connection = Util.getConnection();
             PreparedStatement pStat = connection.prepareStatement(sqlQuery)) {

            pStat.setString(1, String.valueOf(id));
            pStat.executeUpdate();
            connection.commit();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }


    public List<User> getAllUsers() {
        List<User> list = new ArrayList<>();
        try (Connection connection = Util.getConnection();
             Statement statement = connection.createStatement()) {

            ResultSet rs = statement.executeQuery("SELECT * FROM task;");
            while (rs.next()) {
                list.add(new User(rs.getString("name"), rs.getString("last_name"),
                        rs.getByte("age")));
            }
            connection.commit();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return list;
    }

    public void cleanUsersTable() {
        String sqlQuery = "DELETE FROM task";
        try (Connection connection = Util.getConnection();
             Statement stat = connection.createStatement()) {
            stat.executeUpdate(sqlQuery);
            connection.commit();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
}
