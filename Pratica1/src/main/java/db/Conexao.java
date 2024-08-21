package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexao {
    public static Connection conexao() {
        Connection conn = null;
        try {
            String url = "jdbc:sqlite:database.db";
            conn = DriverManager.getConnection(url);
        } catch (SQLException e) {
            System.err.println("Erro conectando ao banco: " + e.getMessage());
            System.exit(1);
        }
        return conn;
    }
}
