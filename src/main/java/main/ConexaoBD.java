package main.java.main;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexaoBD {
    private static final String URL = "jdbc:ucanaccess://C:/Users/ALOCHIO/Desktop/workspace/Estacionamento/src/main/java/resources/estacionamento.accdb";

    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
            return DriverManager.getConnection(URL);
        } catch (ClassNotFoundException e) {
            throw new SQLException("Driver JDBC não encontrado", e);
        }
    }
}
