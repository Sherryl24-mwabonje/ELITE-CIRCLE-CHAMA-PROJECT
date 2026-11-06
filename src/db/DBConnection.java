package db;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Opens the JDBC connection to the MySQL database.
 * Settings are read from "db.properties".
 */
public class DBConnection {
    private static final String DEFAULT_URL = "jdbc:mysql://localhost:3306/elite_circle_chama";
    private static final String DEFAULT_USER = "root";
    private static final String DEFAULT_PASSWORD = "";

    /** Returns a NEW connection. Caller closes it with try-with-resources. */
    public static Connection getConnection() throws SQLException {
        Properties props = new Properties();
        try (FileInputStream in = new FileInputStream("db.properties")) {
            props.load(in);
        } catch (IOException e) {
            // No db.properties file: fall back to defaults
        }
        String url = props.getProperty("db.url", DEFAULT_URL);
        String user = props.getProperty("db.user", DEFAULT_USER);
        String password = props.getProperty("db.password", DEFAULT_PASSWORD);
        return DriverManager.getConnection(url, user, password);
    }

    /** Quick check used at start-up: true if the database can be reached. */
    public static boolean testConnection() {
        try (Connection con = getConnection()) {
            return con != null && !con.isClosed();
        } catch (SQLException e) {
            System.err.println("Connection failed: " + e.getMessage());
            return false;
        }
    }
}