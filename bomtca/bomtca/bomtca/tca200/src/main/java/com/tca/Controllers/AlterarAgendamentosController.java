package com.tca.Controllers;

import com.tca.Models.Agendamento;
import com.tca.Models.Dono;
import com.tca.Models.Pet;
import com.tca.db.DatabaseConnector;
import com.tca.DAO.AgendamentoDAO;
import com.tca.DAO.DonoDAO;
import com.tca.DAO.PetDAO;

import javafx.fxml.FXML;

import java.sql.Connection;
import java.sql.SQLException;

import javafx.scene.control.*;
import javafx.stage.Stage;

public class AlterarAgendamentosController {
    
    @FXML
    private TextField motivoConsultaField;

    @FXML
    private DatePicker dataAgendamentoPicker;

    @FXML
    private Button salvarButton;
    @FXML
    private Button cancelarButton;

    @FXML
    private CheckBox alterarDonoCheckBox;
    @FXML
    private TextField nomeDonoField;
    @FXML
    private TextField telefoneDonoField;
    @FXML
    private TextField emailDonoField;

    @FXML
    private CheckBox alterarPetCheckBox;
    @FXML
    private TextField nomePetField;
    @FXML
    private TextField racaPetField;
    @FXML
    private TextField tipoPetField;
    @FXML
    private TextField idadePetField;
    @FXML
    private TextField sexoPetField;

    @FXML
    private CheckBox alterarMotivoCheckBox;
    @FXML
    private CheckBox alterarDataCheckBox;

    private Agendamento agendamento;
    private final AgendamentoDAO agendamentoDAO = new AgendamentoDAO();

    public void setAgendamento(Agendamento agendamento) {
        this.agendamento = agendamento;
        motivoConsultaField.setText(agendamento.getMotivoConsulta());
        dataAgendamentoPicker.setValue(agendamento.getDataAgendamento()); // Usando apenas data

        // Configuração inicial dos campos
        Dono dono = agendamento.getPet().getDono();
        Pet pet = agendamento.getPet();

        nomeDonoField.setText(dono.getNome());
        telefoneDonoField.setText(dono.getTelefone());
        emailDonoField.setText(dono.getEmail());

        nomePetField.setText(pet.getNome());
        racaPetField.setText(pet.getRaca());
        tipoPetField.setText(pet.getTipo());
        idadePetField.setText(String.valueOf(pet.getIdade()));
        sexoPetField.setText(pet.getSexo());

        // Desativar campos inicialmente
        toggleDonoFields();
        togglePetFields();
        toggleDataField();
        toggleMotivoField();
    }

    @FXML
    public void cancelar() {
        fecharJanela();
    }

    private void fecharJanela() {
        Stage stage = (Stage) salvarButton.getScene().getWindow();
        stage.close();
    }

    public void alteraDataIgual(Agendamento agendamento) {
        AgendamentoDAO agendamentoDAO = new AgendamentoDAO();
    
        // Verifica se a data está disponível
        if (!agendamentoDAO.isDataDisponivel(agendamento.getDataAgendamento())) {
            exibirMensagemErro("Não é possível alterar! Já existe um agendamento para esta data.");
            return; // Aborta a alteração
        }
    
        // Continua com a alteração se a data estiver disponível
        agendamentoDAO.alterarAgendamento(agendamento);
        exibirMensagemSucesso("Agendamento alterado com sucesso!");
    }
    
    // Método para exibir mensagens de erro
    private void exibirMensagemErro(String mensagem) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erro");
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }
    
    // Método para exibir mensagens de sucesso
    private void exibirMensagemSucesso(String mensagem) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Sucesso");
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }    

    // Métodos para habilitar/desabilitar campos
    @FXML
    private void toggleDonoFields() {
        boolean editable = alterarDonoCheckBox.isSelected();
        nomeDonoField.setEditable(editable);
        telefoneDonoField.setEditable(editable);
        emailDonoField.setEditable(editable);
    }

    @FXML
    private void togglePetFields() {
        boolean editable = alterarPetCheckBox.isSelected();
        nomePetField.setEditable(editable);
        racaPetField.setEditable(editable);
        tipoPetField.setEditable(editable);
        idadePetField.setEditable(editable);
        sexoPetField.setEditable(editable);
    }

    @FXML
    public void toggleDataField() {
        dataAgendamentoPicker.setDisable(!alterarDataCheckBox.isSelected());
    }

    @FXML
    public void toggleMotivoField() {
        motivoConsultaField.setEditable(alterarMotivoCheckBox.isSelected());
    }

    @FXML
    public void salvar() {
        if (agendamento != null) {
            // Atualiza os dados do agendamento
            agendamento.setMotivoConsulta(motivoConsultaField.getText());
    
            // Verifica se a data foi alterada antes de validar
            if (alterarDataCheckBox.isSelected()) {
                agendamento.setDataAgendamento(dataAgendamentoPicker.getValue());
    
                // Valida a nova data apenas se ela foi alterada
                if (!agendamentoDAO.isDataDisponivel(agendamento.getDataAgendamento())) {
                    exibirMensagemErro("Não é possível alterar! Já existe um agendamento para esta data.");
                    return; // Aborta a alteração
                }
            }
    
            // Atualiza os dados do dono e pet, se necessário
            Dono dono = agendamento.getPet().getDono();
            Pet pet = agendamento.getPet();
    
            if (alterarDonoCheckBox.isSelected()) {
                dono.setNome(nomeDonoField.getText());
                dono.setTelefone(telefoneDonoField.getText());
                dono.setEmail(emailDonoField.getText());
            }
    
            if (alterarPetCheckBox.isSelected()) {
                pet.setNome(nomePetField.getText());
                pet.setRaca(racaPetField.getText());
                pet.setTipo(tipoPetField.getText());
                pet.setIdade(Integer.parseInt(idadePetField.getText()));
                pet.setSexo(sexoPetField.getText());
            }
    
            // Atualiza os dados no banco de dados
            atualizarAgendamentoBancoDeDados();
    
            exibirMensagemSucesso("Agendamento alterado com sucesso!");
            fecharJanela();
        }
    }
    

    
    private void atualizarAgendamentoBancoDeDados() {
        try (Connection conn = DatabaseConnector.getConnection()) {
            if (conn == null) {
                System.err.println("Erro ao conectar ao banco de dados.");
                return;
            }
    
            conn.setAutoCommit(false); // Inicia a transação
    
            try {
                // Atualizar o agendamento
                AgendamentoDAO agendamentoDAO = new AgendamentoDAO();
                agendamentoDAO.alterarAgendamento(agendamento);
    
                // Atualizar o dono, se necessário
                if (alterarDonoCheckBox.isSelected()) {
                    DonoDAO donoDAO = new DonoDAO();
                    donoDAO.atualizarDono(agendamento.getPet().getDono());
                }
    
                // Atualizar o pet, se necessário
                if (alterarPetCheckBox.isSelected()) {
                    PetDAO petDAO = new PetDAO();
                    petDAO.atualizarPet(agendamento.getPet()); // Chamando o DAO corretamente
                    System.out.println("Dados do pet atualizados com sucesso!");
                } else {
                    System.out.println("Alteração do pet não foi selecionada.");
                }
    
                conn.commit(); // Confirma a transação
                System.out.println("Alterações salvas com sucesso!");
    
            } catch (SQLException e) {
                conn.rollback(); // Reverte a transação em caso de erro
                System.err.println("Erro ao salvar alterações: " + e.getMessage());
                e.printStackTrace();
            }
    
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }        
}