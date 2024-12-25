package org.example;

import java.sql.Connection;
import java.util.InputMismatchException;
import java.util.Scanner;

public class AdminDashboard {
    private final Connection connection;
    private final AddBook addBook;
    private final UpdateInventory updateInventory;
    private final String adminPassword = "Admin@@8";

    public AdminDashboard(Connection connection) {
        this.connection = connection;
        this.addBook = new AddBook(connection);
        this.updateInventory = new UpdateInventory(connection);
    }
    public void displayAdminMenu(Scanner scanner) {
        if (!verifyAdminPassword(scanner)) {
            System.out.println("Access denied. Incorrect password.");
            return;
        }

        while (true) {
            System.out.println("\n======= Admin Dashboard =======");
            System.out.println("1. Add New Book");
            System.out.println("2. Display Offers");
            System.out.println("3. Update Book Inventory");
            System.out.println("4. Back to Main Menu");
            System.out.print("Choose an option: ");

            int adminChoice = -1;

            while (adminChoice < 1 || adminChoice > 4) {
                try {
                    adminChoice = scanner.nextInt();
                    scanner.nextLine();
                    if (adminChoice < 1 || adminChoice > 4) {
                        System.out.println("Invalid choice. Please choose between 1 and 4.");
                    }
                } catch (InputMismatchException e) {
                    System.out.println("Invalid input. Please enter a number between 1 and 4.");
                    scanner.next();
                }
            }

            switch (adminChoice) {
                case 1:
                    System.out.print("Enter book title: ");
                    String title = scanner.nextLine();
                    System.out.print("Enter book author: ");
                    String author = scanner.nextLine();
                    System.out.print("Enter book price: ");
                    double price = scanner.nextDouble();
                    scanner.nextLine();
                    addBook.addBook(title, author, price);
                    break;
                case 2:
//                     Display offers
//                     Ensure DisplayOffers class is properly implemented
//                     If not, you can remove this part or implement it correctly
//                     admin.displayOffers();
                    break;
                case 3:
                    System.out.print("Enter book title to update inventory: ");
                    String bookTitle = scanner.nextLine();
                    System.out.print("Enter new stock quantity: ");
                    int stock = scanner.nextInt();
                    scanner.nextLine();
                    updateInventory.updateInventoryByName(bookTitle, stock); // Call updateInventory method directly
                    break;
                case 4:
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    // Method to verify the admin password
    private boolean verifyAdminPassword(Scanner scanner) {
        System.out.print("Enter admin password: ");
        String inputPassword = scanner.nextLine();
        return inputPassword.equals(adminPassword);
    }
}
