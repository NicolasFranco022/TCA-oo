package com.tca.Controllers;

import com.tca.DAO.AgendamentoDAO;
import com.tca.DAO.DonoDAO;
import com.tca.DAO.PetDAO;
import com.tca.Models.Agendamento;
import com.tca.Models.Dono;
import com.tca.Models.Pet;
import com.tca.db.DatabaseConnector;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.IOException;
import java.sql.Connection;
//import java.sql.PreparedStatement;
//import java.sql.ResultSet;
import java.sql.SQLException;
//import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

public class VerAgendamentosController {

    @FXML
    private TableView<Agendamento> agendamentosTable;

    @FXML
    private TableColumn<Agendamento, String> nomeDonoColumn;
    @FXML
    private TableColumn<Agendamento, String> telefoneDonoColumn;
    @FXML
    private TableColumn<Agendamento, String> emailDonoColumn;

    @FXML
    private TableColumn<Agendamento, String> nomePetColumn;
    @FXML
    private TableColumn<Agendamento, String> racaPetColumn;
    @FXML
    private TableColumn<Agendamento, String> tipoPetColumn;
    @FXML
    private TableColumn<Agendamento, Integer> idadePetColumn;
    @FXML
    private TableColumn<Agendamento, String> sexoPetColumn;

    @FXML
    private TableColumn<Agendamento, String> dataAgendamentoColumn;
    @FXML
    private TableColumn<Agendamento, String> motivoConsultaColumn;

    @FXML
    private TableColumn<Agendamento, Void> acoesColumn;

    @FXML
    private Button voltarButton; // O botão foi adicionado no FXML

    private ObservableList<Agendamento> agendamentosList;

    @FXML
    public void initialize() {
        agendamentosList = FXCollections.observableArrayList();
        configurarColunas();
        carregarAgendamentos();
        agendamentosTable.setItems(agendamentosList);
    }

    private Callback<TableColumn<Agendamento, Void>, TableCell<Agendamento, Void>> getAcoesCellFactory() {
        return column -> new TableCell<>() {
            private final Button excluirButton = new Button("Excluir");
            private final Button alterarButton = new Button("Alterar");

            {
                excluirButton.setOnAction(event -> handleExcluir(getTableView().getItems().get(getIndex())));
                alterarButton.setOnAction(event -> handleAlterar(getTableView().getItems().get(getIndex())));
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(new HBox(5, excluirButton, alterarButton));
                }
            }
        };
    }

    private Callback<TableColumn<Agendamento, String>, TableCell<Agendamento, String>> getMotivoButtonCellFactory() {
        return column -> new TableCell<Agendamento, String>() {
            private final Button viewButton = new Button("Ver Motivo");
    
            {
                // Quando o botão for clicado, ele exibe o motivo completo
                viewButton.setOnAction(event -> {
                    Agendamento agendamento = getTableView().getItems().get(getIndex());
                    mostrarMotivoCompleto(agendamento);
                });
            }
    
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(viewButton);
                }
            }
        };
    }

    private void mostrarMotivoCompleto(Agendamento agendamento) {
        // Criar um pop-up simples para exibir o motivo completo
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Motivo Completo");
        alert.setHeaderText("Motivo do Agendamento:");
        alert.setContentText(agendamento.getMotivoConsulta());  // Exibe o motivo completo
    
        alert.showAndWait();
    }


    private void configurarColunas() {
        // Ajustando o formato da data para exibir somente a data
        motivoConsultaColumn.setCellFactory(getMotivoButtonCellFactory());

        motivoConsultaColumn.setCellFactory(column -> new TableCell<Agendamento, String>() {
            private final Button viewButton = new Button("Ver Motivo");
    
            {
                // Quando o botão for clicado, ele exibe o motivo completo
                viewButton.setOnAction(event -> {
                    Agendamento agendamento = getTableView().getItems().get(getIndex());
                    mostrarMotivoCompleto(agendamento);
                });
            }
    
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(viewButton);
                }
            }
        });
    
        // Configura as outras colunas (sem alteração)
        nomeDonoColumn.setCellValueFactory(cellData -> {
            Pet pet = cellData.getValue().getPet();
            Dono dono = (pet != null) ? pet.getDono() : null;
            return new SimpleStringProperty(dono != null && dono.getNome() != null ? dono.getNome() : "Desconhecido");
        });

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        nomeDonoColumn.setCellValueFactory(cellData -> {
            Pet pet = cellData.getValue().getPet();
            Dono dono = (pet != null) ? pet.getDono() : null;
            return new SimpleStringProperty(dono != null && dono.getNome() != null ? dono.getNome() : "Desconhecido");
        });

        telefoneDonoColumn.setCellValueFactory(cellData -> {
            Pet pet = cellData.getValue().getPet();
            Dono dono = (pet != null) ? pet.getDono() : null;
            return new SimpleStringProperty(dono != null && dono.getTelefone() != null ? dono.getTelefone() : "Desconhecido");
        });

        emailDonoColumn.setCellValueFactory(cellData -> {
            Pet pet = cellData.getValue().getPet();
            Dono dono = (pet != null) ? pet.getDono() : null;
            return new SimpleStringProperty(dono != null && dono.getEmail() != null ? dono.getEmail() : "Desconhecido");
        });

        nomePetColumn.setCellValueFactory(cellData -> {
            Pet pet = cellData.getValue().getPet();
            return new SimpleStringProperty(pet != null ? pet.getNome() : "Desconhecido");
        });
        racaPetColumn.setCellValueFactory(cellData -> {
            Pet pet = cellData.getValue().getPet();
            return new SimpleStringProperty(pet != null ? pet.getRaca() : "Desconhecido");
        });
        tipoPetColumn.setCellValueFactory(cellData -> {
            Pet pet = cellData.getValue().getPet();
            return new SimpleStringProperty(pet != null ? pet.getTipo() : "Desconhecido");
        });
        idadePetColumn.setCellValueFactory(cellData -> {
            Pet pet = cellData.getValue().getPet();
            return new SimpleObjectProperty<>(pet != null ? pet.getIdade() : 0);
        });
        sexoPetColumn.setCellValueFactory(cellData -> {
            Pet pet = cellData.getValue().getPet();
            return new SimpleStringProperty(pet != null ? pet.getSexo() : "Desconhecido");
        });

        motivoConsultaColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getMotivoConsulta()));
        // Alterado para usar LocalDate
        dataAgendamentoColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDataAgendamento().format(formatter)));

        acoesColumn.setCellFactory(getAcoesCellFactory());
    }

    private AgendamentoDAO agendamentoDAO = new AgendamentoDAO();
    
    public void carregarAgendamentos() {

        agendamentosList.clear(); // Limpa a lista antes de carregar novos dados
    
        try {
            // Chama o método no DAO para obter todos os agendamentos
            List<Agendamento> agendamentos = agendamentoDAO.buscarTodosAgendamentos();
    
            // Adiciona os agendamentos carregados na lista
            agendamentosList.addAll(agendamentos);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void handleAlterar(Agendamento agendamento) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/tca/Views/TelaAlterarAgendamentos.fxml"));
            Stage stage = new Stage();
            stage.setScene(new Scene(loader.load()));

            AlterarAgendamentosController controller = loader.getController();
            controller.setAgendamento(agendamento);

            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Alterar Agendamento");
            stage.showAndWait();

            carregarAgendamentos(); // Atualiza os dados após a alteração
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void handleExcluir(Agendamento agendamento) {
        AgendamentoDAO agendamentoDAO = new AgendamentoDAO();
        PetDAO petDAO = new PetDAO();
        DonoDAO donoDAO = new DonoDAO();

        // Exibir mensagem de confirmação para o usuário
        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Confirmação de Exclusão");
        confirmAlert.setHeaderText("Deseja realmente excluir este agendamento?");
        confirmAlert.setContentText("Essa ação é irreversível e também excluirá o pet e o dono relacionados.");

        // Esperar pela resposta do usuário
        Optional<ButtonType> result = confirmAlert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            // Usuário confirmou a exclusão
            try (Connection conn = DatabaseConnector.getConnection()) {
                if (conn == null) {
                    System.err.println("Erro ao conectar ao banco de dados.");
                    return;
                }

                conn.setAutoCommit(false); // Inicia uma transação

                try {
                    // Exclui o agendamento
                    agendamentoDAO.excluirAgendamento(agendamento.getId());
                    System.out.println("Agendamento excluído do banco de dados.");

                    // Exclui o pet relacionado ao agendamento
                    Pet pet = agendamento.getPet();
                    if (pet != null) {
                        petDAO.excluirPet(pet.getId());
                        System.out.println("Pet excluído do banco de dados.");
                    }

                    // Exclui o dono relacionado ao pet
                    Dono dono = pet != null ? pet.getDono() : null;
                    if (dono != null) {
                        donoDAO.excluirDono(dono.getId());
                        System.out.println("Dono excluído do banco de dados.");
                    }

                    conn.commit(); // Confirma a transação

                    // Remove da lista e atualiza a interface
                    agendamentosList.remove(agendamento);
                    System.out.println("Agendamento removido da tela.");

                    // Exibe mensagem de sucesso
                    Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                    successAlert.setTitle("Sucesso");
                    successAlert.setHeaderText(null);
                    successAlert.setContentText("Agendamento excluído com sucesso!");
                    successAlert.showAndWait();

                } catch (SQLException e) {
                    conn.rollback(); // Reverte a transação em caso de erro
                    System.err.println("Erro ao excluir agendamento: " + e.getMessage());
                    e.printStackTrace();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            // Usuário cancelou a exclusão
            System.out.println("A exclusão foi cancelada pelo usuário.");
        }
    }                              

    @FXML
    private void handleVoltarTelaInicial() {
        try {
            // Carrega a tela inicial
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/tca/Views/TelaInicial.fxml"));
            Stage stage = new Stage();
            stage.setScene(new Scene(loader.load()));

            // Fecha a tela de visualização de agendamentos
            Stage currentStage = (Stage) agendamentosTable.getScene().getWindow();
            currentStage.close();

            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Tela Inicial");
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erro");
            alert.setHeaderText("Erro ao carregar a tela inicial");
            alert.setContentText("Houve um erro ao tentar carregar a tela inicial.");
            alert.showAndWait();
        }
    }

    // Método chamado ao clicar no botão "Ver Mais"
    public void handleVerMais() {
        Agendamento selectedAgendamento = agendamentosTable.getSelectionModel().getSelectedItem();

        if (selectedAgendamento != null) {
            // Criação de um pop-up para exibir o motivo completo
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Motivo Completo");
            alert.setHeaderText("Motivo do Agendamento:");
            alert.setContentText(selectedAgendamento.getMotivoConsulta());  // Exibe o motivo completo

            alert.showAndWait();
        } else {
            // Caso não tenha agendamento selecionado
            Alert alert = new Alert(AlertType.WARNING);
            alert.setTitle("Nenhum Agendamento Selecionado");
            alert.setHeaderText("Por favor, selecione um agendamento.");
            alert.showAndWait();
        }
    }

    
}