package org.example;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Scanner;

public class PlaceOrder {
    private final Connection connection;

    private static final String ANSI_RESET = "\u001B[0m";
    private static final String ANSI_GREEN = "\u001B[32m";
    private static final String ANSI_RED = "\u001B[31m";

    public PlaceOrder(Connection connection) {
        this.connection = connection;
    }

    public void placeOrder(String email, String bookTitle, int quantity) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Select a payment method:");
        System.out.println("1. Credit Card");
        System.out.println("2. Debit Card");
        System.out.println("3. PayPal");
        System.out.print("Enter your choice (1/2/3): ");

        int paymentMethodChoice = scanner.nextInt();
        String paymentMethod;

        switch (paymentMethodChoice) {
            case 1:
                paymentMethod = "Credit Card";
                break;
            case 2:
                paymentMethod = "Debit Card";
                break;
            case 3:
                paymentMethod = "PayPal";
                break;
            default:
                System.out.println(ANSI_RED + "Invalid choice. Defaulting to Credit Card." + ANSI_RESET);
                paymentMethod = "Credit Card"; // Default payment method
                break;
        }

        String query = "INSERT INTO orders5 (user_email, book_title, quantity, payment_method) VALUES (?, ?, ?, ?)";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, email);
            preparedStatement.setString(2, bookTitle); // Use book title instead of ID
            preparedStatement.setInt(3, quantity);
            preparedStatement.setString(4, paymentMethod); // Store the payment method


            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println(ANSI_GREEN + "Order placed successfully for " + quantity + " copies of \"" + bookTitle + "\" using " + paymentMethod + "." + ANSI_RESET);

                System.out.println("Redirecting to the main page...");
            } else {
                System.out.println(ANSI_RED + "Failed to place the order. Please try again." + ANSI_RESET);
            }
        } catch (SQLException e) {
            System.err.println(ANSI_RED + "Error placing order: " + e.getMessage() + ANSI_RESET);
        }
    }

    public void addPaymentMethodColumn() {
        String query = "ALTER TABLE orders5 ADD COLUMN payment_method VARCHAR(50)";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.executeUpdate();
            System.out.println("Column 'payment_method' added successfully.");
        } catch (SQLException e) {
            System.err.println("Error adding column: " + e.getMessage());
        }
    }
}
