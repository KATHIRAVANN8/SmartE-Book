package org.example;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class AddBook implements Admin {
    private final Connection connection;

    public AddBook(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void addBook(String title, String author, double price) {
        String query = "INSERT INTO books (title, author, price) VALUES (?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, title);
            stmt.setString(2, author);
            stmt.setDouble(3, price);
            stmt.executeUpdate();
            System.out.println("Book added: " + title);
        } catch (SQLException e) {
            System.err.println("Error adding book: " + e.getMessage());
        }
    }

    @Override
    public void displayOffers() {
        throw new UnsupportedOperationException("This method is not supported here.");
    }

    @Override
    public void updateInventory(int bookId, int newCount) {
        throw new UnsupportedOperationException("This method is not supported here.");
    }
}
