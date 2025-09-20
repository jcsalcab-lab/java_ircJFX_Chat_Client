package java_irc_chat_client;

import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.util.Duration;
import org.pircbotx.*;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.*;
import org.pircbotx.hooks.types.GenericMessageEvent;
import javax.net.ssl.SSLSocketFactory;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class ChatController {

    @FXML private TextField inputField;
    @FXML private ListView<String> userListView_canal;
    @FXML private ScrollPane chatScroll;
    @FXML private TextArea  chatFlow;
    
    @FXML private TextArea chatArea;


    private StackPane rightPane;
    private VBox leftPane;
    private AnchorPane rootPane;

    private PircBotX bot;
    private String canalActivo = null;

    private final Map<String, CanalVentana> canalesAbiertos = new HashMap<>();
    private final Map<String, Button> canalButtons = new HashMap<>();
    private final List<Stage> floatingWindows = new ArrayList<>();

    private final Map<String, Stage> privateChats = new HashMap<>();
    private final Map<String, PrivadoController> privateChatsController = new HashMap<>();
    private final Map<String, Button> privadoButtons = new HashMap<>();

    private final PauseTransition resizePause = new PauseTransition(Duration.millis(250));
    private Stage lastFocusedWindow = null;

    private String password;

    // Solicitudes de chat pendientes o aceptadas
    private final Map<String, Boolean> solicitudPendiente = new HashMap<>();

    private static class CanalVentana {
        Stage stage;
        CanalController controller;
        AnchorPane rootPane;
        CanalVentana(Stage s, CanalController c, AnchorPane p) { stage = s; controller = c; rootPane = p; }
    }

    public void setPassword(String password) { this.password = password; }
    public void setLeftPane(VBox leftPane) { this.leftPane = leftPane; }
    public void setRightPane(StackPane rightPane) { this.rightPane = rightPane; attachRightPaneSizeListeners(); }
    public void setRootPane(AnchorPane rootPane) { this.rootPane = rootPane; }
    public void setBot(PircBotX bot) { this.bot = bot; }

    @FXML
    public void initialize() {
        if (inputField != null) {
            inputField.setDisable(true);
            inputField.setOnAction(e -> sendCommand());
            inputField.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
                if (event.getCode() == KeyCode.TAB) event.consume();
            });
        }

        resizePause.setOnFinished(ev -> {
            if (lastFocusedWindow != null) Platform.runLater(() -> lastFocusedWindow.toFront());
        });
    }

    // ------------------ COMANDOS ------------------
    private void sendCommand() {
        String text = inputField.getText().trim();
        if (text.isEmpty() || bot == null) return;

        try {
            if (text.startsWith("/")) handleCommand(text.substring(1).trim());
            else {
                if (canalActivo != null && canalesAbiertos.containsKey(canalActivo)) {
                    CanalVentana ventana = canalesAbiertos.get(canalActivo);
                    ventana.controller.sendMessageToChannel(text);
                } else {
                    bot.sendIRC().message("NickServ", text);
                    appendSystemMessage("[Yo -> NickServ] " + text);
                }
            }
        } finally {
            inputField.clear();
        }
    }

    
    public Pane getRootPane() {
        return rootPane;
    }

    
    private void handleCommand(String cmd) {
        try {
            if (cmd.startsWith("join ")) abrirCanal(cmd.substring(5).trim());
            else if (cmd.startsWith("quit")) cerrarTodo("Cerrando cliente");
            else if (bot != null) bot.sendRaw().rawLine(cmd);
        } catch (Exception e) { appendSystemMessage("⚠ Error al ejecutar comando: " + e.getMessage()); }
    }

    // ------------------ PRIVADO ------------------
    public void abrirChatPrivado(String nick) {
        if (privateChats.containsKey(nick) || Boolean.TRUE.equals(solicitudPendiente.get(nick))) {
            return;
        }
        mostrarSolicitudPrivado(nick, "");
    }

    public void abrirChatPrivadoConMensaje(String nick, String mensaje) {
        if (privateChats.containsKey(nick)) {
            privateChats.get(nick).toFront();
            appendPrivateMessage(nick, mensaje, false);
            return;
        }
        mostrarSolicitudPrivado(nick, mensaje);
    }

    private void mostrarSolicitudPrivado(String nick, String mensaje) {
        if (Boolean.TRUE.equals(solicitudPendiente.get(nick))) {
            if (!mensaje.isEmpty()) appendPrivateMessage(nick, mensaje, false);
            return;
        }

        solicitudPendiente.put(nick, false);

        Stage solicitudStage = new Stage();
        solicitudStage.setTitle("Solicitud de chat privado de " + nick);

        VBox vbox = new VBox(10);
        vbox.setStyle("-fx-padding: 10;");
        javafx.scene.control.Label label = new javafx.scene.control.Label(
                nick + " quiere chatear contigo.\nMensaje: " + mensaje
        );
        javafx.scene.control.Button aceptarBtn = new javafx.scene.control.Button("Aceptar");
        javafx.scene.control.Button rechazarBtn = new javafx.scene.control.Button("Rechazar");

        vbox.getChildren().addAll(label, aceptarBtn, rechazarBtn);
        solicitudStage.setScene(new Scene(vbox));

        Window owner = (rootPane != null && rootPane.getScene() != null) ? rootPane.getScene().getWindow() : null;
        if (owner != null) solicitudStage.initOwner(owner);

        solicitudStage.show();

        aceptarBtn.setOnAction(e -> {
            solicitudStage.close();
            abrirPrivado(nick);
            solicitudPendiente.put(nick, true);
            if (!mensaje.isEmpty()) appendPrivateMessage(nick, mensaje, false);
        });

        rechazarBtn.setOnAction(e -> {
            solicitudStage.close();
            solicitudPendiente.remove(nick);
            if (bot != null) bot.sendIRC().message(nick, "⚠ Su solicitud de chat privado ha sido denegada.");
        });
    }

    public void abrirPrivado(String nick) {
        try {
            if (privateChats.containsKey(nick)) {
                privateChats.get(nick).toFront();
                return;
            }

            FXMLLoader loader = new FXMLLoader(getClass().getResource("JIRCHAT_PRIVADO.fxml"));
            Parent root = loader.load();
            PrivadoController controller = loader.getController();
            controller.setBot(bot);
            controller.setDestinatario(nick);
            controller.setMainController(this);

            Stage stage = new Stage();
            stage.setTitle("Privado con " + nick);
            stage.setScene(new Scene(root));

            privateChats.put(nick, stage);
            privateChatsController.put(nick, controller);

            Button btn = new Button("@" + nick);
            btn.setMaxWidth(Double.MAX_VALUE);
            btn.setOnAction(e -> stage.toFront());
            privadoButtons.put(nick, btn);

            if (leftPane != null) leftPane.getChildren().add(btn);

            registerFloatingWindow(stage, () -> cerrarPrivado(nick));
            stage.setOnCloseRequest(ev -> cerrarPrivado(nick));

            stage.show();
        } catch (Exception e) {
            appendSystemMessage("⚠ Error al abrir privado con " + nick + ": " + e.getMessage());
        }
    }

    public void cerrarPrivado(String nick) {
        Stage stage = privateChats.remove(nick);
        if (stage != null) stage.close();

        privateChatsController.remove(nick);
        Button btn = privadoButtons.remove(nick);
        if (btn != null && leftPane != null) leftPane.getChildren().remove(btn);
        solicitudPendiente.remove(nick);
    }

    public void appendPrivateMessage(String nick, String mensaje, boolean esMio) {
        PrivadoController controller = privateChatsController.get(nick);
        if (controller != null) controller.appendMessage(esMio ? "Yo" : nick, mensaje);
    }

    // ------------------ MENSAJES CON COLORES ------------------
 // Mensaje normal de usuario
    public void appendMessage(String usuario, String mensaje) {
        Platform.runLater(() -> {
            if (chatArea != null) {
                chatArea.appendText("<" + usuario + "> " + mensaje + "\n");
                chatArea.positionCaret(chatArea.getLength()); // auto-scroll
            }
        });
    }

    // Mensaje del sistema
    public void appendSystemMessage(String mensaje) {
        Platform.runLater(() -> {
            if (chatArea != null) {
                chatArea.appendText("[Sistema] " + mensaje + "\n");
                chatArea.positionCaret(chatArea.getLength()); // auto-scroll
            }
        });
    }

    // Mensaje privado
    public void appendPrivateMessage(String usuario, String mensaje) {
        Platform.runLater(() -> {
            if (chatArea != null) {
                chatArea.appendText("<" + usuario + "> " + mensaje + "\n");
                chatArea.positionCaret(chatArea.getLength()); // auto-scroll
            }
        });
    }


    private String parseIRCMessage(String mensaje) {
        // Eliminamos los códigos de IRC de color y formato
        StringBuilder plainText = new StringBuilder();
        int i = 0;

        while (i < mensaje.length()) {
            char c = mensaje.charAt(i);
            if (c == '\u0003' || c == '\u000F' || c == '\u0002') {
                // Saltamos códigos de color, reset y negrita
                i++;
                // Para código de color, puede haber números después
                if (c == '\u0003') {
                    while (i < mensaje.length() && Character.isDigit(mensaje.charAt(i))) i++;
                }
            } else {
                plainText.append(c);
                i++;
            }
        }

        return plainText.toString();
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

    private void scrollToBottom(ScrollPane sp) { Platform.runLater(() -> sp.setVvalue(1.0)); }

    
    public void connectToIRC() {
        new Thread(() -> {
            try {
                Configuration config = new Configuration.Builder()
                        .setName("akkiles54321") // tu nick
                        .setLogin("akkiles54321")
                        .setRealName("JIRCHAT")
                        .addServer("irc.chatzona.org", 6697)
                        .setSocketFactory(SSLSocketFactory.getDefault())
                        .setAutoNickChange(true)
                        .setAutoReconnect(true)
                        .addListener(new ListenerAdapter() {

                            @Override
                            public void onGenericMessage(GenericMessageEvent event) {
                                appendSystemMessage("[Servidor] " + event.getMessage());
                            }

                            @Override
                            public void onConnect(ConnectEvent event) {
                                Platform.runLater(() -> inputField.setDisable(false));
                                appendSystemMessage("✅ Conectado a irc.chatzona.org");

                                // IDENTIFY a NickServ si hay password
                                if (password != null && !password.isEmpty())
                                    bot.sendIRC().message("NickServ", "IDENTIFY " + password);

                                // Validación anti-bot: JOIN/PART temporal
                                new Thread(() -> {
                                    try {
                                        String validationChannel = "#validationTrigger";
                                        bot.sendRaw().rawLineNow("JOIN " + validationChannel);
                                        bot.sendRaw().rawLineNow("PART " + validationChannel);
                                    } catch (Exception e) {
                                        appendSystemMessage("⚠ Error en validación anti-bot: " + e.getMessage());
                                    }
                                }).start();
                            }

                            @Override
                            public void onMessage(MessageEvent event) {
                                CanalVentana ventana = canalesAbiertos.get(event.getChannel().getName());
                                if (ventana != null)
                                    ventana.controller.appendMessage(event.getUser().getNick(), event.getMessage());
                            }

                            @Override
                            public void onPrivateMessage(PrivateMessageEvent event) {
                                String nick = event.getUser().getNick();
                                String mensaje = event.getMessage();
                                Platform.runLater(() -> mostrarSolicitudPrivado(nick, mensaje));
                            }

                            @Override
                            public void onUserList(UserListEvent event) {
                                actualizarUsuariosCanal(event.getChannel().getName(),
                                        reconstruirListaConPrefijos(event.getChannel()));
                            }

                            @Override
                            public void onJoin(JoinEvent event) {
                                actualizarUsuariosCanal(event.getChannel().getName(),
                                        reconstruirListaConPrefijos(event.getChannel()));
                                CanalVentana ventana = canalesAbiertos.get(event.getChannel().getName());
                                if (ventana != null)
                                    ventana.controller.appendSystemMessage(
                                            event.getUser().getNick() + " ha entrado al canal",
                                            CanalController.MessageType.JOIN,
                                            event.getUser().getNick()
                                    );
                            }

                            @Override
                            public void onPart(PartEvent event) {
                                String canal = event.getChannel().getName();
                                actualizarUsuariosCanal(canal, reconstruirListaConPrefijos(event.getChannel()));
                                CanalVentana ventana = canalesAbiertos.get(canal);
                                if (ventana != null) {
                                    ventana.controller.appendSystemMessage(
                                            event.getUser().getNick() + " ha salido del canal",
                                            CanalController.MessageType.PART,
                                            event.getUser().getNick()
                                    );
                                    ventana.controller.showExitPopup(event.getUser().getNick(), canal);
                                }
                            }

                            @Override
                            public void onQuit(QuitEvent event) {
                                String usuario = event.getUser().getNick();
                                for (Map.Entry<String, CanalVentana> entry : canalesAbiertos.entrySet()) {
                                    Channel canal = entry.getValue().controller.getCanalChannel();
                                    if (canal != null && canal.getUsers().contains(event.getUser())) {
                                        entry.getValue().controller.updateUsers(reconstruirListaConPrefijos(canal));
                                        entry.getValue().controller.appendSystemMessage(
                                                usuario + " ha salido del canal",
                                                CanalController.MessageType.PART,
                                                usuario
                                        );
                                        entry.getValue().controller.showExitPopup(usuario, entry.getKey());
                                    }
                                }
                            }

                            private List<String> reconstruirListaConPrefijos(Channel channel) {
                                return channel.getUsers().stream().map(u -> {
                                    String prefix = "";
                                    if (channel.getOps().contains(u)) prefix = "@";
                                    else if (channel.getVoices().contains(u)) prefix = "+";
                                    return prefix + u.getNick();
                                }).collect(Collectors.toList());
                            }

                        }).buildConfiguration();

                bot = new PircBotX(config);
                bot.startBot();
            } catch (Exception e) {
                appendSystemMessage("⚠ Error al conectar: " + e.getMessage());
            }
        }).start();
    }

    
    // ------------------ CANALES ------------------
    private void abrirCanal(String canal) throws IOException {
        if (canalesAbiertos.containsKey(canal)) {
            canalesAbiertos.get(canal).stage.toFront();
            canalActivo = canal;
            return;
        }

        FXMLLoader loader = new FXMLLoader(getClass().getResource("JIRCHAT_CANAL.fxml"));
        Parent rootStack = loader.load();
        CanalController canalController = loader.getController();
        canalController.setBot(bot);
        canalController.setCanal(canal);
        canalController.setMainController(this);

        AnchorPane canalPane = (AnchorPane) rootStack.lookup("#rootPane");

        Stage canalStage = new Stage();
        canalStage.setTitle("Canal " + canal);
        canalStage.setScene(new Scene(rootStack));

        CanalVentana ventana = new CanalVentana(canalStage, canalController, canalPane);
        canalesAbiertos.put(canal, ventana);

        Button canalBtn = new Button(canal);
        canalBtn.setMaxWidth(Double.MAX_VALUE);
        canalBtn.setOnAction(e -> { canalStage.toFront(); canalActivo = canal; });
        if (leftPane != null) Platform.runLater(() -> leftPane.getChildren().add(canalBtn));
        canalButtons.put(canal, canalBtn);

        registerFloatingWindow(canalStage, () -> cerrarCanalDesdeVentana(canal));
        canalStage.setOnCloseRequest(ev -> cerrarCanalDesdeVentana(canal));

        canalActivo = canal;
        canalStage.show();

        if (bot != null) {
            bot.sendIRC().joinChannel(canal);
            bot.sendRaw().rawLineNow("NAMES " + canal);
        }
    }

    public void cerrarCanalDesdeVentana(String canal) {
        CanalVentana ventana = canalesAbiertos.remove(canal);
        if (ventana != null) Platform.runLater(() -> ventana.stage.close());
        Button btn = canalButtons.remove(canal);
        if (btn != null && leftPane != null) Platform.runLater(() -> leftPane.getChildren().remove(btn));
        floatingWindows.remove(ventana.stage);
    }

    public void actualizarUsuariosCanal(String canal, List<String> usuarios) {
        CanalVentana ventana = canalesAbiertos.get(canal);
        if (ventana != null) ventana.controller.updateUsers(usuarios);
    }

    // ------------------ Ventanas flotantes ------------------
    private void attachRightPaneSizeListeners() {
        if (rightPane == null) return;
        rightPane.widthProperty().addListener((obs, oldV, newV) -> repositionFloatingWindows());
        rightPane.heightProperty().addListener((obs, oldV, newV) -> repositionFloatingWindows());
    }

    private void registerFloatingWindow(Stage stage, Runnable onCloseCleanup) {
        if (stage == null) return;
        if (!floatingWindows.contains(stage)) floatingWindows.add(stage);

        stage.focusedProperty().addListener((obs, was, now) -> { if (now) lastFocusedWindow = stage; });
        stage.setOnCloseRequest(ev -> { floatingWindows.remove(stage); try { onCloseCleanup.run(); } catch (Exception ignored) {} });

        repositionFloatingWindows();
    }

    private void repositionFloatingWindows() {
        if (rightPane == null) return;
        Window mainWin = rightPane.getScene().getWindow();
        if (mainWin == null) return;

        double rightX = mainWin.getX() + rightPane.localToScene(0,0).getX() + rightPane.getScene().getX();
        double rightY = mainWin.getY() + rightPane.localToScene(0,0).getY() + rightPane.getScene().getY();
        double width = rightPane.getWidth();
        double height = rightPane.getHeight();

        for (Stage stage : floatingWindows) {
            stage.setX(rightX);
            stage.setY(rightY);
            stage.setWidth(width);
            stage.setHeight(height);
        }
    }

    public void bindFloatingWindowsToRightPane(Stage primaryStage) {
        if (primaryStage == null) return;

        primaryStage.setOnCloseRequest(event -> {
            for (Stage s : new ArrayList<>(floatingWindows)) Platform.runLater(s::close);
            for (Stage s : privateChats.values()) Platform.runLater(s::close);
        });

        primaryStage.widthProperty().addListener((obs, oldV, newV) -> scheduleResize());
        primaryStage.heightProperty().addListener((obs, oldV, newV) -> scheduleResize());
    }

    public void scheduleResize() {
        resizePause.playFromStart();
        repositionFloatingWindows();
    }

    // ------------------ CERRAR TODO ------------------
    public void cerrarTodo(String mensaje) {
        for (String canal : new ArrayList<>(canalesAbiertos.keySet()))
            cerrarCanalDesdeVentana(canal);

        for (String nick : new ArrayList<>(privateChats.keySet()))
            cerrarPrivado(nick);

        if (bot != null) {
            try {
                if (bot.isConnected())
                    bot.sendIRC().quitServer(mensaje != null ? mensaje : "Cerrando cliente");
                bot.stopBotReconnect();
            } catch (Exception ignored) {}
        }

        Platform.exit();
    }
}
