package org.example;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class RegisterUser {
    private Connection connection;

    private static final String ANSI_RED = "\u001B[31m";
    private static final String ANSI_GREEN = "\u001B[32m";
    private static final String ANSI_RESET = "\u001B[0m";

    public RegisterUser(Connection connection) {
        this.connection = connection;
    }

    public void registerUser(String email, String password) {
        if (!isValidEmail(email)) {
            printErrorMessage("Invalid email. Please enter a correct email.");
            return;
        }

        if (isDuplicateEmail(email)) {
            printErrorMessage("This email is already registered. Please use a different email.");
            return;
        }

        String query = "INSERT INTO users (email, password) VALUES (?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, email);
            stmt.setString(2, password);
            stmt.executeUpdate();
            printSuccessMessage("User registered successfully: " + email);
        } catch (SQLException e) {
            printErrorMessage("Error registering user: " + e.getMessage());
        }
    }

    private boolean isValidEmail(String email) {
        int atPosition = email.indexOf('@');
        int dotPosition = email.lastIndexOf('.');


        if (atPosition <= 0 || !email.endsWith(".com") || email.charAt(0) == '.' || email.charAt(0) == '-' || email.chars().filter(c -> c == '@').count() != 1) {
            return false;
        }

        // Ensure '.' is after '@' and not the last character
        return dotPosition > atPosition + 1 && dotPosition < email.length() - 1;
    }

    private boolean isDuplicateEmail(String email) {
        String query = "SELECT COUNT(*) FROM users WHERE email = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            printErrorMessage("Error checking for duplicate email: " + e.getMessage());
        }
        return false;
    }

    private void printSuccessMessage(String message) {
        System.out.println(ANSI_GREEN + message + ANSI_RESET);
    }

    private void printErrorMessage(String message) {
        System.out.println(ANSI_RED + message + ANSI_RESET);
    }
}
