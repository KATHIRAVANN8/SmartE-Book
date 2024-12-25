package org.example;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SearchBooks {
    private Connection connection;


    public static final String ANSI_RESET = "\u001B[0m";      // Reset color
    public static final String ANSI_GREEN = "\u001B[32m";     // Green color
    public static final String ANSI_RED = "\u001B[31m";       // Red color

    public SearchBooks(Connection connection) {
        this.connection = connection;
    }

    public List<String> searchBooks(String searchInput) {
        List<String> books = new ArrayList<>();
        String query = "SELECT title FROM books WHERE title LIKE ? OR author LIKE ?";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            String searchPattern = "%" + searchInput + "%";
            stmt.setString(1, searchPattern);
            stmt.setString(2, searchPattern);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                books.add(rs.getString("title"));
            }
        } catch (SQLException e) {
            System.err.println("Error searching books: " + e.getMessage());
        }
        return books;
    }

    public void displayBooks(String searchInput) {
        List<String> books = searchBooks(searchInput);

        if (books.isEmpty()) {

            System.out.println(ANSI_RED + "No books available for the author or title: " + searchInput + ANSI_RESET);
        } else {

            System.out.println(ANSI_GREEN + "Books found:" + ANSI_RESET);
            for (String book : books) {
                System.out.println(ANSI_GREEN + "- " + book + ANSI_RESET);
            }
        }
    }
}
