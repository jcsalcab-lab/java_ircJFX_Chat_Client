package java_irc_chat_client;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class ToolBarController {

    private StackPane rightPane;
    private VBox leftPane;
    private ChatController chatController;
    private Button statusButton;

    public void setRightPane(StackPane rightPane) {
        this.rightPane = rightPane;
    }

    public void setLeftPane(VBox leftPane) {
        this.leftPane = leftPane;
    }

    @FXML
    public void abrirChat() {
        try {
            if (chatController == null) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("JIRCHAT_CONNECT_STAGE.fxml"));
                AnchorPane chatPane = loader.load();
                chatController = loader.getController();

                chatController.setLeftPane(leftPane);
                chatController.setRightPane(rightPane);

                rightPane.getChildren().clear();
                rightPane.getChildren().add(chatPane);

                chatController.connectToIRC();

                // Crear botón Status solo una vez
                if (leftPane != null && statusButton == null) {
                    statusButton = new Button("Status");
                    statusButton.setMaxWidth(Double.MAX_VALUE);
                    statusButton.setOnAction(e -> mostrarChat());
                    leftPane.getChildren().add(statusButton);
                }
            } else {
                // Si ya existe, solo mostrarlo
                mostrarChat();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void mostrarChat() {
        if (chatController != null && rightPane != null && chatController.getRootPane() != null) {
            rightPane.getChildren().clear();
            rightPane.getChildren().add(chatController.getRootPane());
        }
    }
}



