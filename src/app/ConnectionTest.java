package app;

import db.DBConnection;

public class ConnectionTest {
    public static void main(String[] args) {
        System.out.println(DBConnection.testConnection() ? "CONNECTED" : "FAILED");
    }
}
 