package java_irc_chat_client;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class ToolBarController {

    private StackPane rightPane;
    private VBox leftPane;
    private ChatController chatController;
    private AnchorPane statusPane; // Referencia al pane del Status
    private AnchorPane currentFrontPane;  // El pane que está en primer plano actualmente

    public void setRightPane(StackPane rightPane) { this.rightPane = rightPane; }
    public void setLeftPane(VBox leftPane) { this.leftPane = leftPane; }
    public ChatController getChatController() { return chatController; }

    /**
     * Abrir chat y vincular floating windows al primaryStage.
     */
    @FXML
    public void abrirChat(Stage primaryStage) {
        if (chatController != null) return; // Ya abierto

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("JIRCHAT_CONNECT_STAGE.fxml"));
            AnchorPane chatPane = loader.load();
            chatController = loader.getController();

            // Vincular panes
            chatController.setLeftPane(leftPane);
            chatController.setRightPane(rightPane);

            // Añadir al rightPane
            rightPane.getChildren().add(chatPane);

            // Guardar statusPane y ponerlo en primer plano
            statusPane = chatController.getRootPane();
            currentFrontPane = statusPane;

            // Conectar al IRC
            chatController.connectToIRC();

            // Crear botón Status en el leftPane
            if (leftPane != null) {
                Button statusButton = new Button("Status");
                statusButton.setMaxWidth(Double.MAX_VALUE);
                statusButton.setOnAction(e -> showStatus());
                leftPane.getChildren().add(statusButton);
            }

            // Vincular ventanas flotantes al primaryStage
            if (primaryStage != null) {
                chatController.bindFloatingWindowsToRightPane(primaryStage);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Muestra el panel de estado en primer plano.
     */
    private void showStatus() {
        if (statusPane == null) return;

        // Solo si no hay otra ventana flotante en primer plano
        if (currentFrontPane == null || currentFrontPane == statusPane) {
            statusPane.toFront();
            currentFrontPane = statusPane;
        }
    }
}


