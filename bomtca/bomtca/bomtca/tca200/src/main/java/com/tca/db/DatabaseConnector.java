package com.tca.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnector {

    private static final String URL = "jdbc:mysql://wagnerweinert.com.br:3306/info23_JOAOVICTOR";
    private static final String USER = "info23_JOAOVICTOR"; // Substitua "seu_usuario" pelo usuário do banco de dados
    private static final String PASSWORD = "info23_JOAOVICTOR"; // Substitua "sua_senha" pela senha do banco de dados

    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver"); // Certifique-se de que o driver JDBC do MySQL está configurado no projeto
        } catch (ClassNotFoundException e) {
            throw new SQLException("Driver JDBC do MySQL não encontrado.", e);
        }
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public static void closeConnection(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                System.err.println("Erro ao fechar a conexão com o banco de dados: " + e.getMessage());
            }
        }
    }
}