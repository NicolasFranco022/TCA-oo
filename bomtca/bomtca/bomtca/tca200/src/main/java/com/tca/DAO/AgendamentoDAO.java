package com.tca.DAO;

import com.tca.Models.Agendamento;
import com.tca.Models.Dono;
import com.tca.Models.Pet;
import com.tca.db.DatabaseConnector;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class AgendamentoDAO {

    public void inserirAgendamento(Agendamento agendamento) throws SQLException {
        Pet pet = agendamento.getPet();
        Dono dono = agendamento.getDono();
    
        // Verificar se o pet_id é válido
        if (pet == null || pet.getId() <= 0) {
            throw new SQLException("Pet inválido ou não encontrado. ID do pet: " + pet.getId());
        }
    
        String sql = "INSERT INTO Agendamento (data_agendamento, pet_id, dono_id, motivo) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
    
            // Verifica se os objetos associados (Pet e Dono) possuem IDs válidos
            if (dono == null || dono.getId() <= 0) {
                throw new SQLException("Dono inválido para o agendamento.");
            }
    
            // Define os valores para o PreparedStatement
            stmt.setDate(1, java.sql.Date.valueOf(agendamento.getDataAgendamento()));  // Data no formato SQL
            stmt.setInt(2, pet.getId());  // Pet ID
            stmt.setInt(3, dono.getId()); // Dono ID
    
            String motivoConsulta = agendamento.getMotivoConsulta();
            if (motivoConsulta == null || motivoConsulta.trim().isEmpty()) {
                throw new SQLException("Motivo da consulta não pode ser vazio.");
            }
            stmt.setString(4, motivoConsulta); // Motivo da consulta
    
            // Executa a inserção no banco de dados
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Agendamento inserido com sucesso!");
            } else {
                throw new SQLException("Falha ao inserir agendamento no banco de dados.");
            }
        }
    }
    
    
    public boolean isDataDisponivel(LocalDate dataAgendamento) {
        String query = "SELECT COUNT(*) FROM Agendamento WHERE data_agendamento = ?";
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
    
            // Converte LocalDate para java.sql.Date para o banco de dados
            stmt.setDate(1, Date.valueOf(dataAgendamento));
    
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    int count = rs.getInt(1);
                    return count == 0; // Retorna true se não há agendamento nessa data
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false; // Retorna false em caso de erro ou se a data já estiver ocupada
    }
    
    

    
    public List<Agendamento> buscarTodosAgendamentos() throws SQLException {
        List<Agendamento> agendamentos = new ArrayList<>();
        String sql = "SELECT a.agendamento_id, a.data_agendamento, a.motivo, "
                   + "d.dono_id, d.dono_nome, d.dono_telefone, d.dono_email, "
                   + "p.pet_id, p.pet_nome, p.pet_raca, p.pet_tipo, p.pet_idade, p.pet_sexo "
                   + "FROM Agendamento a "
                   + "JOIN Pet p ON a.pet_id = p.pet_id "
                   + "JOIN Dono d ON p.dono_id = d.dono_id";
    
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
    
            while (rs.next()) {
                // Recuperar IDs
                int agendamentoId = rs.getInt("agendamento_id");
                int petId = rs.getInt("pet_id");
                int donoId = rs.getInt("dono_id");
    
                // Criar objeto Dono
                Dono dono = new Dono(donoId,
                                     rs.getString("dono_nome"),
                                     rs.getString("dono_telefone"),
                                     rs.getString("dono_email"));
    
                // Criar objeto Pet
                Pet pet = new Pet(petId,
                                  rs.getString("pet_nome"),
                                  rs.getString("pet_raca"),
                                  rs.getString("pet_tipo"),
                                  rs.getInt("pet_idade"),
                                  rs.getString("pet_sexo"),
                                  dono);
    
                // Criar objeto Agendamento
                Agendamento agendamento = new Agendamento(agendamentoId, pet,rs.getDate("data_agendamento").toLocalDate(), rs.getString("motivo"));
                // Adicionar agendamento à lista
                agendamentos.add(agendamento);
            }
        }
    
        return agendamentos;
    }
    
    public void excluirAgendamento(int agendamentoId) {
        String sql = "DELETE FROM Agendamento WHERE agendamento_id = ?";
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, agendamentoId);
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Agendamento excluído com sucesso.");
            } else {
                System.out.println("Nenhum agendamento encontrado com o ID: " + agendamentoId);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public void alterarAgendamento(Agendamento agendamento) {
        String sql = "UPDATE Agendamento SET data_agendamento = ?, motivo = ? WHERE agendamento_id = ?";

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setDate(1, java.sql.Date.valueOf(agendamento.getDataAgendamento()));  // Somente data
            stmt.setString(2, agendamento.getMotivoConsulta());
            stmt.setInt(3, agendamento.getId());
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public boolean verificarConsultaMesmoDia(LocalDate dataConsulta) {
        String sql = "SELECT COUNT(*) FROM agendamentos WHERE data_consulta = ?";
        try (Connection conn = DatabaseConnector.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setDate(1, Date.valueOf(dataConsulta));
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

}
