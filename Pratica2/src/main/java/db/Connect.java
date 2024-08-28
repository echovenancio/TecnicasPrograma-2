package db;

import java.sql.Connection;
import java.sql.DriverManager;

public class Connect {
    public static Connection connect() {
        try {
            String url = "jdbc:sqlite:database.db";
            return DriverManager.getConnection(url);
        } catch (Exception e) {
            System.err.println("Erro pegando conexão com banco: " + e);
            System.exit(1);
        }
        return null;
    }
}
