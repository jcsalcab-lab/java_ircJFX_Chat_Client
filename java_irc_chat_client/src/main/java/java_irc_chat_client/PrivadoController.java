package java_irc_chat_client;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
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

    @FXML private VBox chatBox;
    @FXML private ScrollPane chatScrollPane;
    @FXML private TextField inputField_privado;

    private String usuarioPrivado;
    private PircBotX bot;

    public PrivadoController() {}

    // --- Setters ---
    public void setBot(PircBotX bot) { this.bot = bot; }
    public void setUsuarioPrivado(String usuario) { this.usuarioPrivado = usuario; }

    // --- Inicialización del FXML ---
    @FXML
    public void initialize() {
        inputField_privado.setOnAction(e -> sendCommand());
    }

    // --- Comandos y envío de mensajes ---
    private void sendCommand() {
        String text = inputField_privado.getText().trim();
        if (text.isEmpty() || bot == null || usuarioPrivado == null) return;

        try {
            if (text.startsWith("/")) {
                handleCommand(text.substring(1).trim());
            } else {
                sendMessageToUser(text);
            }
        } finally {
            inputField_privado.clear();
        }
    }

    private void handleCommand(String cmd) {
        if (cmd.startsWith("me ")) {
            bot.sendIRC().action(usuarioPrivado, cmd.substring(3).trim());
        } else {
            bot.sendRaw().rawLine(cmd);
        }
    }

    public void sendMessageToUser(String msg) {
        if (bot != null && usuarioPrivado != null) {
            bot.sendIRC().message(usuarioPrivado, msg);
            appendMessage("Yo", msg);
        }
    }

    // --- Mostrar mensajes ---
    public void appendMessage(String usuario, String mensaje) {
        Platform.runLater(() -> {
            // Prefijo usuario
            Text userText = new Text("<" + usuario + "> ");
            userText.setFont(Font.font("Arial", FontWeight.BOLD, 14));
            userText.setFill(Color.BLACK);

            // Parsear contenido enriquecido
            TextFlow messageFlow = parseIRCMessage(mensaje);

            // Combinar
            TextFlow fullFlow = new TextFlow(userText);
            fullFlow.getChildren().addAll(messageFlow.getChildren());

            chatBox.getChildren().add(fullFlow);

            autoScroll();
        });
    }

    public void appendSystemMessage(String mensaje) {
        Platform.runLater(() -> {
            String cleanMessage = mensaje.replaceAll("\\p{Cntrl}", "");
            Text sysText = new Text(cleanMessage);
            sysText.setFont(Font.font("Arial", FontWeight.NORMAL, 13));
            sysText.setFill(Color.GRAY);
            sysText.setStyle("-fx-font-style: italic;");

            TextFlow flow = new TextFlow(sysText);
            chatBox.getChildren().add(flow);

            autoScroll();
        });
    }

    // --- Parseo enriquecido de mensajes ---
    private TextFlow parseIRCMessage(String mensaje) {
        TextFlow flow = new TextFlow();
        int i = 0;
        Color currentColor = Color.BLACK;
        boolean bold = false;

        while (i < mensaje.length()) {
            char c = mensaje.charAt(i);

            if (c == '\u0003') { // código de color IRC
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
            } else if (c == ':' && i + 1 < mensaje.length() && mensaje.charAt(i+1) == ':') {
                // soporte básico para emojis/íconos tipo ::smile::
                int end = mensaje.indexOf("::", i + 2);
                if (end > i) {
                    String iconKey = mensaje.substring(i + 2, end);
                    ImageView icon = loadIcon(iconKey);
                    if (icon != null) {
                        flow.getChildren().add(icon);
                    } else {
                        flow.getChildren().add(new Text("::" + iconKey + "::"));
                    }
                    i = end + 2;
                } else {
                    flow.getChildren().add(new Text(":"));
                    i++;
                }
            } else {
                // Texto normal
                StringBuilder textChunk = new StringBuilder();
                while (i < mensaje.length() && "\u0003\u000F\u0002:".indexOf(mensaje.charAt(i)) == -1) {
                    textChunk.append(mensaje.charAt(i));
                    i++;
                }
                Text t = new Text(textChunk.toString());
                t.setFill(currentColor);
                t.setFont(Font.font("Arial", bold ? FontWeight.BOLD : FontWeight.NORMAL, 14));
                flow.getChildren().add(t);
            }
        }

        return flow;
    }

    // --- Mapea códigos IRC a colores intensos ---
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

    // --- Carga de íconos básicos (ejemplo) ---
    private ImageView loadIcon(String key) {
        try {
            // Aquí podrías mapear claves a imágenes locales
            String path = "/icons/" + key + ".png"; // ruta en resources/icons
            Image img = new Image(getClass().getResourceAsStream(path));
            ImageView iv = new ImageView(img);
            iv.setFitWidth(16);
            iv.setFitHeight(16);
            return iv;
        } catch (Exception e) {
            return null;
        }
    }

    // --- Scroll automático ---
    private void autoScroll() {
        chatBox.layout();
        chatScrollPane.layout();
        chatScrollPane.setVvalue(1.0);
    }
}

