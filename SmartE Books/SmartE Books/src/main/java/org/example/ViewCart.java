package org.example;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.InputMismatchException;
import java.util.Scanner;

public class ViewCart {
    private final Connection connection;

    private static final String ANSI_RED = "\u001B[31m";
    private static final String ANSI_GREEN = "\u001B[32m";
    private static final String ANSI_RESET = "\u001B[0m";

    public ViewCart(Connection connection) {
        this.connection = connection;
    }
    public void viewCart(String email) {
        while (true) {
            displayCart(email);
            System.out.println("Select an action:");
            System.out.println("1. Order a product");
            System.out.println("2. Delete a product");
            System.out.println("3. Exit");

            Scanner scanner = new Scanner(System.in);
            int actionChoice = getValidActionChoice(scanner);

            switch (actionChoice) {
                case 1:
                    orderProduct(email);
                    break;
                case 2:
                    deleteProductFromCart(email);
                    break;
                case 3:
                    System.out.println(ANSI_GREEN + "Exiting the cart view." + ANSI_RESET);
                    return;
                default:
                    System.out.println(ANSI_RED + "Invalid choice. Please try again." + ANSI_RESET);
            }
        }
    }

    private void displayCart(String email) {
        String query = "SELECT b.title AS book_title, SUM(c.quantity) AS total_quantity " +
                "FROM cart1 c " +
                "JOIN books b ON c.book_id = b.id " +
                "WHERE c.user_email = ? GROUP BY b.title";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, email);
            ResultSet resultSet = preparedStatement.executeQuery();

            System.out.println(ANSI_GREEN + "\n======= Your Cart =======" + ANSI_RESET);
            boolean cartEmpty = true;

            while (resultSet.next()) {
                String bookTitle = resultSet.getString("book_title");
                int quantity = resultSet.getInt("total_quantity");

                System.out.println("Book Title: " + bookTitle + " | Quantity: " + quantity);
                cartEmpty = false;
            }

            if (cartEmpty) {
                System.out.println(ANSI_RED + "Your cart is empty." + ANSI_RESET);
            }
        } catch (SQLException e) {
            System.err.println(ANSI_RED + "Error fetching cart details: " + e.getMessage() + ANSI_RESET);
        }
    }
    private int getValidActionChoice(Scanner scanner) {
        int actionChoice = -1;
        while (actionChoice < 1 || actionChoice > 3) {
            System.out.print("Enter your choice (1-3): ");
            try {
                actionChoice = scanner.nextInt();
            } catch (InputMismatchException e) {
                System.out.println(ANSI_RED + "Invalid input. Please enter a number." + ANSI_RESET);
                scanner.nextLine();
            }
        }
        return actionChoice;
    }


    private void orderProduct(String email) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter the title of the book you want to order: ");
        String bookTitle = scanner.nextLine();

        if (!isBookInCart(email, bookTitle)) {
            System.out.println(ANSI_RED + "The book \"" + bookTitle + "\" is not available in your cart." + ANSI_RESET);
            return;
        }

        System.out.print("Enter the quantity you want to order: ");
        int quantity = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        String paymentMethod = getPaymentMethod();

        String orderQuery = "INSERT INTO orders5 (user_email, book_title, quantity, payment_method) " +
                "VALUES (?, ?, ?, ?)";

        try (PreparedStatement preparedStatement = connection.prepareStatement(orderQuery)) {
            preparedStatement.setString(1, email);
            preparedStatement.setString(2, bookTitle);
            preparedStatement.setInt(3, quantity);
            preparedStatement.setString(4, paymentMethod);
            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println(ANSI_GREEN + "Order placed successfully for \"" + bookTitle + "\"." + ANSI_RESET);
            } else {
                System.out.println(ANSI_RED + "Could not place the order. The book may not exist." + ANSI_RESET);
            }
        } catch (SQLException e) {
            System.err.println(ANSI_RED + "Error placing the order: " + e.getMessage() + ANSI_RESET);
        }
    }

    private String getPaymentMethod() {
        System.out.println("\nSelect a payment method:");
        System.out.println("1. Credit Card");
        System.out.println("2. Debit Card");
        System.out.println("3. PayPal");
        System.out.println("4. Exit");

        Scanner scanner = new Scanner(System.in);
        int paymentChoice = -1;

        while (paymentChoice < 1 || paymentChoice > 4) {
            System.out.print("Enter your choice (1-4): ");
            try {
                paymentChoice = scanner.nextInt();
                scanner.nextLine(); // Consume newline
            } catch (InputMismatchException e) {
                System.out.println(ANSI_RED + "Invalid input. Please enter a number." + ANSI_RESET);
                scanner.nextLine();
            }
        }

        switch (paymentChoice) {
            case 1:
                return "Credit Card";
            case 2:
                return "Debit Card";
            case 3:
                return "PayPal";
            case 4:
                System.out.println(ANSI_GREEN + "Exiting payment options." + ANSI_RESET);
                return null;
            default:
                return "Unknown";
        }
    }


    private boolean isBookInCart(String email, String bookTitle) {
        String query = "SELECT COUNT(*) AS count FROM cart1 c " +
                "JOIN books b ON c.book_id = b.id " +
                "WHERE c.user_email = ? AND b.title = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, email);
            preparedStatement.setString(2, bookTitle);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return resultSet.getInt("count") > 0;
            }
        } catch (SQLException e) {
            System.err.println(ANSI_RED + "Error checking if book is in cart: " + e.getMessage() + ANSI_RESET);
        }
        return false;
    }

    private void deleteProductFromCart(String email) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter the title of the book you want to delete: ");
        String bookTitle = scanner.nextLine();

        if (!isBookInCart(email, bookTitle)) {
            System.out.println(ANSI_RED + "The book \"" + bookTitle + "\" is not available in your cart." + ANSI_RESET);
            return;
        }

        String deleteQuery = "DELETE FROM cart1 WHERE user_email = ? AND book_id IN " +
                "(SELECT id FROM books WHERE title = ?)";

        try (PreparedStatement preparedStatement = connection.prepareStatement(deleteQuery)) {
            preparedStatement.setString(1, email);
            preparedStatement.setString(2, bookTitle);
            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println(ANSI_GREEN + "Successfully deleted \"" + bookTitle + "\" from your cart." + ANSI_RESET);
            } else {
                System.out.println(ANSI_RED + "Could not delete \"" + bookTitle + "\". It may not be in your cart." + ANSI_RESET);
            }
        } catch (SQLException e) {
            System.err.println(ANSI_RED + "Error deleting product: " + e.getMessage() + ANSI_RESET);
        }
    }
}
