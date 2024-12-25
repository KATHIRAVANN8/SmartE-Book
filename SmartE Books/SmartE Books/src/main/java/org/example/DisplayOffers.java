package org.example;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DisplayOffers implements Admin {
    private final Connection connection;

    public DisplayOffers(Connection connection) {
        this.connection = connection;
    }

    // Method to display current offers
    @Override
    public void displayOffers() {
        String query = "SELECT b.title, o.offer_description, o.discount_percentage " +
                "FROM books b " +
                "JOIN offers o ON b.id = o.book_id";

        try (PreparedStatement stmt = connection.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            System.out.println("Current Offers:");
            boolean hasOffers = false; // Flag to check if there are any offers
            while (rs.next()) {
                hasOffers = true;
                String title = rs.getString("title");
                String offerDescription = rs.getString("offer_description");
                double discount = rs.getDouble("discount_percentage");

                System.out.printf("Book: %s, Offer: %s, Discount: %.2f%%\n", title, offerDescription, discount);
            }
            if (!hasOffers) {
                System.out.println("No current offers available.");
            }
        } catch (SQLException e) {
            System.err.println("Error displaying offers: " + e.getMessage());
        }
    }

    @Override
    public void addBook(String title, String author, double price) {
        throw new UnsupportedOperationException("This method is not supported here.");
    }

    @Override
    public void updateInventory(int bookId, int newCount) {
        throw new UnsupportedOperationException("This method is not supported here.");
    }
}
