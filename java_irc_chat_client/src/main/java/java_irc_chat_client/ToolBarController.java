package java_irc_chat_client;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;

public class ToolBarController {

    private StackPane rightPane;
    private boolean baseChatOpen = false;
    private AnchorPane baseChat;

    public void setRightPane(StackPane rightPane) {
        this.rightPane = rightPane;
    }

    @FXML
    public void abrirChat() {
        if (!baseChatOpen) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/JIRCHAT_CONNECT_STAGE.fxml"));
                baseChat = loader.load();

                // Ajustar tamaño al rightPane
                baseChat.prefWidthProperty().bind(rightPane.widthProperty());
                baseChat.prefHeightProperty().bind(rightPane.heightProperty());

                // Anclar el AnchorPane al rightPane
                AnchorPane.setTopAnchor(baseChat, 0.0);
                AnchorPane.setBottomAnchor(baseChat, 0.0);
                AnchorPane.setLeftAnchor(baseChat, 0.0);
                AnchorPane.setRightAnchor(baseChat, 0.0);

                rightPane.getChildren().add(baseChat);
                baseChatOpen = true;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}



