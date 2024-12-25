-- Create the database
CREATE DATABASE Ebookdb;

-- Use the created database
USE Ebookdb;

-- Create the users table
CREATE TABLE users (
    id INT AUTO_INCREMENT PRIMARY KEY,            -- Unique identifier for each user
    email VARCHAR(255) NOT NULL UNIQUE,           -- User's email, must be unique
    password VARCHAR(255) NOT NULL,                -- User's password
    is_verified BOOLEAN DEFAULT FALSE               -- Flag to indicate if the user is verified
);

-- Create the books table
CREATE TABLE books (
    id INT AUTO_INCREMENT PRIMARY KEY,            -- Unique identifier for each book
    title VARCHAR(255) NOT NULL,                  -- Title of the book
    author VARCHAR(255) NOT NULL,                 -- Author of the book
    price DECIMAL(10, 2) NOT NULL                 -- Price of the book with two decimal places
);

-- Create the cart table
CREATE TABLE cart (
    cart_id INT AUTO_INCREMENT PRIMARY KEY,       -- Unique identifier for each cart entry
    user_email VARCHAR(255) NOT NULL,             -- Email of the user
    book_id INT NOT NULL,                          -- Book identifier
    FOREIGN KEY (user_email) REFERENCES users(email), -- Foreign key referencing users
    FOREIGN KEY (book_id) REFERENCES books(id)    -- Foreign key referencing books
);

-- Create the orders table
CREATE TABLE orders (
    order_id INT AUTO_INCREMENT PRIMARY KEY,       -- Unique identifier for each order
    user_id INT NOT NULL,                          -- User ID of the person placing the order
    total DECIMAL(10, 2) NOT NULL,                -- Total price of the order
    FOREIGN KEY (user_id) REFERENCES users(id)    -- Foreign key referencing users
);
