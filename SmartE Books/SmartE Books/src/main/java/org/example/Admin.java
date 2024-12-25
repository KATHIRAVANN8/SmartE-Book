package org.example;

public interface Admin {
    void addBook(String title, String author, double price);
    void displayOffers();
    void updateInventory(int bookId, int newCount);
}
