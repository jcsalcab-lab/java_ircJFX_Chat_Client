package java_irc_chat_client;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.pircbotx.*;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.ConnectEvent;
import org.pircbotx.hooks.events.MessageEvent;
import org.pircbotx.hooks.events.NoticeEvent;
import org.pircbotx.hooks.events.UserListEvent;

import javax.net.ssl.SSLSocketFactory;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class ChatController {

    // ------------------ FXML principal ------------------
    @FXML private AnchorPane rootPane;
    @FXML private TextArea chatArea;    
    @FXML private TextField inputField; 

    private PircBotX bot;

    // ------------------ Canales ------------------
    private VBox leftPane;
    private StackPane rightPane;
    private String canalActivo = null;

    private static class CanalVentana {
        Stage stage;
        CanalController controller;
        AnchorPane rootPane;
        CanalVentana(Stage s, CanalController c, AnchorPane p) { stage = s; controller = c; rootPane = p; }
    }

    private final Map<String, CanalVentana> canalesAbiertos = new HashMap<>();
    private final Map<String, Button> canalButtons = new HashMap<>();
    private final List<Stage> floatingWindows = new ArrayList<>();
    private AnchorPane currentFrontPane;

    private final Map<String, Set<String>> usersBuffer = new HashMap<>();
    private final Map<String, List<String>> messagesBuffer = new HashMap<>();

    // ------------------ Inicialización ------------------
    @FXML
    public void initialize() {
        inputField.setOnAction(e -> sendCommand(inputField));
        inputField.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == KeyCode.TAB) event.consume(); // Autocompletado opcional
        });
    }

    public void setBot(PircBotX bot) { this.bot = bot; }
    public void setLeftPane(VBox leftPane) { this.leftPane = leftPane; }
    public void setRightPane(StackPane rightPane) { this.rightPane = rightPane; this.currentFrontPane = rootPane; }

    // ------------------ Envío de comandos ------------------
    private void sendCommand(TextField field) {
        String text = field.getText().trim();
        if (text.isEmpty()) return;
        boolean isCommand = text.startsWith("/");

        try {
            if (isCommand) {
                String cmd = text.substring(1).trim();
                if (cmd.startsWith("join ")) abrirCanal(cmd.substring(5).trim());
                else if (cmd.startsWith("quit")) cerrarTodo("Cerrando cliente");
                else if (bot != null) bot.sendRaw().rawLine(cmd);
                else appendSystemMessage("⚠ No conectado aún.");
            } else {
                appendMessage("Yo", text);
            }
        } catch(Exception e) {
            appendSystemMessage("⚠ Error: " + e.getMessage());
        }
        field.clear();
    }

    // ------------------ Mensajes ------------------
    public void appendMessage(String usuario, String mensaje) {
        Platform.runLater(() -> chatArea.appendText("<" + usuario + "> " + mensaje + "\n"));
    }

    public void appendSystemMessage(String mensaje) {
        Platform.runLater(() -> chatArea.appendText("[Servidor] " + mensaje + "\n"));
    }

    // ------------------ Gestión de canales ------------------
    private void abrirCanal(String canal) throws IOException {
        if (canalesAbiertos.containsKey(canal)) {
            CanalVentana v = canalesAbiertos.get(canal);
            v.stage.toFront();
            canalActivo = canal;
            return;
        }

        FXMLLoader loader = new FXMLLoader(getClass().getResource("JIRCHAT_CANAL.fxml"));
        AnchorPane canalPane = loader.load();
        CanalController canalController = loader.getController();
        canalController.setBot(bot);
        canalController.setCanal(canal);
        canalController.setMainController(this);

        Stage canalStage = new Stage();
        canalStage.setTitle("Canal " + canal);
        canalStage.setScene(new Scene(canalPane));

        if (rightPane != null && rightPane.getScene() != null) {
            javafx.geometry.Point2D pos = rightPane.localToScreen(0, 0);
            if (pos != null) { canalStage.setX(pos.getX()); canalStage.setY(pos.getY()); }
            canalStage.setWidth(rightPane.getWidth()); canalStage.setHeight(rightPane.getHeight());
        }

        floatingWindows.add(canalStage);
        CanalVentana ventana = new CanalVentana(canalStage, canalController, canalPane);
        canalesAbiertos.put(canal, ventana);

        Button canalBtn = new Button(canal);
        canalBtn.setMaxWidth(Double.MAX_VALUE);
        canalBtn.setOnAction(e -> { canalStage.toFront(); canalActivo = canal; });
        if (leftPane != null) leftPane.getChildren().add(canalBtn);
        canalButtons.put(canal, canalBtn);

        canalStage.setOnCloseRequest(ev -> cerrarCanalDesdeVentana(canal));
        canalActivo = canal;
        canalStage.show();

        if (bot != null) {
            bot.sendIRC().joinChannel(canal);
            bot.sendRaw().rawLineNow("NAMES " + canal);
        }

        if (messagesBuffer.containsKey(canal)) {
            List<String> buffer = messagesBuffer.get(canal);
            buffer.forEach(m -> canalController.appendMessage("Servidor", m));
            messagesBuffer.remove(canal);
        }

        if (usersBuffer.containsKey(canal)) {
            List<String> ulist = new ArrayList<>(usersBuffer.get(canal));
            Collections.sort(ulist, String.CASE_INSENSITIVE_ORDER);
            canalController.updateUsers(ulist);
        }

        appendSystemMessage("➡ Uniéndote a " + canal);
    }

    public void cerrarCanalDesdeVentana(String canal) {
        CanalVentana ventana = canalesAbiertos.remove(canal);
        if (ventana != null) {
            Platform.runLater(() -> ventana.stage.close());
            Button btn = canalButtons.remove(canal);
            if (btn != null && leftPane != null) Platform.runLater(() -> leftPane.getChildren().remove(btn));
            floatingWindows.remove(ventana.stage);
            if (canalActivo != null && canalActivo.equals(canal)) canalActivo = null;
            if (currentFrontPane == ventana.rootPane) currentFrontPane = rootPane;
        }
    }

    // ------------------ Conexión al servidor IRC ------------------
    public void connectToIRC() {
        new Thread(() -> {
            try {
                Configuration config = new Configuration.Builder()
                        .setName("akiles54321")
                        .setLogin("akiles54321")
                        .setRealName("JIRCHAT")
                        .addServer("irc.chatzona.org", 6697)
                        .setSocketFactory(SSLSocketFactory.getDefault())
                        .setAutoNickChange(true)
                        .setAutoReconnect(true)
                        .addListener(new ListenerAdapter() {
                            private boolean identificado = false;

                            @Override
                            public void onConnect(ConnectEvent event) {
                                appendSystemMessage("✅ Conectado a irc.chatzona.org");
                                if(bot != null) bot.sendIRC().message("NickServ", "IDENTIFY <tu_password>");
                            }

                            @Override
                            public void onNotice(NoticeEvent event) {
                                String from = event.getUser() != null ? event.getUser().getNick() : "Servidor";
                                String mensaje = event.getMessage();
                                appendSystemMessage("[" + from + "] " + mensaje);

                                if(!identificado && mensaje.toLowerCase().contains("identified")) {
                                    identificado = true;
                                    appendSystemMessage("✅ Identificación exitosa. Ahora puedes unirte a canales.");
                                }
                            }

                            @Override
                            public void onMessage(MessageEvent event) {
                                String canal = event.getChannel() != null ? event.getChannel().getName() : null;
                                String usuario = event.getUser().getNick();
                                String mensaje = event.getMessage();

                                if(canal != null && canalesAbiertos.containsKey(canal)) {
                                    CanalVentana v = canalesAbiertos.get(canal);
                                    Platform.runLater(() -> v.controller.appendMessage(usuario, mensaje));
                                } else if(canal != null) {
                                    messagesBuffer.putIfAbsent(canal, new ArrayList<>());
                                    messagesBuffer.get(canal).add("<"+usuario+"> "+mensaje);
                                } else {
                                    appendMessage(usuario, mensaje);
                                }
                            }

                            @Override
                            public void onUserList(UserListEvent event) {
                                String canal = event.getChannel() != null ? event.getChannel().getName() : null;
                                if (canal == null) return;

                                Set<User> userObjs = event.getUsers();
                                List<String> nicks = userObjs.stream().map(User::getNick)
                                        .filter(Objects::nonNull).map(String::trim)
                                        .filter(s -> !s.isEmpty())
                                        .sorted(String.CASE_INSENSITIVE_ORDER)
                                        .collect(Collectors.toList());

                                usersBuffer.put(canal, new HashSet<>(nicks));
                                if(canalesAbiertos.containsKey(canal)) {
                                    CanalVentana v = canalesAbiertos.get(canal);
                                    Platform.runLater(() -> v.controller.updateUsers(nicks));
                                }

                                appendSystemMessage("NAMES ("+canal+"): "+nicks);
                            }

                        }).buildConfiguration();

                bot = new PircBotX(config);
                bot.startBot();
            } catch(Exception e) { appendSystemMessage("Error al conectar: "+e.getMessage()); }
        }).start();
    }

 // ------------------ Getter para rootPane ------------------
    public AnchorPane getRootPane() {
        return rootPane;
    }

    // ------------------ Método para vincular floating windows ------------------
    public void bindFloatingWindowsToRightPane(Stage primaryStage) {
        // Esto asegura que las ventanas flotantes tengan referencia al primaryStage
        for (CanalVentana ventana : canalesAbiertos.values()) {
            ventana.stage.initOwner(primaryStage);
        }
    }

    // ------------------ Cerrar todo ------------------
    public void cerrarTodo(String mensaje){
        for(String canal : new ArrayList<>(canalesAbiertos.keySet())) cerrarCanalDesdeVentana(canal);
        if(bot != null){
            try{
                bot.sendIRC().quitServer(mensaje != null ? mensaje : "Cerrando cliente");
                bot.stopBotReconnect();
                bot.close();
            } catch(Exception ignored){}
        }
        Platform.runLater(() -> {
            try{
                Stage primaryStage = (Stage)rootPane.getScene().getWindow();
                if(primaryStage != null) primaryStage.close();
            }finally{ Platform.exit(); System.exit(0);}
        });
    }
}
