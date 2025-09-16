package java_irc_chat_client;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import org.pircbotx.PircBotX;

import java.util.List;
import java.util.function.Consumer;

public class CanalController {

    @FXML private TextArea chatArea_canal;
    @FXML private TextField inputField_canal;
    @FXML private ListView<String> userListView_canal;

    private String canal;
    private PircBotX bot;
    private final ObservableList<String> users = FXCollections.observableArrayList();
    private Consumer<String> onUserDoubleClick;

    private ChatController mainController;

    public CanalController() {}

    public void setBot(PircBotX bot) { this.bot = bot; }
    public void setCanal(String canal) { this.canal = canal; }
    public void setUserDoubleClickHandler(Consumer<String> handler) { this.onUserDoubleClick = handler; }
    public void setMainController(ChatController mainController) { this.mainController = mainController; }

    @FXML
    public void initialize() {
        chatArea_canal.setEditable(false);
        chatArea_canal.setWrapText(true);
        userListView_canal.setItems(users);

        userListView_canal.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                String selectedUser = userListView_canal.getSelectionModel().getSelectedItem();
                if (selectedUser != null && !selectedUser.isEmpty()) {
                    inputField_canal.setText("/msg " + selectedUser + " ");
                    inputField_canal.requestFocus();
                    if (onUserDoubleClick != null) onUserDoubleClick.accept(selectedUser);
                }
            }
        });

        inputField_canal.setOnAction(e -> sendCommand());
    }

    private void sendCommand() {
        String text = inputField_canal.getText().trim();
        if (text.isEmpty() || bot == null) return;
        try {
            if (text.startsWith("/")) handleCommand(text.substring(1).trim());
            else sendMessageToChannel(text);
        } finally { inputField_canal.clear(); }
    }

    private void handleCommand(String cmd) {
        if (cmd.startsWith("part")) {
            String[] parts = cmd.split(" ", 2);
            String message = parts.length == 2 ? parts[1] : "";

            if (!message.isEmpty()) bot.sendRaw().rawLine("PART " + canal + " :" + message);
            else bot.sendRaw().rawLine("PART " + canal);

            appendSystemMessage("➡ Saliendo de " + canal);

            if (mainController != null) {
                Platform.runLater(() -> mainController.cerrarCanalDesdeVentana(canal));
            }

        } else if (cmd.startsWith("msg ")) {
            String[] parts = cmd.split(" ", 3);
            if (parts.length >= 3) bot.sendIRC().message(parts[1], parts[2]);
        } else if (cmd.startsWith("me ")) bot.sendIRC().action(canal, cmd.substring(3).trim());
        else bot.sendRaw().rawLine(cmd);
    }

    public void sendMessageToChannel(String msg) {
        bot.sendIRC().message(canal, msg);
        appendMessage("Yo", msg);
    }

    public void appendMessage(String usuario, String mensaje) {
        Platform.runLater(() -> chatArea_canal.appendText("<" + usuario + "> " + mensaje + "\n"));
    }

    public void appendSystemMessage(String mensaje) {
        Platform.runLater(() -> chatArea_canal.appendText(mensaje + "\n"));
    }

    public void updateUsers(List<String> userList) {
        Platform.runLater(() -> {
            users.clear();
            userList.sort(String::compareToIgnoreCase);
            users.addAll(userList);
        });
    }
}





