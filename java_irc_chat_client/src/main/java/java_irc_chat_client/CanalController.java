package java_irc_chat_client;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import org.pircbotx.Channel;
import org.pircbotx.PircBotX;

import java.util.*;
import java.util.function.Consumer;

public class CanalController {

    @FXML private VBox chatBox;
    @FXML private ScrollPane chatScrollPane;
    @FXML private TextField inputField_canal;
    @FXML private ListView<String> userListView_canal;

    private String canal;
    private PircBotX bot;
    private ChatController mainController;
    private Channel canalChannel;

    private final ObservableList<String> users = FXCollections.observableArrayList();
    private final List<String> nickCache = new ArrayList<>();
    private final List<String> currentMatches = new ArrayList<>();
    private int matchIndex = -1;
    private String lastPrefix = null;
    private Consumer<String> onUserDoubleClick;
    private SymbolMapper symbolMapper;

    public CanalController() {}

    public void setBot(PircBotX bot) { this.bot = bot; }
    public void setCanal(String canal) { this.canal = canal; }
    public void setMainController(ChatController mainController) { this.mainController = mainController; }
    public void setUserDoubleClickHandler(Consumer<String> handler) { this.onUserDoubleClick = handler; }
    public void setCanalChannel(Channel canalChannel) { this.canalChannel = canalChannel; }
    public Channel getCanalChannel() { return canalChannel; }

    @FXML
    public void initialize() {
        symbolMapper = new SymbolMapper();
        userListView_canal.setItems(users);
        userListView_canal.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) { setText(null); setStyle(""); }
                else {
                    setText(item);
                    setStyle(getIndex() % 2 == 0 ? "-fx-background-color: #FFFACD; -fx-font-weight: bold;" 
                                                  : "-fx-background-color: #ADD8E6; -fx-font-weight: bold;");
                }
            }
        });

        inputField_canal.setOnAction(e -> sendCommand());
        inputField_canal.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == KeyCode.TAB) { event.consume(); handleTabCompletion(event.isShiftDown()); }
        });

        userListView_canal.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                String selectedUser = userListView_canal.getSelectionModel().getSelectedItem();
                if (selectedUser != null && !selectedUser.startsWith("Usuarios:")) {
                    abrirChatPrivado(selectedUser);
                    inputField_canal.setText("/msg " + selectedUser + " ");
                    inputField_canal.positionCaret(inputField_canal.getText().length());
                    if (onUserDoubleClick != null) onUserDoubleClick.accept(selectedUser);
                }
            }
        });
    }

    // --- TAB Autocompletado ---
    private void handleTabCompletion(boolean reverse) {
        String text = inputField_canal.getText();
        int caretPos = inputField_canal.getCaretPosition();
        int start = Math.max(0, text.lastIndexOf(' ', caretPos - 1) + 1);
        String prefix = text.substring(start, caretPos).trim();
        if (prefix.isEmpty()) return;

        if (!prefix.equalsIgnoreCase(lastPrefix)) {
            currentMatches.clear();
            for (String nick : users) {
                if (nick == null || nick.startsWith("Usuarios:")) continue;
                String cleanNick = nick.startsWith("@") || nick.startsWith("+") ? nick.substring(1) : nick;
                if (cleanNick.toLowerCase().startsWith(prefix.toLowerCase())) currentMatches.add(nick);
            }
            currentMatches.sort(String.CASE_INSENSITIVE_ORDER);
            lastPrefix = prefix;
            matchIndex = -1;
        }

        if (currentMatches.isEmpty()) return;

        matchIndex = reverse
                ? (matchIndex - 1 + currentMatches.size()) % currentMatches.size()
                : (matchIndex + 1) % currentMatches.size();

        String replacement = currentMatches.get(matchIndex);
        inputField_canal.setText(text.substring(0, start) + replacement + text.substring(caretPos));
        inputField_canal.positionCaret(start + replacement.length());
    }

    // --- Envío de mensajes ---
    private void sendCommand() {
        String text = inputField_canal.getText().trim();
        if (text.isEmpty() || bot == null) return;
        try {
            if (text.startsWith("/")) handleCommand(text.substring(1).trim());
            else sendMessageToChannel(text);
        } finally {
            inputField_canal.clear();
            lastPrefix = null;
            currentMatches.clear();
            matchIndex = -1;
        }
    }

    private void handleCommand(String cmd) {
        if (cmd.startsWith("part")) {
            bot.sendRaw().rawLine("PART " + canal);
            appendSystemMessage("➡ Saliendo de " + canal);
            if (mainController != null)
                Platform.runLater(() -> mainController.cerrarCanalDesdeVentana(canal));
        } else if (cmd.startsWith("msg ")) {
            String[] parts = cmd.split(" ", 3);
            if (parts.length >= 3) bot.sendIRC().message(parts[1], parts[2]);
        } else if (cmd.startsWith("me ")) bot.sendIRC().action(canal, cmd.substring(3).trim());
        else bot.sendRaw().rawLine(cmd);
    }

    public void sendMessageToChannel(String msg) {
        if (bot != null && canal != null) {
            bot.sendIRC().message(canal, msg);
            appendMessage("Yo", msg);
        }
    }

    public void appendMessage(String usuario, String mensaje) {
        Platform.runLater(() -> {
            Text userText = new Text("<" + usuario + "> ");
            userText.setFont(Font.font("Segoe UI Emoji", FontWeight.BOLD, 14));
            userText.setFill(Color.BLACK);

            TextFlow messageFlow = parseIRCMessage(mensaje);

            TextFlow fullFlow = new TextFlow(userText);
            fullFlow.getChildren().addAll(messageFlow.getChildren());

            chatBox.getChildren().add(fullFlow);
            autoScroll();
        });
    }

    public void appendSystemMessage(String mensaje) {
        Platform.runLater(() -> {
            TextFlow messageFlow = parseIRCMessage(mensaje);

            for (var t : messageFlow.getChildren()) {
                ((Text) t).setFill(Color.GRAY);
                ((Text) t).setFont(Font.font("Segoe UI Emoji", FontWeight.NORMAL, 13));
                ((Text) t).setStyle("-fx-font-style: italic;");
            }

            chatBox.getChildren().add(messageFlow);
            autoScroll();
        });
    }

    private TextFlow parseIRCMessage(String mensaje) {
        TextFlow flow = new TextFlow();
        int i = 0;
        Color currentColor = Color.BLACK;
        boolean bold = false;

        while (i < mensaje.length()) {
            char c = mensaje.charAt(i);
            if (c == '\u0003') { // color
                i++;
                StringBuilder num = new StringBuilder();
                while (i < mensaje.length() && Character.isDigit(mensaje.charAt(i))) num.append(mensaje.charAt(i++));
                try { currentColor = ircColorToFX(Integer.parseInt(num.toString())); } 
                catch (Exception e) { currentColor = Color.BLACK; }
            } else if (c == '\u000F') { currentColor = Color.BLACK; bold = false; i++; } // reset
            else if (c == '\u0002') { bold = !bold; i++; } // bold
            else {
                int codePoint = mensaje.codePointAt(i);
                String mapped = symbolMapper.mapChar((char) codePoint);
                Text t = new Text(mapped);
                t.setFill(currentColor);
                t.setFont(Font.font("Segoe UI Emoji", bold ? FontWeight.BOLD : FontWeight.NORMAL, 14));
                flow.getChildren().add(t);

                i += Character.charCount(codePoint);
            }
        }
        return flow;
    }

    private Color ircColorToFX(int code) {
        return switch (code) {
            case 0 -> Color.WHITE; case 1 -> Color.BLACK; case 2 -> Color.DODGERBLUE;
            case 3 -> Color.LIMEGREEN; case 4 -> Color.RED; case 5 -> Color.SADDLEBROWN;
            case 6 -> Color.MEDIUMPURPLE; case 7 -> Color.ORANGE; case 8 -> Color.GOLD;
            case 9 -> Color.GREEN; case 10 -> Color.CYAN; case 11 -> Color.TURQUOISE;
            case 12 -> Color.ROYALBLUE; case 13 -> Color.HOTPINK; case 14 -> Color.DARKGREY; case 15 -> Color.LIGHTGREY;
            default -> Color.BLACK;
        };
    }

    private void autoScroll() {
        if (chatScrollPane != null) {
            chatBox.layout();
            chatScrollPane.layout();
            chatScrollPane.setVvalue(1.0);
        }
    }

    public void abrirChatPrivado(String nick) {
        if (nick == null || nick.trim().isEmpty()) return;
        if (mainController != null) mainController.abrirChatPrivado(nick);
    }

    public void updateUsers(List<String> userList) {
        Platform.runLater(() -> {
            users.clear();
            nickCache.clear();
            List<String> validUsers = new ArrayList<>();
            for (String u : userList) if (u != null && !u.trim().isEmpty()) validUsers.add(u.trim());

            // Orden personalizado: @ > + > símbolos > letras/números
            validUsers.sort((a, b) -> {
                int rankA = getNickRank(a);
                int rankB = getNickRank(b);
                if (rankA != rankB) return Integer.compare(rankA, rankB);

                // Para moderadores, operadores y letras: ignorar mayúsculas
                if (rankA == 0 || rankA == 1 || rankA == 3) {
                    String cleanA = a.startsWith("@") || a.startsWith("+") ? a.substring(1) : a;
                    String cleanB = b.startsWith("@") || b.startsWith("+") ? b.substring(1) : b;
                    return cleanA.compareToIgnoreCase(cleanB);
                }

                // Para símbolos, ordenar por Unicode
                return a.compareTo(b);
            });

            users.add("Usuarios: " + validUsers.size());
            users.addAll(validUsers);

            for (String u : validUsers)
                nickCache.add(u.startsWith("@") || u.startsWith("+") ? u.substring(1) : u);
        });
    }

    private int getNickRank(String nick) {
        if (nick.startsWith("@")) return 0;
        if (nick.startsWith("+")) return 1;
        char c = nick.charAt(0);
        if (!Character.isLetterOrDigit(c)) return 2;
        return 3;
    }
}
