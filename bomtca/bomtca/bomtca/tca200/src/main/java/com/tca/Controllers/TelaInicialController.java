package com.tca.Controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class TelaInicialController {

    @FXML
    private Button btnCriarAgendamento;

    @FXML
    private Button btnVerAgendamentos;

    @FXML
    private void CadastrarAgendamentos() {
        System.out.println("Clicou no botão Criar Agendamento");

        try {   
            // Carrega a tela de cadastro de agendamento
            Parent root = FXMLLoader.load(getClass().getResource("/com/tca/Views/TelaCadastrarAgendamento.fxml"));
            Stage stage = (Stage) btnCriarAgendamento.getScene().getWindow();  // Acessa o Stage atual
            stage.setScene(new Scene(root, 600, 860));  // Altera a cena
            stage.centerOnScreen();
            stage.setTitle("Cadastrar Novo Agendamento");
            stage.show();  // Exibe a nova cena         
        } catch (Exception e) {
             System.out.println("Erro ao trocar para a tela de agendamento: " + e.getMessage());
             e.printStackTrace();
        }
    }

    @FXML
    private void VerAgendamentos() {
        System.out.println("Clicou no botão Ver Agendamentos");

        try {   
            // Carrega a tela de cadastro de agendamento
            Parent root = FXMLLoader.load(getClass().getResource("/com/tca/Views/TelaVerAgendamentos.fxml"));
            Stage stage = (Stage) btnVerAgendamentos.getScene().getWindow();  // Acessa o Stage atual
            stage.setScene(new Scene(root, 600, 860));  // Altera a cena
            stage.centerOnScreen();
            stage.setTitle("Ver Consultas");
            stage.show();  // Exibe a nova cena         
        } catch (Exception e) {
             System.out.println("Erro ao trocar para a tela de agendamento: " + e.getMessage());
             e.printStackTrace();
        }
    }
}
