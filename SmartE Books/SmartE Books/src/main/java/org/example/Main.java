package org.example;

import java.sql.Connection;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Main {
    private static final String ANSI_RED = "\u001B[31m";
    private static final String ANSI_GREEN = "\u001B[32m";
    private static final String ANSI_RESET = "\u001B[0m";

    private static String currentUserEmail = null;

    public static void main(String[] args) {
        Connection connection = DatabaseConnection.getConnection();
        if (connection == null) {
            System.err.println("Failed to connect to the database.");
            return;
        }

        Scanner scanner = new Scanner(System.in);
        RegisterUser registerUser = new RegisterUser(connection);
        LoginUser loginUser = new LoginUser(connection);
        LogoutUser logoutUser = new LogoutUser();
        SearchBooks searchBooks = new SearchBooks(connection);
        AddToCart cartService = new AddToCart(connection);
        ViewCart viewCartService = new ViewCart(connection);
        PlaceOrder orderService = new PlaceOrder(connection);
        AdminDashboard adminPage = new AdminDashboard(connection);

        while (true) {
            try {
                if (currentUserEmail == null) {
                    displayWelcomeMenu();
                    int choice = scanner.nextInt();
                    scanner.nextLine();

                    switch (choice) {
                        case 1:
                            System.out.print("Enter email: ");
                            String email = scanner.nextLine();
                            System.out.print("Enter password: ");
                            String password = scanner.nextLine();
                            registerUser.registerUser(email, password);
                            break;

                        case 2:
                            System.out.print("Enter email: ");
                            String loginEmail = scanner.nextLine();
                            System.out.print("Enter password: ");
                            String loginPassword = scanner.nextLine();
                            if (loginUser.login(loginEmail, loginPassword)) {
                                currentUserEmail = loginEmail;
                                System.out.println(ANSI_GREEN + "Login successful!" + ANSI_RESET);
                            } else {
                                System.out.println(ANSI_RED + "Invalid credentials." + ANSI_RESET);
                            }
                            break;

                        case 0:
                            System.out.println("Exiting...");
                            scanner.close();
                            return;

                        default:
                            System.out.println(ANSI_RED + "Invalid choice. Please try again." + ANSI_RESET);
                    }
                } else {
                    displayUserMenu();
                    int choice = scanner.nextInt();
                    scanner.nextLine();

                    switch (choice) {
                        case 3:
                            System.out.print("Enter book title or author to search: ");
                            String searchInput = scanner.nextLine();
                            searchBooks.displayBooks(searchInput);
                            break;

                        case 4:
                            System.out.print("Enter the book name to add to cart: ");
                            String bookName = scanner.nextLine();
                            cartService.addToCart(currentUserEmail, bookName);
                            break;

                        case 5:
                            System.out.println("Your current cart items:");
                            viewCartService.viewCart(currentUserEmail);
                            break;

                        case 6:
                            System.out.print("Enter book title: ");
                            String bookTitle = scanner.nextLine();
                            System.out.print("Enter quantity: ");
                            int quantity = scanner.nextInt();
                            orderService.placeOrder(currentUserEmail, bookTitle, quantity);
                            break;

                        case 7:
                            adminPage.displayAdminMenu(scanner);
                            break;

                        case 8:
                            logoutUser.logout();
                            currentUserEmail = null;
                            System.out.println(ANSI_GREEN + "You have been logged out." + ANSI_RESET);
                            break;

                        case 0:
                            System.out.println("Exiting...");
                            scanner.close();
                            return;

                        default:
                            System.out.println(ANSI_RED + "Invalid choice. Please try again." + ANSI_RESET);
                    }
                }
            } catch (InputMismatchException e) {
                System.out.println(ANSI_RED + "Invalid input. Please enter a number." + ANSI_RESET);
                scanner.nextLine();
            }
        }
    }

    private static void displayWelcomeMenu() {
        System.out.println("\n======= Welcome to the eBook Store! =======");
        System.out.println("1. Register");
        System.out.println("2. Login ");
        System.out.println("0. Logout ");
        System.out.print("Choose an option: ");
    }

    private static void displayUserMenu() {
        System.out.println("\n======= eBook Store Menu =======");
        System.out.println("3. Search Books");
        System.out.println("4. Add to Cart ");
        System.out.println("5. View Cart ");
        System.out.println("6. Place Order ");
        System.out.println("7. Admin Page ");
        System.out.println("8. Logout");
        System.out.println("0. Exit ");
        System.out.print("Choose an option: ");
    }
}
