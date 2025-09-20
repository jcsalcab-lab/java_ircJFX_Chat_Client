package java_irc_chat_client;

import javafx.animation.FadeTransition;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.util.Duration;
import org.pircbotx.Channel;
import org.pircbotx.PircBotX;

import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class CanalController {

    @FXML private VBox chatBox;
    @FXML private ScrollPane chatScrollPane;
    @FXML private TextField inputField_canal;
    @FXML private ListView<String> userListView_canal;
    @FXML private AnchorPane popupPane;

    private String canal;
    private PircBotX bot;
    private ChatController mainController;
    private Channel canalChannel;

    // lista de usuarios mostrada en el ListView (incluye la línea de contador "Usuarios: n")
    private final ObservableList<String> users = FXCollections.observableArrayList();
    // cache de nicks sin prefijos
    private final List<String> nickCache = new ArrayList<>();
    private final List<String> currentMatches = new ArrayList<>();
    private int matchIndex = -1;
    private String lastPrefix = null;
    private Consumer<String> onUserDoubleClick;
    private SymbolMapper symbolMapper;

    // Conjunto con los nicks conocidos (minúsculas, sin prefijos)
    private Set<String> knownNicks = Collections.emptySet();

    public void setBot(PircBotX bot) { this.bot = bot; }
    public void setCanal(String canal) { this.canal = canal; }
    public void setMainController(ChatController mainController) { this.mainController = mainController; }
    public void setUserDoubleClickHandler(Consumer<String> handler) { this.onUserDoubleClick = handler; }
    public void setCanalChannel(Channel canalChannel) { this.canalChannel = canalChannel; }
    public Channel getCanalChannel() { return canalChannel; }

 
    
    @FXML
    public void initialize() {
        symbolMapper = new SymbolMapper();
        if (chatBox != null) chatBox.setStyle("-fx-background-color: #FFF8DC;");

        // Items en el ListView
        userListView_canal.setItems(users);

        // cellFactory: pinta filas alternas y destaca los usuarios conocidos en verde neón
        userListView_canal.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(item);

                    // si es la línea contador "Usuarios: X" la pintamos de otra forma
                    if (item.startsWith("Usuarios:")) {
                        setStyle("-fx-background-color: transparent; -fx-font-weight: bold;");
                        return;
                    }

                    // decidir estilo alterno por índice base (si no es conocido)
                    String baseStyle = getIndex() % 2 == 0
                            ? "-fx-background-color: #FFFACD; -fx-font-weight: bold;"
                            : "-fx-background-color: #ADD8E6; -fx-font-weight: bold;";

                    // comprobar si es conocido -> quitar prefijo @/+ y comparar en lowercase
                    String clean = item;
                    if (clean.startsWith("@") || clean.startsWith("+")) clean = clean.substring(1);
                    boolean esConocido = knownNicks.contains(clean.toLowerCase());

                    if (esConocido) {
                        // verde neón (fondo) + texto en negro
                        setStyle("-fx-background-color: #39FF14; -fx-text-fill: black; -fx-font-weight: bold;");
                    } else {
                        setStyle(baseStyle);
                    }
                }
            }
        });

        // input events
        if (inputField_canal != null) {
            inputField_canal.setOnAction(e -> sendCommand());
            inputField_canal.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
                if (event.getCode() == KeyCode.TAB) { event.consume(); handleTabCompletion(event.isShiftDown()); }
            });
        }

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
            appendSystemMessage("➡ Saliendo de " + canal, MessageType.PART, bot.getNick());
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
            TextFlow flow = new TextFlow();
            Text userText = new Text("<" + usuario + "> ");
            userText.setFill(Color.DARKBLUE);
            userText.setFont(Font.font("System", FontWeight.BOLD, 12));
            flow.getChildren().add(userText);

            flow.getChildren().addAll(parseIRCMessage(mensaje).getChildren());

            chatBox.getChildren().add(flow);
            autoScroll();
        });
    }


    public void appendSystemMessage(String mensaje, MessageType type, String nickSalida) {
        Platform.runLater(() -> {
            TextFlow flow = new TextFlow();
            flow.setStyle("-fx-background-color: #FFF8DC; -fx-padding: 3px;");

            Text prefix = new Text("___________________________> ");
            Text body = new Text(mensaje);
            Text suffix = new Text(" <___________________________");

            switch (type) {
                case JOIN -> body.setFill(Color.web("#009300"));
                case PART -> body.setFill(Color.web("#FC7F00"));
                case KICK -> body.setFill(Color.web("#FF0000"));
            }

            prefix.setFill(Color.DARKGRAY);
            suffix.setFill(Color.DARKGRAY);
            prefix.setFont(Font.font("System", FontWeight.NORMAL, 12));
            body.setFont(Font.font("System", FontWeight.BOLD, 12));
            suffix.setFont(Font.font("System", FontWeight.NORMAL, 12));

            flow.getChildren().addAll(prefix, body, suffix);
            flow.setLineSpacing(1.2);

            chatBox.getChildren().add(flow);
            autoScroll();

            if (type == MessageType.PART && nickSalida != null) {
                showExitPopup(nickSalida, canal);
            }
        });
    }

    public void showExitPopup(String nick, String canal) {
        Platform.runLater(() -> {
            if (popupPane == null) return;

            StackPane bubble = new StackPane();
            bubble.setBackground(new Background(new BackgroundFill(Color.BEIGE, new CornerRadii(10), Insets.EMPTY)));
            bubble.setPadding(new Insets(10));
            bubble.setPrefWidth(200);

            Label label = new Label(nick + " sale de " + canal);
            label.setStyle("-fx-font-weight: bold; -fx-text-fill: #333333;");
            label.setWrapText(true);
            label.setMaxWidth(180);
            bubble.getChildren().add(label);

            Polygon triangle = new Polygon(0.0, 0.0, 10.0, 0.0, 5.0, 10.0);
            triangle.setFill(Color.BEIGE);

            VBox container = new VBox(bubble, triangle);
            container.setSpacing(0);
            container.setMouseTransparent(true);

            popupPane.getChildren().add(container);

            popupPane.applyCss();
            popupPane.layout();
            double paneWidth = popupPane.getWidth();
            double popupWidth = 200;
            container.setLayoutX((paneWidth - popupWidth) / 2);
            container.setLayoutY(20);

            TranslateTransition tt = new TranslateTransition(Duration.seconds(5), container);
            tt.setByY(-30);

            FadeTransition ft = new FadeTransition(Duration.seconds(5), container);
            ft.setFromValue(1.0);
            ft.setToValue(0.0);

            tt.play();
            ft.play();

            ft.setOnFinished(e -> popupPane.getChildren().remove(container));
        });
    }

    private void autoScroll() {
        if (chatScrollPane != null) {
            chatBox.layout();
            chatScrollPane.layout();
            chatScrollPane.setVvalue(1.0);
        }
    }

    /**
     * Actualiza la lista de usuarios y el contador correctamente.
     * Esta función RELOADS la lista de usuarios conocidos desde XML cada vez que se llama,
     * de forma que los conocidos se reflejen inmediatamente.
     */
    public void updateUsers(List<String> userList) {
        Platform.runLater(() -> {
            // ---- recargar knownNicks desde XML (asegura que siempre usamos la versión actual) ----
            try {
                Set<String> recargado = KnownUsers.loadKnownNicks();
                this.knownNicks = (recargado != null) ? recargado : Collections.emptySet();
            } catch (Exception e) {
                this.knownNicks = Collections.emptySet();
            }

            // Depuración / debug: mostrar tamaño
            System.out.println("CanalController.updateUsers: knownNicks.size() = " + knownNicks.size());

            // ---- construir la lista de usuarios a mostrar ----
            users.clear();
            nickCache.clear();

            List<String> validUsers = userList.stream()
                    .filter(u -> u != null && !u.trim().isEmpty())
                    .sorted(this::compareNicks)
                    .collect(Collectors.toList());

            users.add("Usuarios: " + validUsers.size());
            users.addAll(validUsers);

            for (String u : validUsers) {
                nickCache.add(u.startsWith("@") || u.startsWith("+") ? u.substring(1) : u);
            }

            // Forzar refresco para que la cellFactory vuelva a pintar con la nueva knownNicks
            userListView_canal.setItems(null);
            userListView_canal.setItems(users);
            userListView_canal.refresh();
        });
    }

    private int compareNicks(String a, String b) {
        int rankA = getNickRank(a);
        int rankB = getNickRank(b);
        if (rankA != rankB) return Integer.compare(rankA, rankB);

        String cleanA = a.startsWith("@") || a.startsWith("+") ? a.substring(1) : a;
        String cleanB = b.startsWith("@") || b.startsWith("+") ? b.substring(1) : b;
        return cleanA.compareToIgnoreCase(cleanB);
    }

    private int getNickRank(String nick) {
        if (nick.startsWith("@")) return 0;
        if (nick.startsWith("+")) return 1;
        char c = nick.charAt(0);
        if (!Character.isLetterOrDigit(c)) return 2;
        return 3;
    }

    public void abrirChatPrivado(String nick) {
        if (mainController != null) mainController.abrirChatPrivado(nick);
    }

    public String getCanal() { return canal; }

    public enum MessageType { JOIN, PART, KICK }
    
    private TextFlow parseIRCMessage(String mensaje) {
        TextFlow flow = new TextFlow();
        Color currentColor = Color.BLACK;
        boolean bold = false;
        int i = 0;

        while (i < mensaje.length()) {
            char c = mensaje.charAt(i);
            if (c == '\u0003') { // Código de color
                i++;
                StringBuilder num = new StringBuilder();
                while (i < mensaje.length() && Character.isDigit(mensaje.charAt(i))) num.append(mensaje.charAt(i++));
                try { currentColor = ircColorToFX(Integer.parseInt(num.toString())); }
                catch (Exception e) { currentColor = Color.BLACK; }
            } else if (c == '\u000F') { currentColor = Color.BLACK; bold = false; i++; } // Reset
            else if (c == '\u0002') { bold = !bold; i++; } // Bold
            else {
                int codePoint = mensaje.codePointAt(i);
                Text t = new Text(new String(Character.toChars(codePoint)));
                t.setFill(currentColor);
                t.setFont(Font.font("System", bold ? FontWeight.BOLD : FontWeight.NORMAL, 12));
                flow.getChildren().add(t);
                i += Character.charCount(codePoint);
            }
        }
        return flow;
    }

    private Color ircColorToFX(int code) {
        return switch (code) {
            case 0 -> Color.WHITE;
            case 1 -> Color.BLACK;
            case 2 -> Color.BLUE;
            case 3 -> Color.GREEN;
            case 4 -> Color.RED;
            case 5 -> Color.BROWN;
            case 6 -> Color.PURPLE;
            case 7 -> Color.ORANGE;
            case 8 -> Color.YELLOW;
            case 9 -> Color.LIGHTGREEN;
            case 10 -> Color.CYAN;
            case 11 -> Color.TURQUOISE;
            case 12 -> Color.DARKBLUE;
            case 13 -> Color.PINK;
            case 14 -> Color.GRAY;
            case 15 -> Color.LIGHTGRAY;
            default -> Color.BLACK;
        };
    }

}

