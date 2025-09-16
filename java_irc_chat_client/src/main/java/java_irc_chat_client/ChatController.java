package java_irc_chat_client;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.pircbotx.Configuration;
import org.pircbotx.PircBotX;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.*;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import javax.net.ssl.SSLSocketFactory;

public class ChatController {

    @FXML private AnchorPane rootPane;
    @FXML private TextArea chatArea;
    @FXML private TextField inputField;

    private VBox leftPane;
    private StackPane rightPane;

    private PircBotX bot;

    private static class CanalVentana {
        Stage stage;
        CanalController controller;
        CanalVentana(Stage s, CanalController c) { stage = s; controller = c; }
    }

    private final Map<String, CanalVentana> canalesAbiertos = new HashMap<>();
    private final Map<String, Button> canalButtons = new HashMap<>();
    private String canalActivo = null;

    // Buffers
    private final Map<String, Set<String>> usersBuffer = new HashMap<>();
    private final Map<String, List<String>> messagesBuffer = new HashMap<>();

    public void setLeftPane(VBox leftPane) { this.leftPane = leftPane; }
    public void setRightPane(StackPane rightPane) { this.rightPane = rightPane; }
    public AnchorPane getRootPane() { return rootPane; }

    @FXML
    public void initialize() {
        inputField.setOnAction(e -> sendCommand());
    }

    // Cierra un canal y actualiza UI (ventana y botón)
    public void cerrarCanalDesdeVentana(String canal) {
        CanalVentana ventana = canalesAbiertos.remove(canal);

        if (ventana != null) {
            Platform.runLater(() -> ventana.stage.close());
        }

        Button btn = canalButtons.remove(canal);
        if (btn != null && leftPane != null) {
            Platform.runLater(() -> leftPane.getChildren().remove(btn));
        }

        if (canalActivo != null && canalActivo.equals(canal)) {
            canalActivo = null;
        }
    }
    
    

 // Cierra todos los canales, desconecta del servidor y cierra la aplicación
    public void cerrarTodo(String mensaje) {
        // 1. Cerrar todos los canales abiertos
        for (String canal : new ArrayList<>(canalesAbiertos.keySet())) {
            cerrarCanalDesdeVentana(canal);
        }

        // 2. Desconectar del servidor IRC
        if (bot != null) {
            try {
                bot.sendIRC().quitServer(mensaje != null ? mensaje : "Cerrando cliente");
                bot.stopBotReconnect(); // Detener reconexiones automáticas
                bot.close();            // Cierra hilos internos
            } catch (Exception e) {
                // Ignorar errores de desconexión
            }
        }

        // 3. Cerrar la ventana principal y salir completamente
        Platform.runLater(() -> {
            try {
                Stage primaryStage = (Stage) rootPane.getScene().getWindow();
                if (primaryStage != null) primaryStage.close();
            } finally {
                // Salir completamente de la aplicación
                Platform.exit();
                System.exit(0);
            }
        });
    }


    public void connectToIRC() {
        new Thread(() -> {
            try {
                Configuration configuration = new Configuration.Builder()
                        .setName("akiles5432")
                        .setLogin("akiles5432")
                        .setRealName("JIRCHAT")
                        .addServer("irc.chatzona.org", 6697)
                        .setSocketFactory(SSLSocketFactory.getDefault())
                        .setAutoNickChange(true)
                        .setAutoReconnect(true)
                        .addListener(new ListenerAdapter() {

                            @Override
                            public void onMessage(MessageEvent event) {
                                String canal = event.getChannel() != null ? event.getChannel().getName() : null;
                                if (canal != null && canalesAbiertos.containsKey(canal)) {
                                    CanalVentana v = canalesAbiertos.get(canal);
                                    Platform.runLater(() -> v.controller.appendMessage(event.getUser().getNick(), event.getMessage()));
                                } else if (canal != null) {
                                    messagesBuffer.putIfAbsent(canal, new ArrayList<>());
                                    messagesBuffer.get(canal).add("<" + event.getUser().getNick() + "> " + event.getMessage());
                                } else {
                                    Platform.runLater(() -> chatArea.appendText("<" + event.getUser().getNick() + "> " + event.getMessage() + "\n"));
                                }
                            }

                            @Override
                            public void onConnect(ConnectEvent event) {
                                Platform.runLater(() -> chatArea.appendText("✅ Conectado a irc.chatzona.org\n"));
                            }

                            @Override
                            public void onDisconnect(DisconnectEvent event) {
                                Platform.runLater(() -> chatArea.appendText("❌ Desconectado del servidor\n"));
                            }

                            @Override
                            public void onNotice(NoticeEvent event) {
                                Platform.runLater(() -> chatArea.appendText("[NOTICE] " + event.getMessage() + "\n"));
                            }

                            @Override
                            public void onServerResponse(ServerResponseEvent event) {
                                int code = event.getCode();
                                List<String> parsed = event.getParsedResponse();
                                AtomicReference<String> channelRef = new AtomicReference<>(null);

                                for (String p : parsed) {
                                    if (p.startsWith("#") || p.startsWith("&") || p.startsWith("+") || p.startsWith("!")) {
                                        channelRef.set(p);
                                        break;
                                    }
                                }
                                if (channelRef.get() == null && parsed.size() >= 3) {
                                    channelRef.set(parsed.get(2));
                                }

                                final String channel = channelRef.get();

                                switch (code) {
                                    case 353: // RPL_NAMREPLY
                                        if (channel == null) return;
                                        usersBuffer.putIfAbsent(channel, new HashSet<>());
                                        String nickChunk = parsed.get(parsed.size() - 1);
                                        if (nickChunk.startsWith(":")) nickChunk = nickChunk.substring(1);
                                        String[] nickTokens = nickChunk.split(" +");
                                        for (String n : nickTokens) {
                                            if (!n.isEmpty()) usersBuffer.get(channel).add(n.replaceAll("^[@+~&%]+", ""));
                                        }
                                        break;

                                    case 366: // RPL_ENDOFNAMES
                                        if (channel != null) {
                                            CanalVentana v = canalesAbiertos.get(channel);
                                            if (v != null) {
                                                List<String> userList = new ArrayList<>(usersBuffer.getOrDefault(channel, Collections.emptySet()));
                                                Collections.sort(userList, String.CASE_INSENSITIVE_ORDER);
                                                Platform.runLater(() -> {
                                                    v.controller.updateUsers(userList);
                                                    usersBuffer.remove(channel);
                                                });
                                            }
                                        }
                                        break;
                                }
                            }

                        }).buildConfiguration();

                bot = new PircBotX(configuration);
                bot.startBot();

            } catch (Exception e) {
                Platform.runLater(() -> chatArea.appendText("Error al conectar: " + e.getMessage() + "\n"));
            }
        }).start();
    }

    private void sendCommand() {
        String text = inputField.getText().trim();
        if (text.isEmpty()) return;
        boolean isCommand = text.startsWith("/");

        if (isCommand) {
            String cmd = text.substring(1).trim();
            try {
                if (cmd.startsWith("join ")) {
                    String canal = cmd.substring(5).trim();
                    abrirCanal(canal);
                } else if (cmd.startsWith("part")) {
                    String[] parts = cmd.split(" ", 3);
                    String canal = parts.length >= 2 ? parts[1] : canalActivo;
                    String message = parts.length == 3 ? parts[2] : "";
                    cerrarCanalDesdeVentana(canal);
                    if (!message.isEmpty()) bot.sendRaw().rawLine("PART " + canal + " :" + message);
                    else bot.sendRaw().rawLine("PART " + canal);
                } else if (cmd.startsWith("quit")) {
                    cerrarTodo("Cerrando cliente");
                } else if (cmd.startsWith("nick ")) {
                    bot.sendIRC().changeNick(cmd.substring(5).trim());
                } else {
                    bot.sendRaw().rawLine(cmd);
                }
            } catch (Exception e) {
                chatArea.appendText("⚠ Error al ejecutar comando: " + e.getMessage() + "\n");
            }
        } else {
            if (canalActivo != null && canalesAbiertos.containsKey(canalActivo)) {
                CanalVentana ventana = canalesAbiertos.get(canalActivo);
                bot.sendIRC().message(canalActivo, text);
                ventana.controller.appendMessage("Yo", text);
            } else {
                bot.sendIRC().message("#mas_de_40", text);
                chatArea.appendText("<Yo> " + text + "\n");
            }
        }
        inputField.clear();
    }

    private void abrirCanal(String canal) throws IOException {
        if (canalesAbiertos.containsKey(canal)) {
            CanalVentana v = canalesAbiertos.get(canal);
            v.stage.toFront();
            v.stage.requestFocus();
            canalActivo = canal;
            return;
        }

        FXMLLoader loader = new FXMLLoader(getClass().getResource("JIRCHAT_CANAL.fxml"));
        CanalController canalController = new CanalController();
        loader.setController(canalController);
        AnchorPane canalPane = loader.load();

        canalController.setBot(bot);
        canalController.setCanal(canal);
        canalController.setMainController(this);

        Stage canalStage = new Stage();
        canalStage.setTitle("Canal " + canal);
        canalStage.setScene(new Scene(canalPane));
        canalStage.setWidth(rightPane != null ? rightPane.getWidth() : 600);
        canalStage.setHeight(rightPane != null ? rightPane.getHeight() : 400);

        CanalVentana ventana = new CanalVentana(canalStage, canalController);
        canalesAbiertos.put(canal, ventana);

        Button canalBtn = new Button(canal);
        canalBtn.setMaxWidth(Double.MAX_VALUE);
        canalBtn.setOnAction(e -> {
            ventana.stage.toFront();
            ventana.stage.requestFocus();
            canalActivo = canal;
        });
        if (leftPane != null) leftPane.getChildren().add(canalBtn);
        canalButtons.put(canal, canalBtn);

        canalStage.setOnCloseRequest(ev -> cerrarCanalDesdeVentana(canal));

        canalActivo = canal;
        canalStage.show();

        bot.sendIRC().joinChannel(canal);
        bot.sendRaw().rawLineNow("NAMES " + canal);

        if (messagesBuffer.containsKey(canal)) {
            List<String> buffer = messagesBuffer.get(canal);
            buffer.forEach(m -> canalController.appendMessage("Servidor", m));
            messagesBuffer.remove(canal);
        }

        if (usersBuffer.containsKey(canal)) {
            List<String> userList = new ArrayList<>(usersBuffer.get(canal));
            canalController.updateUsers(userList);
        }

        chatArea.appendText("➡ uniéndote a " + canal + "\n");
    }
}




