package  java_irc_chat_client;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.scene.control.ScrollPane;
import org.pircbotx.PircBotX;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;
import javafx.scene.paint.Color;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;


public class CanalController {

	@FXML private VBox chatBox; 
	@FXML private ScrollPane chatScrollPane;

    @FXML private TextField inputField_canal;
    @FXML private ListView<String> userListView_canal;

    private String canal;
    private PircBotX bot;
    private ChatController mainController;

    // Lista que se muestra en el ListView (incluye encabezado)
    private final ObservableList<String> users = FXCollections.observableArrayList();
    private Consumer<String> onUserDoubleClick;

    // --- Caché limpia de nicks (solo nicks reales, sin "Usuarios: X") ---
    private final List<String> nickCache = new ArrayList<>();

    // --- Variables para autocompletado ---
    private String lastPrefix = null;
    private List<String> currentMatches = new ArrayList<>();
    private int matchIndex = 0;

    public CanalController() {}

    // --- Setters ---
    public void setBot(PircBotX bot) { this.bot = bot; }
    public void setCanal(String canal) { this.canal = canal; }
    public void setMainController(ChatController mainController) { this.mainController = mainController; }
    public void setUserDoubleClickHandler(Consumer<String> handler) { this.onUserDoubleClick = handler; }

    
    
  
    // --- Inicialización del FXML ---
    @FXML
    public void initialize() {
       

        userListView_canal.setItems(users);
        userListView_canal.getStylesheets().add(
        	    getClass().getResource("/java_irc_chat_client/style.css").toExternalForm()
        	);

        userListView_canal.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                String selectedUser = userListView_canal.getSelectionModel().getSelectedItem();
                if (selectedUser != null && !selectedUser.isEmpty() && !selectedUser.startsWith("Usuarios:")) {
                    inputField_canal.setText("/msg " + selectedUser + " ");
                    inputField_canal.requestFocus();
                    if (onUserDoubleClick != null) onUserDoubleClick.accept(selectedUser);
                }
            }
        });

        inputField_canal.setOnAction(e -> sendCommand());

        inputField_canal.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.TAB) {
                event.consume();
                handleTabCompletion(event.isShiftDown()); // shift+tab = atrás
            }
        });

        
        // Interceptamos TAB con un EventFilter para cogerlo antes del cambio de foco
        inputField_canal.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == KeyCode.TAB) {
                event.consume(); // evita que JavaFX cambie el foco
                // Pasamos si Shift está pulsado para permitir Shift+TAB (rotar hacia atrás)
                handleTabCompletion(event.isShiftDown());
            }
        });
    }

    /**
     * Autocompletado: busca nicks en nickCache que empiecen por el prefijo.
     * Si reverse==true (Shift+TAB) rota hacia atrás.
     */
 // --- Autocompletado con TAB ---
    private void handleTabCompletion(boolean reverse) {
        String text = inputField_canal.getText();
        int caretPos = inputField_canal.getCaretPosition();

        // Obtener la palabra actual (último espacio antes del cursor)
        int start = Math.max(0, text.lastIndexOf(' ', caretPos - 1) + 1);
        String prefix = text.substring(start, caretPos);

        if (prefix == null || prefix.trim().isEmpty()) return;
        prefix = prefix.trim();

        // 🔹 Si el prefijo cambió → recalcular coincidencias
        if (!prefix.equalsIgnoreCase(lastPrefix)) {
            currentMatches.clear();

            for (String u : users) {
                if (u == null || u.startsWith("Usuarios:")) continue;
                if (u.toLowerCase().startsWith(prefix.toLowerCase())) {
                    currentMatches.add(u);
                }
            }
            Collections.sort(currentMatches, String.CASE_INSENSITIVE_ORDER);

            lastPrefix = prefix;
            matchIndex = -1; // 👈 reseteamos a -1 para que el primer TAB apunte a 0
        }

        if (currentMatches.isEmpty()) return;

        // 🔹 Avanzar índice en cada TAB
        if (reverse) {
            matchIndex = (matchIndex - 1 + currentMatches.size()) % currentMatches.size();
        } else {
            matchIndex = (matchIndex + 1) % currentMatches.size();
        }

        String replacement = currentMatches.get(matchIndex);

        // Reemplazar en el TextField
        String newText = text.substring(0, start) + replacement + text.substring(caretPos);
        inputField_canal.setText(newText);

        // Cursor justo después del nick
        inputField_canal.positionCaret(start + replacement.length());
    }




    // --- Comandos y envío de mensajes ---
    private void sendCommand() {
        String text = inputField_canal.getText().trim();
        if (text.isEmpty() || bot == null) return;

        try {
            if (text.startsWith("/")) handleCommand(text.substring(1).trim());
            else sendMessageToChannel(text);
        } finally {
            inputField_canal.clear();
            // Reiniciar autocompletado después de enviar
            lastPrefix = null;
            currentMatches.clear();
            matchIndex = 0;
        }
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

        } else if (cmd.startsWith("me ")) {
            bot.sendIRC().action(canal, cmd.substring(3).trim());
        } else {
            bot.sendRaw().rawLine(cmd);
        }
    }

    public void sendMessageToChannel(String msg) {
        if (bot != null && canal != null) {
            bot.sendIRC().message(canal, msg);
            appendMessage("Yo", msg);
        }
    }

    public void appendMessage(String usuario, String mensaje) {
        Platform.runLater(() -> {
            // Nombre del usuario en negrita
            Text userText = new Text("<" + usuario + "> ");
            userText.setStyle("-fx-font-weight: bold; -fx-font-family: 'Arial';");

            // Parsear el mensaje con colores IRC y formato
            TextFlow messageFlow = parseIRCMessage(mensaje);

            // Combinar usuario + mensaje
            TextFlow fullFlow = new TextFlow();
            fullFlow.getChildren().add(userText);
            fullFlow.getChildren().addAll(messageFlow.getChildren());

            chatBox.getChildren().add(fullFlow);

            // Auto-scroll hacia abajo
            chatBox.layout();
            chatScrollPane.layout();
            chatScrollPane.setVvalue(1.0);
        });
    }

    /**
     * Parsea un mensaje IRC con códigos de color y negrita (\u0003 = color, \u0002 = bold, \u000F = reset)
     */
    private TextFlow parseIRCMessage(String mensaje) {
        TextFlow flow = new TextFlow();
        int i = 0;
        Color currentColor = Color.BLACK;
        boolean bold = false;

        while (i < mensaje.length()) {
            char c = mensaje.charAt(i);

            if (c == '\u0003') { // código de color
                i++;
                StringBuilder num = new StringBuilder();
                while (i < mensaje.length() && Character.isDigit(mensaje.charAt(i))) {
                    num.append(mensaje.charAt(i));
                    i++;
                }
                try {
                    int colorCode = Integer.parseInt(num.toString());
                    currentColor = ircColorToFX(colorCode);
                } catch (Exception e) {
                    currentColor = Color.BLACK;
                }
            } else if (c == '\u000F') { // reset
                currentColor = Color.BLACK;
                bold = false;
                i++;
            } else if (c == '\u0002') { // negrita
                bold = !bold;
                i++;
            } else {
                // Texto normal hasta el próximo código
                StringBuilder textChunk = new StringBuilder();
                while (i < mensaje.length() && "\u0003\u000F\u0002".indexOf(mensaje.charAt(i)) == -1) {
                    textChunk.append(mensaje.charAt(i));
                    i++;
                }
                Text t = new Text(textChunk.toString());
                t.setFill(currentColor);

                // Fuente clara y consistente
                if (bold) {
                    t.setFont(javafx.scene.text.Font.font("Arial", javafx.scene.text.FontWeight.BOLD, 14));
                } else {
                    t.setFont(javafx.scene.text.Font.font("Arial", javafx.scene.text.FontWeight.NORMAL, 14));
                }

                flow.getChildren().add(t);
            }
        }
        return flow;
    }


    /**
     * Mapea códigos de color IRC (0-15) a colores JavaFX
     */
    
    	private Color ircColorToFX(int code) {
    	    switch (code) {
    	        case 0: return Color.WHITE;
    	        case 1: return Color.BLACK;
    	        case 2: return Color.DODGERBLUE;   // más intenso que BLUE normal
    	        case 3: return Color.LIMEGREEN;    // verde vivo
    	        case 4: return Color.RED;
    	        case 5: return Color.SADDLEBROWN;
    	        case 6: return Color.MEDIUMPURPLE;
    	        case 7: return Color.ORANGE;
    	        case 8: return Color.GOLD;         // amarillo más fuerte
    	        case 9: return Color.GREEN;        // mejor que LIGHTGREEN
    	        case 10: return Color.CYAN;        // ya fuerte
    	        case 11: return Color.TURQUOISE;   // en vez de LIGHTCYAN
    	        case 12: return Color.ROYALBLUE;   // más intenso que LIGHTBLUE
    	        case 13: return Color.HOTPINK;     // rosa fuerte
    	        case 14: return Color.DARKGREY;    // en vez de GREY lavado
    	        case 15: return Color.LIGHTGREY;   // lo puedes dejar o cambiar a SILVER
    	        default: return Color.BLACK;
    	    }
    	}




    public void appendSystemMessage(String mensaje) {
        Platform.runLater(() -> {
            // Crear una copia local dentro del lambda
            String cleanMessage = mensaje.replaceAll("\\p{Cntrl}", "");

            Text text = new Text(cleanMessage); // 🔹 quitar \n
            text.setStyle("-fx-font-style: italic; -fx-fill: gray; -fx-font-family: 'Arial';");

            TextFlow textFlow = new TextFlow(text);
            chatBox.getChildren().add(textFlow);

            // Auto-scroll hacia abajo
            chatBox.layout();
            chatScrollPane.layout();
            chatScrollPane.setVvalue(1.0);
        });
    }




    // --- Actualización de usuarios ---
    public void updateUsers(List<String> userList) {
        Platform.runLater(() -> {
            users.clear();
            nickCache.clear();

            List<String> validUsers = new ArrayList<>();
            for (String u : userList) {
                if (u != null && !u.trim().isEmpty()) validUsers.add(u.trim());
            }
            validUsers.sort(String::compareToIgnoreCase);

            // mostramos encabezado + lista en el ListView (igual que antes)
            users.add("Usuarios: " + validUsers.size());
            users.addAll(validUsers);

            // y guardamos una caché limpia sólo con nicks, para autocompletar
            nickCache.addAll(validUsers);

            // Reiniciar estado de autocompletado
            lastPrefix = null;
            currentMatches.clear();
            matchIndex = 0;
        });
    }

}

