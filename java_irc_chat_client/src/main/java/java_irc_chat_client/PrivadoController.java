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
import javafx.stage.Window;
import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import javax.swing.JOptionPane;
import org.pircbotx.PircBotX;
import org.pircbotx.dcc.FileTransfer;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.PrivateMessageEvent;
import org.pircbotx.hooks.events.IncomingFileTransferEvent;
import org.pircbotx.hooks.types.GenericMessageEvent;
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

    private ChatController chatController;

    public void setChatController(ChatController chatController) {
        this.chatController = chatController;
    }

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

        // Ocultamos barra de progreso inicialmente
        progressBar.setVisible(false);
        progressBar.setProgress(0);

        // Drag & Drop para archivos
        rootPane.setOnDragOver(event -> {
            if (event.getDragboard().hasFiles())
                event.acceptTransferModes(javafx.scene.input.TransferMode.COPY);
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
                while (i < mensaje.length() && Character.isDigit(mensaje.charAt(i))) {
                    num.append(mensaje.charAt(i++));
                }
                try {
                    currentColor = ircColorToFX(Integer.parseInt(num.toString()));
                } catch (Exception e) {
                    currentColor = Color.BLACK;
                }
            } else if (c == '\u000F') {
                currentColor = Color.BLACK;
                bold = false;
                i++;
            } else if (c == '\u0002') {
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
            case 0 -> Color.WHITE;
            case 1 -> Color.BLACK;
            case 2 -> Color.DODGERBLUE;
            case 3 -> Color.LIMEGREEN;
            case 4 -> Color.RED;
            case 5 -> Color.SADDLEBROWN;
            case 6 -> Color.MEDIUMPURPLE;
            case 7 -> Color.ORANGE;
            case 8 -> Color.GOLD;
            case 9 -> Color.GREEN;
            case 10 -> Color.CYAN;
            case 11 -> Color.TURQUOISE;
            case 12 -> Color.ROYALBLUE;
            case 13 -> Color.HOTPINK;
            case 14 -> Color.DARKGREY;
            case 15 -> Color.LIGHTGREY;
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
        for (String part : parts) {
            result = (result << 8) | (Integer.parseInt(part) & 0xFF);
        }
        return result;
    }

    /** Enviar solicitud DCC SEND al otro usuario */
    private void sendDCCFile(String filePath) {
        try {
            File file = new File(filePath);
            if (!file.exists()) return;

            appendSystemMessage("Solicitud de envío de archivo: " + file.getName() + " a " + destinatario);

            ServerSocket serverSocket = new ServerSocket(0);
            int port = serverSocket.getLocalPort();
            String ip = InetAddress.getLocalHost().getHostAddress();
            long size = file.length();
            long ipLong = ipToLong(ip);

            String safeFilename = file.getName().replace("\"", "\\\"");
            String[] tokens = {
                "\"" + safeFilename + "\"",
                String.valueOf(ipLong),
                String.valueOf(port),
                String.valueOf(size)
            };

            StringBuilder rawMsgBuilder = new StringBuilder("DCC SEND");
            for (String token : tokens) {
                rawMsgBuilder.append(" ").append(token);
            }
            String rawMsg = rawMsgBuilder.toString();
            String ctcpMsg = "\u0001" + rawMsg + "\u0001";

            System.out.println("DEBUG: Preparando DCC SEND");
            System.out.println("Archivo: " + file.getAbsolutePath());
            System.out.println("Tamaño: " + size + " bytes");
            System.out.println("IP local: " + ip + " -> ipLong=" + ipLong);
            System.out.println("Puerto ServerSocket: " + port);
            System.out.println("Mensaje CTCP a enviar: " + ctcpMsg);

            dccManager.addOutgoingFile(destinatario, file, serverSocket, port, ipLong, size);

            bot.sendRaw().rawLineNow("PRIVMSG " + destinatario + " :" + ctcpMsg);
            System.out.println("DEBUG: DCC SEND enviado a " + destinatario);

            // Mostrar barra progreso al iniciar envío
            Platform.runLater(() -> {
                actualizarBarraProgreso(0);
                progressBar.setVisible(true);
            });

        } catch (Exception e) {
            appendSystemMessage("⚠ Error al enviar archivo: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Muestra un diálogo para aceptar o rechazar la recepción del archivo.
     *
     * @param nick El nick del remitente
     * @param filename El nombre del archivo
     * @param fileSize El tamaño del archivo en bytes
     * @return true si el usuario acepta la transferencia, false en caso contrario
     */
    public boolean showFileAcceptanceDialog(String nick, String filename, long fileSize) {
        String message = String.format(
            "%s quiere enviarte el archivo:\n%s\nTamaño: %d bytes\n¿Aceptar?",
            nick, filename, fileSize
        );

        int option = JOptionPane.showConfirmDialog(null, message, "Confirmar recepción de archivo",
            JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

        return option == JOptionPane.YES_OPTION;
    }
    

    /** Maneja mensajes entrantes (privados o CTCP) */
    public void handleIncomingPrivateMessage(String nick, String rawMsg) {
        System.out.println("DEBUG: handleIncomingPrivateMessage: Raw PRIVMSG recibido de " + nick + ": [" + rawMsg + "]");

        // Detectar CTCP DCC SEND o ACCEPT
        if (rawMsg.startsWith("\u0001DCC SEND")) {
            // Aquí podrías agregar lógica para aceptar o mostrar diálogo
            Platform.runLater(() -> {
                // Ejemplo básico: aceptar automáticamente
                boolean aceptar = mostrarDialogoAceptarArchivo(nick, rawMsg);
                if (aceptar) {
                    try {
                        dccManager.handleIncomingSend(bot, nick, rawMsg, this);
                        // Mostrar barra progreso al iniciar recepción
                        actualizarBarraProgreso(0);
                        progressBar.setVisible(true);
                    } catch (Exception e) {
                        appendSystemMessage("Error al aceptar archivo: " + e.getMessage());
                    }
                } else {
                    appendSystemMessage("Transferencia de archivo rechazada.");
                }
            });
        } else if (rawMsg.startsWith("\u0001DCC ACCEPT")) {
            // Procesar aceptación de archivo para iniciar envío
            try {
                dccManager.handleIncomingAccept(bot, nick, rawMsg, this);
                // Mostrar barra progreso al iniciar envío
                actualizarBarraProgreso(0);
                progressBar.setVisible(true);
            } catch (Exception e) {
                appendSystemMessage("Error al procesar aceptación: " + e.getMessage());
            }
        } else {
            // Mensaje normal
            appendMessage(nick, rawMsg);
        }
    }

    private boolean mostrarDialogoAceptarArchivo(String nick, String rawMsg) {
        // Aquí debes implementar un diálogo modal para que el usuario acepte o no
        // Por simplicidad, devolvemos true para aceptar siempre
        // Mejor implementar diálogo JavaFX real
        return true;
    }

    /**
     * Este método será llamado por DCCManager periódicamente para actualizar progreso
     * @param progreso valor entre 0 y 1 que indica el progreso de la transferencia
     */
    public void actualizarBarraProgreso(double progreso) {
        Platform.runLater(() -> {
            if (!progressBar.isVisible()) {
                progressBar.setVisible(true);
            }
            progressBar.setProgress(progreso);
            if (progreso >= 1.0) {
                progressBar.setVisible(false);
                progressBar.setProgress(0);
                appendSystemMessage("Transferencia finalizada");
            }
        });
    }

}

