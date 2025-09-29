package java_irc_chat_client;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
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

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

import org.pircbotx.PircBotX;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.PrivateMessageEvent;
import Cliente_DCC.DCCManager;

public class PrivadoController extends ListenerAdapter {

    @FXML private BorderPane rootPane;
    @FXML private VBox chatBox;
    @FXML private ScrollPane chatScrollPane;
    @FXML private TextField inputField_privado;
    @FXML private ProgressBar progressBar;

    private PircBotX bot;
    private String destinatario;
    private ChatController mainController;
    private SymbolMapper symbolMapper;
    private DCCManager dccManager;

    public void setBot(PircBotX bot) { this.bot = bot; }
    public void setDestinatario(String nick) { this.destinatario = nick; }
    public void setMainController(ChatController mainController) { this.mainController = mainController; }
    public void setDccManager(DCCManager manager) { this.dccManager = manager; }
    public BorderPane getRootPane() { return rootPane; }
    public PircBotX getBot() { return this.bot; }

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
            if (c == '\u0003') {
                i++;
                StringBuilder num = new StringBuilder();
                while (i < mensaje.length() && Character.isDigit(mensaje.charAt(i))) num.append(mensaje.charAt(i++));
                try { currentColor = ircColorToFX(Integer.parseInt(num.toString())); } 
                catch (Exception e) { currentColor = Color.BLACK; }
            } else if (c == '\u000F') {
                currentColor = Color.BLACK; bold = false; i++;
            } else if (c == '\u0002') {
                bold = !bold; i++;
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

    




    private long ipToLong(String ip) {
        String[] parts = ip.split("\\.");
        long result = 0;
        for (String part : parts) result = (result << 8) | (Integer.parseInt(part) & 0xFF);
        return result;
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

            aceptar.setOnAction(e -> { accepted[0] = true; stage.close(); });
            rechazar.setOnAction(e -> { accepted[0] = false; stage.close(); });

            vbox.getChildren().addAll(label, aceptar, rechazar);
            stage.setScene(new Scene(vbox));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
        });

        while (Platform.isFxApplicationThread() && !Platform.isFxApplicationThread()) { /* Esperando… */ }

        return accepted[0];
    }

    public void iniciarBarraProgreso(String filename, long fileSize) {
        Platform.runLater(() -> {
            if (progressBar != null) { progressBar.setProgress(0); progressBar.setVisible(true); }
            appendSystemMessage("Transferencia iniciada de fichero: " + filename);
        });
    }

    public void actualizarBarraProgreso(double progreso) {
        Platform.runLater(() -> { if (progressBar != null) progressBar.setProgress(progreso); });
    }

    public void finalizarBarraProgreso(String filename) {
        Platform.runLater(() -> {
            if (progressBar != null) progressBar.setVisible(false);
            appendSystemMessage("Transferencia del archivo " + filename + " terminada en c:\\temp\\descargas");
        });
    }

    // ---------------- Listener para mensajes privados ----------------
 // -------------------- PrivadoController --------------------
    @Override
    public void onPrivateMessage(PrivateMessageEvent event) {
        String nick = event.getUser().getNick();
        String rawMsg = event.getMessage();

        System.out.println("DEBUG: Raw PRIVMSG recibido de " + nick + ": [" + rawMsg + "]");

        try {
            // Quitar delimitadores CTCP
            String mensaje = rawMsg.replace("\u0001", "").trim();

            // -------------------- DCC SEND --------------------
            if (mensaje.startsWith("DCC SEND ")) {
                String payload = mensaje.substring(9).trim();

                int firstQuote = payload.indexOf('"');
                int secondQuote = payload.indexOf('"', firstQuote + 1);

                if (firstQuote == -1 || secondQuote == -1) {
                    appendSystemMessage("⚠ Mensaje DCC SEND mal formado de " + nick + ": " + mensaje);
                    return;
                }

                // Token 0: nombre del archivo
                String fileName = payload.substring(firstQuote + 1, secondQuote);

                // Tokens 1,2,3: IP, puerto, tamaño
                String[] remainingTokens = payload.substring(secondQuote + 1).trim().split(" ");
                if (remainingTokens.length < 3) {
                    appendSystemMessage("⚠ DCC SEND incompleto de " + nick);
                    return;
                }

                long ipLong = Long.parseLong(remainingTokens[0]);
                int port = Integer.parseInt(remainingTokens[1]);
                long fileSize = Long.parseLong(remainingTokens[2]);
                String ip = longToIp(ipLong);

                // DEBUG: Enumerar tokens como en el emisor
                System.out.println("=== DEBUG: DCC SEND recibido ===");
                System.out.println("De: " + nick);
                System.out.println("Archivo: " + fileName);
                System.out.println("IP long: " + ipLong + " -> " + ip);
                System.out.println("Puerto: " + port);
                System.out.println("Tamaño: " + fileSize + " bytes");
                System.out.println("Tokens enumerados:");
                System.out.println("Token 0: \"" + fileName + "\"");
                System.out.println("Token 1: " + ipLong);
                System.out.println("Token 2: " + port);
                System.out.println("Token 3: " + fileSize);
                System.out.println("Mensaje bruto recibido (sin CTCP): " + mensaje);

                // Mostrar diálogo de aceptación en hilo de JavaFX
                Platform.runLater(() -> {
                    boolean accepted = showFileAcceptanceDialog(nick, fileName, fileSize);
                    if (accepted) {
                        String acceptMsg = "\u0001DCC ACCEPT \"" + fileName + "\" " + port + "\u0001";
                        bot.sendRaw().rawLineNow("PRIVMSG " + nick + " :" + acceptMsg);
                        System.out.println("DEBUG: DCC ACCEPT enviado: " + acceptMsg);

                        // Iniciar recepción
                        dccManager.receiveFile(nick, fileName, ip, port, fileSize);

                    } else {
                        String rejectMsg = "\u0001DCC REJECT \"" + fileName + "\"\u0001";
                        bot.sendRaw().rawLineNow("PRIVMSG " + nick + " :" + rejectMsg);
                        System.out.println("DEBUG: DCC REJECT enviado: " + rejectMsg);
                    }
                });
                return;
            }

            // -------------------- DCC ACCEPT --------------------
            if (mensaje.startsWith("DCC ACCEPT ")) {
                String[] tokens = mensaje.split(" ");
                if (tokens.length < 3) return;
                String fileName = tokens[2].replace("\"", "");
                int port = (tokens.length >= 4) ? Integer.parseInt(tokens[3]) : -1;

                System.out.println("DEBUG: DCC ACCEPT recibido de " + nick + " para " + fileName + " en puerto " + port);
                dccManager.startSendingFile(nick); // Solo ahora se inicia el envío de bytes
                return;
            }

            // -------------------- DCC REJECT --------------------
            if (mensaje.startsWith("DCC REJECT ")) {
                String[] tokens = mensaje.split(" ");
                if (tokens.length < 3) return;
                String fileName = tokens[2].replace("\"", "");
                System.out.println("DEBUG: DCC REJECT recibido de " + nick + " para " + fileName);
                appendSystemMessage("⚠ El usuario " + nick + " rechazó la transferencia de " + fileName);
                return;
            }

            // -------------------- Mensaje privado normal --------------------
            Platform.runLater(() -> appendMessage(nick, mensaje));

        } catch (Exception e) {
            appendSystemMessage("⚠ Error procesando mensaje privado de " + nick + ": " + e.getMessage());
            e.printStackTrace();
        }
    }

    // -------------------- sendDCCFile --------------------
    private void sendDCCFile(String filePath) {
        try {
            File file = new File(filePath);
            if (!file.exists()) return;

            appendSystemMessage("Solicitud de envío de archivo: " + file.getName() + " a " + destinatario);

            // Preparar ServerSocket
            ServerSocket serverSocket = new ServerSocket(0);
            int port = serverSocket.getLocalPort();
            String ip = InetAddress.getLocalHost().getHostAddress();
            long size = file.length();
            long ipLong = ipToLong(ip);

            // Tokens para DCC SEND
            String safeFilename = file.getName().replace("\"", "\\\"");
            String[] tokens = { "\"" + safeFilename + "\"", String.valueOf(ipLong), String.valueOf(port), String.valueOf(size) };

            // Mensaje bruto y debug token por token
            StringBuilder rawMsgBuilder = new StringBuilder("DCC SEND");
            System.out.println("=== DEBUG: Preparando DCC SEND ===");
            System.out.println("Archivo: " + file.getAbsolutePath());
            System.out.println("Tamaño (bytes): " + size);
            System.out.println("IP local: " + ip + " -> ipLong=" + ipLong);
            System.out.println("Puerto ServerSocket: " + port);

            for (int i = 0; i < tokens.length; i++) {
                rawMsgBuilder.append(" ").append(tokens[i]);
                System.out.println("Token " + i + ": " + tokens[i]);
            }

            String rawMsg = rawMsgBuilder.toString();
            System.out.println("Mensaje bruto a enviar: " + rawMsg);

            String ctcp = "\u0001" + rawMsg + "\u0001";
            System.out.println("Mensaje final CTCP a enviar: " + ctcp);

            // Guardamos en DCCManager para usar más tarde si el receptor acepta
            DCCManager.addOutgoingFile(destinatario, file, serverSocket, port, ipLong, size);

            // Enviar mensaje DCC SEND al receptor
            bot.sendRaw().rawLineNow("PRIVMSG " + destinatario + " :" + ctcp);
            System.out.println("DEBUG: DCC SEND enviado al receptor." + destinatario);

            // ⚠ NOTA: no iniciamos la transferencia aquí. 
            // Esperamos el DCC ACCEPT en onPrivateMessage → entonces se llamará a dccManager.startSendingFile(nick).

        } catch (Exception e) {
            appendSystemMessage("⚠ Error al enviar archivo: " + e.getMessage());
            e.printStackTrace();
        }
    }





    private String longToIp(long ipLong) {
        return String.format("%d.%d.%d.%d",
                (ipLong >> 24) & 0xFF,
                (ipLong >> 16) & 0xFF,
                (ipLong >> 8) & 0xFF,
                ipLong & 0xFF);
    }
}
