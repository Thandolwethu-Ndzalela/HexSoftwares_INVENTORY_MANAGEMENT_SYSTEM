package database_connectivity;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database_Manager {
    // Defining the base URL part
    private static final String DB_BASE_URL = "jdbc:ucanaccess://";

    // Defining the file path (Update this path if necessary)
    private static final String DB_FILE_PATH = "C:/Users/thand/OneDrive/Desktop/Application/" +
            "Hex_Internship/HexSoftwares_INVENTORY_MANAGEMENT_SYSTEM/.idea/resources/Inventory.accdb";

    // Combine the base URL and file path to form the full URL
    private static final String DB_URL = DB_BASE_URL + DB_FILE_PATH; // MS Access database
    private static final String USER = "";
    private static final String PASSWORD = "";

    public static Connection getConnection() {
        // Validate if the database file exists before trying to connect
        File dbFile = new File(DB_FILE_PATH);
        if (!dbFile.exists()) {
            System.err.println("Error: Database file not found at: " + DB_FILE_PATH);
            return null;
        }

        try {
            Connection connection = DriverManager.getConnection(DB_URL, USER, PASSWORD);
            System.out.println("Welcome to the Access Database, you have connected successfully.");
            return connection;
        } catch (SQLException e) {
            System.err.println("Database connection failed: " + e.getMessage());
            return null;
        }
    }
}
