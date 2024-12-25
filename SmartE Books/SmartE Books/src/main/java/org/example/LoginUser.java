package org.example;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginUser {
    private Connection connection;

    public LoginUser(Connection connection) {
        this.connection = connection;
    }

    public boolean login(String email, String password) {
        String query = "SELECT password FROM users WHERE email = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, email);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                String storedPassword = resultSet.getString("password");
                if (storedPassword.equals(password)) {
                    return true;
                }
            }
        } catch (SQLException e) {
            System.err.println("An error occurred during login: " + e.getMessage());
        }

        return false;
    }
}
