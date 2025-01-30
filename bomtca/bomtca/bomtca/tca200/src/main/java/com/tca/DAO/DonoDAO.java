package com.tca.DAO;

import com.tca.Models.Dono;
import com.tca.db.DatabaseConnector;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DonoDAO {

    public int inserirDono(Dono dono) throws SQLException {
        String sql = "INSERT INTO Dono (dono_nome, dono_telefone, dono_email) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
    
            stmt.setString(1, dono.getNome());
            stmt.setString(2, dono.getTelefone());
            stmt.setString(3, dono.getEmail());
    
            stmt.executeUpdate();
    
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    int donoId = rs.getInt(1);
                    dono.setId(donoId); // Atualiza o ID no objeto Dono
                    return donoId;
                }
            }
        }
        return -1; // Retorna -1 se não conseguiu inserir
    }
    
    public void atualizarDono(Dono dono) throws SQLException {
        String sql = "UPDATE Dono SET dono_nome = ?, dono_telefone = ?, dono_email = ? WHERE dono_id = ?";

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, dono.getNome());
            stmt.setString(2, dono.getTelefone());
            stmt.setString(3, dono.getEmail());
            stmt.setInt(4, dono.getId());
            stmt.executeUpdate();
        }
    }

    public Dono buscarDonoPorId(int id) throws SQLException {
        String sql = "SELECT * FROM Dono WHERE dono_id = ?";

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Dono dono = new Dono();
                    dono.setId(rs.getInt("dono_id"));
                    dono.setNome(rs.getString("dono_nome"));
                    dono.setTelefone(rs.getString("dono_telefone"));
                    dono.setEmail(rs.getString("dono_email"));
                    return dono;
                }
            }
        }
        return null;
    }

    public List<Dono> buscarTodosDonos() throws SQLException {
        String sql = "SELECT * FROM Dono";
        List<Dono> donos = new ArrayList<>();

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Dono dono = new Dono();
                dono.setId(rs.getInt("dono_id"));
                dono.setNome(rs.getString("dono_nome"));
                dono.setTelefone(rs.getString("dono_telefone"));
                dono.setEmail(rs.getString("dono_email"));
                donos.add(dono);
            }
        }
        return donos;
    }

    public void excluirDono(int donoId) {
        String sql = "DELETE FROM Dono WHERE dono_id = ?";
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, donoId);
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Dono excluído com sucesso.");
            } else {
                System.out.println("Nenhum dono encontrado com o ID: " + donoId);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Dono buscarDonoPorTelefoneOuEmail(String telefone, String email) throws SQLException {
        String sql = "SELECT * FROM Dono WHERE dono_telefone = ? OR dono_email = ?";
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
    
            stmt.setString(1, telefone);
            stmt.setString(2, email);
    
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Dono dono = new Dono();
                    dono.setId(rs.getInt("dono_id"));
                    dono.setNome(rs.getString("dono_nome"));
                    dono.setTelefone(rs.getString("dono_telefone"));
                    dono.setEmail(rs.getString("dono_email"));
                    return dono;
                }
            }
        }
        return null;
    }    
}
