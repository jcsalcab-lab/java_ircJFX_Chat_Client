package java_irc_chat_client;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.MenuBar;
import javafx.scene.control.SplitPane;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class MainApp extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        VBox rootVBox = new VBox();

        // --- Cargar menú ---
        MenuBar menuBar = FXMLLoader.load(getClass().getResource("menu.fxml"));
        rootVBox.getChildren().add(menuBar);

        // --- Cargar toolbar con controlador ---
        FXMLLoader toolbarLoader = new FXMLLoader(getClass().getResource("toolbar.fxml"));
        ToolBar toolBar = toolbarLoader.load();
        ToolBarController tbController = toolbarLoader.getController();

        // --- Crear SplitPane ---
        SplitPane splitPane = new SplitPane();

        // Lado izquierdo: VBox fijo
        VBox leftPane = new VBox();
        leftPane.setPrefWidth(150);
        leftPane.setMinWidth(150);
        leftPane.setMaxWidth(150);
        leftPane.setStyle("-fx-border-color: orange; -fx-border-width: 2;");
        leftPane.setSpacing(10);

        // Lado derecho: StackPane para chat
        StackPane rightPane = new StackPane();
        tbController.setRightPane(rightPane);  // pasar referencia al controlador

        splitPane.getItems().addAll(leftPane, rightPane);

        // Mantener divisor fijo
        splitPane.setDividerPositions(150.0 / 1200); // posición inicial
        splitPane.getDividers().get(0).positionProperty().addListener((obs, oldVal, newVal) -> {
            splitPane.getDividers().get(0).setPosition(150.0 / splitPane.getWidth());
        });

        VBox.setVgrow(splitPane, Priority.ALWAYS);

        // Añadir toolbar y splitPane al VBox principal
        rootVBox.getChildren().addAll(toolBar, splitPane);

        // Crear escena y mostrar
        Scene scene = new Scene(rootVBox, 1200, 800);
        primaryStage.setScene(scene);
        primaryStage.setTitle("JIRCHAT");
        primaryStage.show();

        // Abrir automáticamente la ventana de chat base en el rightPane
        tbController.abrirChat();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

