package com.tca.Controllers;

import com.tca.Models.Agendamento;
import com.tca.Models.Dono;
import com.tca.Models.Pet;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
//import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AgendamentoController {

    @FXML
    private TextField nomeDonoField;
    @FXML
    private TextField telefoneDonoField;
    @FXML
    private TextField emailDonoField;

    @FXML
    private TextField nomePetField;
    @FXML
    private TextField racaPetField;
    @FXML
    private TextField tipoPetField;
    @FXML
    private TextField idadePetField;

    @FXML
    private DatePicker dataAgendamentoPicker;
    @FXML
    private TextField motivoAgendamentoField;

    // Botões para o sexo do pet
    @FXML
    private Button machoButton;
    @FXML
    private Button femeaButton;

    private String sexoPet; // Variável para armazenar o sexo do pet

    // Inicialização dos componentes
    @FXML
    public void initialize() {
        // Formatação do telefone ao digitar
        telefoneDonoField.textProperty().addListener((observable, oldValue, newValue) -> {
            String texto = newValue.replaceAll("[^0-9]", ""); // Remove qualquer caractere não numérico

            if (texto.length() > 11) {
                texto = texto.substring(0, 11); // Limita o número de caracteres a 11 (telefone com DDD e celular)
            }

            if (texto.length() > 6) {
                texto = "(" + texto.substring(0, 2) + ") " + texto.substring(2, 3) + " " + texto.substring(3, 7) + "-" + texto.substring(7);
            } else if (texto.length() > 2) {
                texto = "(" + texto.substring(0, 2) + ") " + texto.substring(2);
            }

            telefoneDonoField.setText(texto);
        });
    }


    @FXML
    public void salvarAgendamento(ActionEvent event) {
        try {
            // Validar campos
            if (validarCampos()) {
                // Criar os objetos Dono e Pet
                Dono dono = new Dono(nomeDonoField.getText(), telefoneDonoField.getText(), emailDonoField.getText());
                int idadePet = Integer.parseInt(idadePetField.getText());
                Pet pet = new Pet(nomePetField.getText(), racaPetField.getText(), tipoPetField.getText(), idadePet, sexoPet);
                Agendamento agendamento = new Agendamento(dono, pet, dataAgendamentoPicker.getValue(), motivoAgendamentoField.getText());

                // Formatar a data para exibir no console
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                String dataFormatada = agendamento.getDataAgendamento().format(formatter);

                // Exibir dados no console
                System.out.println("Cadastro de Agendamento:");
                System.out.println("Dono: " + dono.getNome());
                System.out.println("Telefone: " + dono.getTelefone());
                System.out.println("Email: " + dono.getEmail());
                System.out.println("Pet: " + pet.getNome() + ", " + pet.getRaca() + ", " + pet.getIdade() + " anos");
                System.out.println("Sexo: " + pet.getSexo());
                System.out.println("Data Agendamento: " + dataFormatada);
                System.out.println("Motivo: " + agendamento.getMotivoConsulta());

                // Mensagem de sucesso
                mostrarMensagemSucesso("Cadastro Concluído!");

                // Voltar para a tela inicial
                voltarParaTelaInicial();
            } else {
                mostrarMensagemErro("Todos os campos devem ser preenchidos corretamente!");
            }
        } catch (NumberFormatException e) {
            mostrarMensagemErro("A idade do pet deve ser um número válido!");
        } catch (Exception e) {
            mostrarMensagemErro("Ocorreu um erro ao salvar o agendamento: " + e.getMessage());
        }
    }

    private boolean validarCampos() {
        return !nomeDonoField.getText().isEmpty() && !telefoneDonoField.getText().isEmpty()
                && !emailDonoField.getText().isEmpty() && !nomePetField.getText().isEmpty()
                && !racaPetField.getText().isEmpty() && !idadePetField.getText().isEmpty()
                && dataAgendamentoPicker.getValue() != null && !motivoAgendamentoField.getText().isEmpty()
                && sexoPet != null; // Verificar se o sexo foi selecionado
    }

    // Ação para os botões de sexo do pet (Macho/Fêmea)
    @FXML
    private void selecionarSexo(ActionEvent event) {
        Button source = (Button) event.getSource();
        sexoPet = source.getText(); // Atribui o texto do botão (Macho ou Fêmea)

        // Exibe uma mensagem de sucesso ao selecionar o sexo
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Sexo Selecionado");
        alert.setHeaderText(null);
        alert.setContentText("Você selecionou " + sexoPet + "!");
        alert.showAndWait();

        System.out.println("Sexo selecionado: " + sexoPet);
    }

    private void mostrarMensagemErro(String mensagem) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erro");
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }

    private void mostrarMensagemSucesso(String mensagem) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Sucesso");
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }

    private void voltarParaTelaInicial() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/tca/views/TelaInicial.fxml"));
            Stage stage = (Stage) nomeDonoField.getScene().getWindow();
            stage.setScene(new Scene(loader.load()));
            stage.setTitle("Tela Inicial");
        } catch (IOException e) {
            Logger.getLogger(AgendamentoController.class.getName()).log(Level.SEVERE, null, e);
        }
    }
}
