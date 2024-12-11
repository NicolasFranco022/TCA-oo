package com.tca;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Carregar o FXML da tela inicial
        Parent root = FXMLLoader.load(getClass().getResource("/com/tca/Views/TelaInicial.fxml"));
        
        // Configurar a janela principal
        primaryStage.setTitle("Sistema de Agendamento Veterinário");
        primaryStage.setScene(new Scene(root, 600, 450));
        primaryStage.show();
    }

    @Override
    public void init() throws Exception {
        super.init();
    }

    @Override
    public void stop() throws Exception {
        super.stop();
    }

    public static void main(String[] args) {
        launch(args);  // Iniciar a aplicação JavaFX
    }
}
