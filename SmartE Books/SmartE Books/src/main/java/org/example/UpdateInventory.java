package org.example;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class UpdateInventory implements Admin {
    private final Connection connection;

    // ANSI color codes for console output
    private static final String ANSI_RESET = "\u001B[0m";
    private static final String ANSI_GREEN = "\u001B[32m"; // Green
    private static final String ANSI_RED = "\u001B[31m";   // Red

    public UpdateInventory(Connection connection) {
        this.connection = connection;
    }
    @Override
    public void updateInventory(int bookId, int newCount) {
        String query = "UPDATE books SET inventory_count = ? WHERE id = ?"; // Ensure 'inventory_count' exists in your DB

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, newCount);
            stmt.setInt(2, bookId);
            int rowsUpdated = stmt.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println(ANSI_GREEN + "Inventory updated successfully for book ID: " + bookId + ANSI_RESET);
            } else {
                System.out.println(ANSI_RED + "No book found with the given ID: " + bookId + ANSI_RESET);
            }
        } catch (SQLException e) {
            System.err.println(ANSI_RED + "Error updating inventory: " + e.getMessage() + ANSI_RESET);
        }
    }

    public void updateInventoryByName(String bookTitle, int newCount) {
        String query = "UPDATE books SET inventory_count = ? WHERE title = ?"; // Change 'inventory_count' if necessary

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, newCount);
            stmt.setString(2, bookTitle);
            int rowsUpdated = stmt.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println(ANSI_GREEN + "Inventory updated successfully for book: " + bookTitle + ANSI_RESET);
            } else {
                System.out.println(ANSI_RED + "No book found with the given title: " + bookTitle + ANSI_RESET);
            }
        } catch (SQLException e) {
            System.err.println(ANSI_RED + "Error updating inventory by name: " + e.getMessage() + ANSI_RESET);
        }
    }

    @Override
    public void addBook(String title, String author, double price) {
        throw new UnsupportedOperationException("This method is not supported here.");
    }

    @Override
    public void displayOffers() {
        throw new UnsupportedOperationException("This method is not supported here.");
    }
}
