package java_irc_chat_client;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Modality;
import javafx.stage.Stage;

import org.pircbotx.PircBotX;
import Cliente_DCC.DCCManager;

public class PrivadoController {

    @FXML private BorderPane rootPane;
    @FXML private VBox chatBox;
    @FXML private ScrollPane chatScrollPane;
    @FXML private TextField inputField_privado;

    private PircBotX bot;
    private String destinatario;
    private ChatController mainController;
    private SymbolMapper symbolMapper;

    public void setBot(PircBotX bot) { this.bot = bot; }
    public void setDestinatario(String nick) { this.destinatario = nick; }
    public void setMainController(ChatController mainController) { this.mainController = mainController; }
    public BorderPane getRootPane() { return rootPane; }

    @FXML
    public void initialize() {
        symbolMapper = new SymbolMapper();
        inputField_privado.setOnAction(e -> sendMessage());

        // Drag & Drop para archivos
        rootPane.setOnDragOver(event -> {
            if (event.getDragboard().hasFiles()) event.acceptTransferModes(javafx.scene.input.TransferMode.COPY);
            event.consume();
        });

        rootPane.setOnDragDropped(event -> {
            var db = event.getDragboard();
            if (db.hasFiles()) {
                db.getFiles().forEach(file -> {
                    if (bot != null && destinatario != null) {
                        sendDCCFile(file.getAbsolutePath());
                    }
                });
            }
            event.setDropCompleted(true);
            event.consume();
        });
    }


    private void sendMessage() {
        String text = inputField_privado.getText().trim();
        if (text.isEmpty() || bot == null || destinatario == null) return;

        bot.sendIRC().message(destinatario, text);
        appendMessage("Yo", text);

        // --- Guardar en el log del privado ---
        ChatLogger.log(destinatario, "Yo", text);

        inputField_privado.clear();
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
            if (c == '\u0003') { // Código de color IRC
                i++;
                StringBuilder num = new StringBuilder();
                while (i < mensaje.length() && Character.isDigit(mensaje.charAt(i))) num.append(mensaje.charAt(i++));
                try { currentColor = ircColorToFX(Integer.parseInt(num.toString())); } 
                catch (Exception e) { currentColor = Color.BLACK; }
            } else if (c == '\u000F') { // Reset
                currentColor = Color.BLACK;
                bold = false;
                i++;
            } else if (c == '\u0002') { // Negrita toggle
                bold = !bold;
                i++;
            } else {
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
        chatBox.layout();
        chatScrollPane.layout();
        chatScrollPane.setVvalue(1.0);
    }

    public void initializeChat() {
        Platform.runLater(() -> chatScrollPane.setVvalue(1.0));
    }
    
    private void sendDCCFile(String filePath) {
        try {
            var file = new java.io.File(filePath);
            if (!file.exists()) return;

            appendSystemMessage("Solicitud de envío de archivo: " + file.getName() + " a " + destinatario);

            // Mensaje al receptor
            bot.sendIRC().message(destinatario, "/DCC SEND " + file.getName() + " " + file.length());

            // Guardamos el archivo en un mapa para seguimiento si quieres
            DCCManager.addOutgoingFile(destinatario, file);
        } catch (Exception e) {
            appendSystemMessage("⚠ Error al enviar archivo: " + e.getMessage());
        }
    }
    
    public boolean showFileAcceptanceDialog(String nick, String fileName, long fileSize) {
        final boolean[] accepted = {false};

        Platform.runLater(() -> {
            Stage stage = new Stage();
            stage.setTitle("Aceptar archivo de " + nick);

            VBox vbox = new VBox(10);
            vbox.setStyle("-fx-padding: 10;");
            Label label = new Label(nick + " quiere enviarte el archivo:\n" + fileName + " (" + fileSize + " bytes)");

            Button aceptar = new Button("Aceptar");
            Button rechazar = new Button("Rechazar");

            aceptar.setOnAction(e -> {
                accepted[0] = true;
                stage.close();
            });

            rechazar.setOnAction(e -> {
                accepted[0] = false;
                stage.close();
            });

            vbox.getChildren().addAll(label, aceptar, rechazar);
            stage.setScene(new Scene(vbox));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
        });

        // Espera mientras se muestra el diálogo
        while (Platform.isFxApplicationThread() && !Platform.isFxApplicationThread()) {
            // Esperando…
        }

        return accepted[0];
    }


}


