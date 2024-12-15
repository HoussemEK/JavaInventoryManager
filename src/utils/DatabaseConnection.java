package utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static Connection connection; 

    
    private DatabaseConnection() throws SQLException {
        try {
            
            Class.forName("com.mysql.jdbc.Driver");

            
            connection = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/inventory_db", 
                "root", 
                ""      
            );

            System.out.println("Connexion à la base de données établie avec succès.");
        } catch (ClassNotFoundException e) {
            System.err.println("Driver MySQL introuvable.");
            e.printStackTrace();
        }
    }

    
    public static Connection getInstance() throws SQLException {
        if (connection == null || connection.isClosed()) {
            new DatabaseConnection(); 
        }
        return connection;
    }
}
