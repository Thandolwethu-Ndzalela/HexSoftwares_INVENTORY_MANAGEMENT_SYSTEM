//package Inventory_Managment_System;//package Inventory_Managment_System;
//import database_connectivity.Database_Manager;
//
//import java.sql.Connection;
//import java.sql.PreparedStatement;
//import java.sql.ResultSet;
//import java.sql.SQLException;
//import java.sql.Statement;
//import java.util.Scanner;
//
//public class Main {
//    private static Scanner scanner = new Scanner(System.in); // Use a single scanner instance
//
//    public static void main(String[] args) {
//        try {
//            Connection con = Database_Manager.getConnection();
//            Inventory_Menu menu = new Inventory_Menu("Inventory Management System", con);
//
//            menu.add_new_choice("Add Product", () -> addProduct(con));
//            menu.add_new_choice("View Products", () -> viewProducts(con));
//            menu.add_new_choice("View Transactions", () -> viewTransactions(con));
//            menu.add_new_choice("Process Transaction", () -> processTransaction(con));
//
//            menu.run();
//
//        } finally {
//            scanner.close(); // Close the scanner when exiting the program
//        }
//    }
//
//    private static void addProduct(Connection con) {
//        System.out.println("Adding a product...");
//        try {
//            System.out.print("Enter product name: ");
//            String name = scanner.nextLine();
//            System.out.print("Enter product price: ");
//            double price = scanner.nextDouble();
//            System.out.print("Enter product quantity: ");
//            int quantity = scanner.nextInt();
//            scanner.nextLine(); // Consume leftover newline
//
//            String query = "INSERT INTO Products (prod_name, price, quantity) VALUES (?, ?, ?)";
//            try (PreparedStatement stmt = con.prepareStatement(query)) {
//                stmt.setString(1, name);
//                stmt.setDouble(2, price);
//                stmt.setInt(3, quantity);
//                stmt.executeUpdate();
//            }
//
//            System.out.println("Product added successfully.");
//        } catch (SQLException e) {
//            System.out.println("Error adding product: " + e.getMessage());
//        }
//    }
//
//    private static void viewProducts(Connection con) {
//        System.out.println("Viewing products...");
//        String query = "SELECT * FROM Products";
//        try (Statement stmt = con.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
//            System.out.println("Product ID | Name | Price | Quantity");
//            while (rs.next()) {
//                System.out.printf("%d | %s | %.2f | %d\n",
//                        rs.getInt("prod_ID"),
//                        rs.getString("prod_name"),
//                        rs.getDouble("price"),
//                        rs.getInt("quantity"));
//            }
//        } catch (SQLException e) {
//            System.out.println("Error retrieving products: " + e.getMessage());
//        }
//    }
//    private static void viewTransactions(Connection con) { // NEW METHOD
//        System.out.println("Viewing transactions...");
//        String query = """
//                SELECT Transactions.trans_ID, Products.prod_name, Transactions.type, Transactions.quantity, Transactions.trans_date
//                FROM Transactions
//                JOIN Products ON Transactions.prod_ID = Products.prod_ID
//                ORDER BY Transactions.trans_date DESC
//                """;
//        try (Statement stmt = con.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
//            System.out.println("Trans ID | Product Name | Type | Quantity | Date");
//            while (rs.next()) {
//                System.out.printf("%d | %s | %s | %d | %s\n",
//                        rs.getInt("trans_ID"),
//                        rs.getString("prod_name"),
//                        rs.getString("type"),
//                        rs.getInt("quantity"),
//                        rs.getDate("trans_date").toString());
//            }
//        } catch (SQLException e) {
//            System.out.println("Error retrieving transactions: " + e.getMessage());
//        }
//    }
//
//    private static void processTransaction(Connection con) {
//        System.out.println("Processing transaction...");
//        try {
//            System.out.print("Enter product ID: ");
//            int productId = scanner.nextInt();
//            System.out.print("Enter quantity: ");
//            int quantity = scanner.nextInt();
//            scanner.nextLine(); // Consume leftover newline
//            System.out.print("Enter transaction type (purchase/sale): ");
//            String type = scanner.nextLine();
//
//            String query = "SELECT quantity FROM Products WHERE prod_ID = ?";
//            try (PreparedStatement stmt = con.prepareStatement(query)) {
//                stmt.setInt(1, productId);
//                try (ResultSet rs = stmt.executeQuery()) {
//                    if (rs.next()) {
//                        int currentQuantity = rs.getInt("quantity");
//
//                        if (type.equalsIgnoreCase("purchase")) {
//                            updateProductQuantity(con, productId, currentQuantity + quantity);
//                            logTransaction(con, productId, "purchase", quantity);
//                            System.out.println("Purchase transaction processed successfully.");
//                        } else if (type.equalsIgnoreCase("sale")) {
//                            if (currentQuantity >= quantity) {
//                                updateProductQuantity(con, productId, currentQuantity - quantity);
//                                logTransaction(con, productId, "sale", quantity);
//                                System.out.println("Sale transaction processed successfully.");
//                            } else {
//                                System.out.println("Insufficient stock for sale.");
//                            }
//                        }
//                    } else {
//                        System.out.println("Product not found.");
//                    }
//                }
//            }
//        } catch (SQLException e) {
//            System.out.println("Error processing transaction: " + e.getMessage());
//        }
//    }
//
//    private static void updateProductQuantity(Connection con, int productId, int newQuantity) throws SQLException {
//        String updateQuery = "UPDATE Products SET quantity = ? WHERE prod_ID = ?";
//        try (PreparedStatement updateStmt = con.prepareStatement(updateQuery)) {
//            updateStmt.setInt(1, newQuantity);
//            updateStmt.setInt(2, productId);
//            updateStmt.executeUpdate();
//        }
//    }
//
//    private static void logTransaction(Connection con, int productId, String type, int quantity) throws SQLException {
//        String insertTransQuery = "INSERT INTO Transactions (prod_ID, type, quantity, trans_date) VALUES (?, ?, ?, ?)";
//        try (PreparedStatement transStmt = con.prepareStatement(insertTransQuery)) {
//            transStmt.setInt(1, productId);
//            transStmt.setString(2, type);
//            transStmt.setInt(3, quantity);
//            transStmt.setDate(4, new java.sql.Date(System.currentTimeMillis()));
//            transStmt.executeUpdate();
//        }
//    }
//}
//
package Inventory_Managment_System;
import database_connectivity.Database_Manager;

import java.sql.*;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Main {
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) throws SQLException {
        try (Connection con = Database_Manager.getConnection()) {
            Inventory_Menu menu = new Inventory_Menu("Inventory Management System", con);

            menu.add_new_choice("Add Product", () -> addProduct(con));
            menu.add_new_choice("Delete Product", () -> deleteProduct(con));
            menu.add_new_choice("View Products", () -> viewProducts(con));
            menu.add_new_choice("View Transactions", () -> viewTransactions(con));
            menu.add_new_choice("Removing duplicate products", () -> removeDuplicateProducts(con));
            menu.add_new_choice("Process Transaction", () -> processTransaction(con));

            menu.run();
        } finally {
            scanner.close();
        }
    }

    private static void addProduct(Connection con) {
    System.out.println("Adding a product...");
    try {
        System.out.print("Enter product name: ");
        String name = scanner.nextLine().trim().toLowerCase(); // Normalize to lowercase

        System.out.print("Enter product price: ");
        double price = scanner.nextDouble();
        System.out.print("Enter product quantity: ");
        int quantity = scanner.nextInt();
        scanner.nextLine(); // Consume leftover newline

        // Check if product already exists
        String checkQuery = "SELECT prod_ID, quantity FROM Products WHERE LOWER(prod_name) = ?";
        try (PreparedStatement checkStmt = con.prepareStatement(checkQuery)) {
            checkStmt.setString(1, name);
            try (ResultSet rs = checkStmt.executeQuery()) {
                if (rs.next()) {
                    // Product exists: Update quantity
                    int existingId = rs.getInt("prod_ID");
                    int existingQuantity = rs.getInt("quantity");
                    int newQuantity = existingQuantity + quantity;

                    String updateQuery = "UPDATE Products SET quantity = ? WHERE prod_ID = ?";
                    try (PreparedStatement updateStmt = con.prepareStatement(updateQuery)) {
                        updateStmt.setInt(1, newQuantity);
                        updateStmt.setInt(2, existingId);
                        updateStmt.executeUpdate();
                    }
                    System.out.println("Product already exists. Quantity updated.");
                    return;
                }
            }
        }

        // Product does not exist: Insert new entry
        String insertQuery = "INSERT INTO Products (prod_name, price, quantity) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = con.prepareStatement(insertQuery)) {
            stmt.setString(1, name);
            stmt.setDouble(2, price);
            stmt.setInt(3, quantity);
            stmt.executeUpdate();
        }

        System.out.println("Product added successfully.");
    } catch (SQLException e) {
        System.out.println("Error adding product: " + e.getMessage());
    }
}
    private static void deleteProduct(Connection con) {
        System.out.println("Deleting a product...");
        try {
            // Get product name to delete
            System.out.print("Enter product name to delete: ");
            String name = scanner.nextLine().trim().toLowerCase(); // Normalize to lowercase

            // Check if the product exists
            String checkQuery = "SELECT prod_ID FROM Products WHERE LOWER(prod_name) = ?";
            try (PreparedStatement checkStmt = con.prepareStatement(checkQuery)) {
                checkStmt.setString(1, name);
                try (ResultSet rs = checkStmt.executeQuery()) {
                    if (rs.next()) {
                        int productId = rs.getInt("prod_ID");

                        // Delete the product
                        String deleteProductQuery = "DELETE FROM Products WHERE prod_ID = ?";
                        try (PreparedStatement deleteStmt = con.prepareStatement(deleteProductQuery)) {
                            deleteStmt.setInt(1, productId);
                            int rowsDeleted = deleteStmt.executeUpdate();

                            if (rowsDeleted > 0) {
                                System.out.println("Product deleted successfully.");
                            } else {
                                System.out.println("Error: Product could not be deleted.");
                            }
                        }
                    } else {
                        System.out.println("Product not found.");
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Error deleting product: " + e.getMessage());
        }
    }
    private static void viewProducts(Connection con) {
        System.out.println("Viewing products...");
        String query = "SELECT * FROM Products";
        try (Statement stmt = con.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
            System.out.printf("%-10s %-20s %-10s %-10s\n", "Product ID", "Name", "Price", "Quantity");
            System.out.println("------------------------------------------------------");
            while (rs.next()) {
                System.out.printf("%-10d %-20s %-10.2f %-10d\n",
                        rs.getInt("prod_ID"), rs.getString("prod_name"), rs.getDouble("price"), rs.getInt("quantity"));
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving products: " + e.getMessage());
        }
        System.out.println();
    }
    private static void viewTransactions(Connection con) {
        System.out.println("Viewing transactions...");
        String query = "SELECT Transactions.trans_ID, Products.prod_name, Transactions.type, Transactions.quantity, Transactions.trans_date " +
                "FROM Transactions JOIN Products ON Transactions.prod_ID = Products.prod_ID " +
                "ORDER BY Transactions.trans_date DESC";
        try (Statement stmt = con.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
            System.out.printf("%-10s %-20s %-10s %-10s %-20s\n", "Trans ID", "Product Name", "Type", "Quantity", "Date");
            System.out.println("------------------------------------------------------------------------------------------");
            while (rs.next()) {
                System.out.printf("%-10d %-20s %-10s %-10d %-20s\n",
                        rs.getInt("trans_ID"), rs.getString("prod_name"), rs.getString("type"),
                        rs.getInt("quantity"), rs.getDate("trans_date"));
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving transactions: " + e.getMessage());
        }
        System.out.println();
    }
    private static void processTransaction(Connection con) {
        System.out.println("Processing transaction...");
        try {
            int productId = getValidInteger("Enter product ID: ", con);
            int quantity = getValidInteger("Enter quantity: ", con);

            System.out.print("Enter transaction type (purchase/sale): ");
            String type = scanner.nextLine().trim().toLowerCase();

            if (!type.equals("purchase") && !type.equals("sale")) {
                System.out.println("Invalid transaction type.");
                return;
            }

            String query = "SELECT quantity FROM Products WHERE prod_ID = ?";
            try (PreparedStatement stmt = con.prepareStatement(query)) {
                stmt.setInt(1, productId);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        int currentQuantity = rs.getInt("quantity");

                        if (type.equals("purchase")) {
                            updateProductQuantity(con, productId, currentQuantity + quantity);
                            logTransaction(con, productId, "purchase", quantity);
                            System.out.println("Purchase transaction processed successfully.");
                        } else if (type.equals("sale")) {
                            if (currentQuantity >= quantity) {
                                updateProductQuantity(con, productId, currentQuantity - quantity);
                                logTransaction(con, productId, "sale", quantity);
                                System.out.println("Sale transaction processed successfully.");
                            } else {
                                System.out.println("Insufficient stock for sale.");
                            }
                        }
                    } else {
                        System.out.println("Product not found.");
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println("Error processing transaction: " + e.getMessage());
        }
    }
    private static int getValidInteger(String prompt, Connection con) {
        int value;
        while (true) {
            System.out.print(prompt);
            try {
                value = scanner.nextInt();
                scanner.nextLine(); // Consume newline
                if (value > 0) {
                    return value;
                } else {
                    System.out.println("Please enter a positive number.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a valid number.");
                scanner.nextLine();
            }
        }
    }
    private static void updateProductQuantity(Connection con, int productId, int newQuantity) throws SQLException {
        String updateQuery = "UPDATE Products SET quantity = ? WHERE prod_ID = ?";
        try (PreparedStatement updateStmt = con.prepareStatement(updateQuery)) {
            updateStmt.setInt(1, newQuantity);
            updateStmt.setInt(2, productId);
            updateStmt.executeUpdate();
        }
    }
    private static void logTransaction(Connection con, int productId, String type, int quantity) throws SQLException {
        String insertTransQuery = "INSERT INTO Transactions (prod_ID, type, quantity, trans_date) VALUES (?, ?, ?, ?)";
        try (PreparedStatement transStmt = con.prepareStatement(insertTransQuery)) {
            transStmt.setInt(1, productId);
            transStmt.setString(2, type);
            transStmt.setInt(3, quantity);
            transStmt.setDate(4, new java.sql.Date(System.currentTimeMillis()));
            transStmt.executeUpdate();
        }
    }
//    private static void removeDuplicateProducts(Connection con) {
//        System.out.println("Removing duplicate products...");
//
//        String findDuplicatesQuery = """
//        SELECT prod_name, MIN(prod_ID) as keep_id, SUM(quantity) as total_quantity, MAX(price) as latest_price
//        FROM Products
//        GROUP BY prod_name
//        HAVING COUNT(*) > 1
//    """;
//
//        try (Statement stmt = con.createStatement(); ResultSet rs = stmt.executeQuery(findDuplicatesQuery)) {
//            while (rs.next()) {
//                String productName = rs.getString("prod_name");
//                int keepId = rs.getInt("keep_id");
//                int totalQuantity = rs.getInt("total_quantity");
//                double latestPrice = rs.getDouble("latest_price");
//
//                // Update the kept product with the new total quantity and latest price
//                String updateQuery = "UPDATE Products SET quantity = ?, price = ? WHERE prod_ID = ?";
//                try (PreparedStatement updateStmt = con.prepareStatement(updateQuery)) {
//                    updateStmt.setInt(1, totalQuantity);
//                    updateStmt.setDouble(2, latestPrice);
//                    updateStmt.setInt(3, keepId);
//                    updateStmt.executeUpdate();
//                }
//
//                // Delete duplicate products except the kept one
//                String deleteQuery = "DELETE FROM Products WHERE prod_name = ? AND prod_ID <> ?";
//                try (PreparedStatement deleteStmt = con.prepareStatement(deleteQuery)) {
//                    deleteStmt.setString(1, productName);
//                    deleteStmt.setInt(2, keepId);
//                    deleteStmt.executeUpdate();
//                }
//
//                System.out.println("Merged and removed duplicates for: " + productName);
//            }
//        } catch (SQLException e) {
//            System.out.println("Error removing duplicates: " + e.getMessage());
//        }
//    }
private static void removeDuplicateProducts(Connection con) {
    System.out.println("Removing duplicate products...");
    try {
        // Step 1: Update Transactions to Reference the Kept Product
        String updateTransactions = "UPDATE TRANSACTIONS " +
                "SET TRANSACTIONS.product_ID = (" +
                "SELECT MIN(P2.prod_ID) " +
                "FROM Products AS P1, Products AS P2 " +
                "WHERE LCase(P1.prod_name) = LCase(P2.prod_name) " +
                "AND P1.prod_ID <> P2.prod_ID " +
                "AND TRANSACTIONS.product_ID = P1.prod_ID) " +
                "WHERE EXISTS (" +
                "SELECT 1 FROM Products AS P1, Products AS P2 " +
                "WHERE LCase(P1.prod_name) = LCase(P2.prod_name) " +
                "AND P1.prod_ID <> P2.prod_ID " +
                "AND TRANSACTIONS.product_ID = P1.prod_ID);";
        try (PreparedStatement pstmt = con.prepareStatement(updateTransactions)) {
            pstmt.executeUpdate();
        }

        // Step 2: Delete Duplicate Products
        String deleteDuplicates = "DELETE FROM Products " +
                "WHERE prod_ID IN (SELECT prod_ID FROM Products " +
                "WHERE LCase(prod_name) IN (SELECT LCase(prod_name) FROM Products " +
                "GROUP BY LCase(prod_name) HAVING COUNT(*) > 1) " +
                "AND prod_ID NOT IN (SELECT MIN(prod_ID) FROM Products " +
                "GROUP BY LCase(prod_name) HAVING COUNT(*) > 1));";
        try (PreparedStatement pstmt = con.prepareStatement(deleteDuplicates)) {
            pstmt.executeUpdate();
        }

        System.out.println("Duplicate products removed successfully.");
    } catch (SQLException e) {
        System.out.println("Error removing duplicates: " + e.getMessage());
    }
}
}
