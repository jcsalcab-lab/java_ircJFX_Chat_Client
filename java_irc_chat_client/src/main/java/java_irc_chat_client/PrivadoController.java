package java_irc_chat_client;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.pircbotx.PircBotX;

public class PrivadoController {

    // --- FXML ---
    @FXML private BorderPane rootPane;
    @FXML private VBox chatBox;
    @FXML private ScrollPane chatScrollPane;
    @FXML private TextField inputField_privado;

    // --- Variables ---
    private PircBotX bot;
    private String destinatario;           // Usuario destinatario
    private ChatController mainController; // Para redirigir mensajes entrantes
    private String usuarioPrivado;

    // --- Setters ---
    public void setBot(PircBotX bot) { this.bot = bot; }
    public void setDestinatario(String nick) { this.destinatario = nick; }
    public void setMainController(ChatController mainController) { this.mainController = mainController; }
    public void setUsuarioPrivado(String usuario) { this.usuarioPrivado = usuario; }

    // --- Getter para Stage ---
    public BorderPane getRootPane() { return rootPane; }

    // --- Inicialización del FXML ---
    @FXML
    public void initialize() {
        inputField_privado.setOnAction(e -> sendMessage());
    }

    // --- Inicialización adicional (scroll al final) ---
    public void initializeChat() {
        Platform.runLater(() -> chatScrollPane.setVvalue(1.0));
    }

    // --- Enviar mensaje ---
    private void sendMessage() {
        String text = inputField_privado.getText().trim();
        if (text.isEmpty() || bot == null || destinatario == null) return;

        // Enviar mensaje al servidor IRC
        bot.sendIRC().message(destinatario, text);

        // Mostrar mensaje en ventana propia
        appendMessage("Yo", text);

        // Limpiar campo
        inputField_privado.clear();
    }

    // --- Mostrar mensajes ---
    public void appendMessage(String usuario, String mensaje) {
        Platform.runLater(() -> {
            Text userText = new Text("<" + usuario + "> ");
            userText.setFont(Font.font("Arial", FontWeight.BOLD, 14));
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
            Text sysText = new Text(mensaje);
            sysText.setFont(Font.font("Arial", FontWeight.NORMAL, 13));
            sysText.setFill(Color.GRAY);
            sysText.setStyle("-fx-font-style: italic;");

            TextFlow flow = new TextFlow(sysText);
            chatBox.getChildren().add(flow);
            autoScroll();
        });
    }

    // --- Parseo de colores y formato IRC ---
    private TextFlow parseIRCMessage(String mensaje) {
        TextFlow flow = new TextFlow();
        int i = 0;
        Color currentColor = Color.BLACK;
        boolean bold = false;

        while (i < mensaje.length()) {
            char c = mensaje.charAt(i);

            if (c == '\u0003') { // color IRC
                i++;
                StringBuilder num = new StringBuilder();
                while (i < mensaje.length() && Character.isDigit(mensaje.charAt(i))) {
                    num.append(mensaje.charAt(i));
                    i++;
                }
                try { currentColor = ircColorToFX(Integer.parseInt(num.toString())); } 
                catch (Exception e) { currentColor = Color.BLACK; }
            } else if (c == '\u000F') { currentColor = Color.BLACK; bold = false; i++; }
            else if (c == '\u0002') { bold = !bold; i++; }
            else if (c == ':' && i + 1 < mensaje.length() && mensaje.charAt(i+1) == ':') {
                int end = mensaje.indexOf("::", i + 2);
                if (end > i) {
                    String iconKey = mensaje.substring(i + 2, end);
                    ImageView icon = loadIcon(iconKey);
                    if (icon != null) flow.getChildren().add(icon);
                    else flow.getChildren().add(new Text("::" + iconKey + "::"));
                    i = end + 2;
                } else { flow.getChildren().add(new Text(":")); i++; }
            } else {
                StringBuilder textChunk = new StringBuilder();
                while (i < mensaje.length() && "\u0003\u000F\u0002:".indexOf(mensaje.charAt(i)) == -1) {
                    textChunk.append(mensaje.charAt(i)); i++;
                }
                Text t = new Text(textChunk.toString());
                t.setFill(currentColor);
                t.setFont(Font.font("Arial", bold ? FontWeight.BOLD : FontWeight.NORMAL, 14));
                flow.getChildren().add(t);
            }
        }
        return flow;
    }

    private Color ircColorToFX(int code) {
        switch (code) {
            case 0: return Color.WHITE;
            case 1: return Color.BLACK;
            case 2: return Color.DODGERBLUE;
            case 3: return Color.LIMEGREEN;
            case 4: return Color.RED;
            case 5: return Color.SADDLEBROWN;
            case 6: return Color.MEDIUMPURPLE;
            case 7: return Color.ORANGE;
            case 8: return Color.GOLD;
            case 9: return Color.GREEN;
            case 10: return Color.CYAN;
            case 11: return Color.TURQUOISE;
            case 12: return Color.ROYALBLUE;
            case 13: return Color.HOTPINK;
            case 14: return Color.DARKGREY;
            case 15: return Color.LIGHTGREY;
            default: return Color.BLACK;
        }
    }

    private ImageView loadIcon(String key) {
        try {
            String path = "/icons/" + key + ".png";
            Image img = new Image(getClass().getResourceAsStream(path));
            ImageView iv = new ImageView(img);
            iv.setFitWidth(16);
            iv.setFitHeight(16);
            return iv;
        } catch (Exception e) { return null; }
    }

    private void autoScroll() {
        chatBox.layout();
        chatScrollPane.layout();
        chatScrollPane.setVvalue(1.0);
    }
}



