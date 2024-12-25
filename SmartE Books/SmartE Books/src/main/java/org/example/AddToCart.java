package org.example;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

class AddToCart {
    private Connection connection;

    // ANSI escape codes for colors
    private static final String ANSI_RESET = "\u001B[0m";
    private static final String ANSI_GREEN = "\u001B[32m";  // Green text
    private static final String ANSI_RED = "\u001B[31m";    // Red text

    public AddToCart(Connection connection) {
        this.connection = connection;
    }

    // Add to cart using book name
    public void addToCart(String email, String bookName) {
        // First, check if the user exists
        if (!isUserExists(email)) {
            System.out.println(ANSI_RED + "Error: The user with email " + email + " does not exist." + ANSI_RESET);
            return;
        }

        // Fetch the book ID using book title
        int bookId = getBookId(bookName);
        if (bookId == -1) {
            System.out.println(ANSI_RED + "Error: The book with name '" + bookName + "' does not exist." + ANSI_RESET);
            return;
        }

        // Proceed to add the book to the cart using book_id
        String query = "INSERT INTO cart1 (user_email, book_id, quantity) VALUES (?, ?, ?)"; // Changed from cart to cart1
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, email);
            stmt.setInt(2, bookId);  // Use book ID instead of book name
            stmt.setInt(3, 1);  // Default quantity is 1
            stmt.executeUpdate();
            System.out.println(ANSI_GREEN + "Book '" + bookName + "' added to cart for User Email: " + email + ANSI_RESET);
        } catch (SQLException e) {
            System.err.println(ANSI_RED + "Error adding book to cart: " + e.getMessage() + ANSI_RESET);
        }
    }

    // Check if user exists by email
    private boolean isUserExists(String email) {
        String query = "SELECT COUNT(*) FROM users WHERE email = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, email);
            ResultSet resultSet = stmt.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt(1) > 0; // Return true if the count is greater than 0
            }
        } catch (SQLException e) {
            System.err.println(ANSI_RED + "Error checking if user exists: " + e.getMessage() + ANSI_RESET);
        }
        return false; // Return false if there was an error or the user does not exist
    }

    // Fetch book ID by book name
    private int getBookId(String bookName) {
        String query = "SELECT id FROM books WHERE title = ?";  // Use 'title' instead of 'name'
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, bookName);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("id");  // Return the book ID
            }
        } catch (SQLException e) {
            System.err.println(ANSI_RED + "Error retrieving book ID: " + e.getMessage() + ANSI_RESET);
        }
        return -1;  // Return -1 if the book is not found
    }
}
