package java_irc_chat_client;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.pircbotx.*;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.*;

import javax.net.ssl.SSLSocketFactory;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class ChatController {

    @FXML
    private TextField inputField;
    @FXML
    private ListView<String> userListView_canal;
    @FXML
    private TextArea chatArea;

    private PircBotX bot;
    private String canalActivo = null;

    private StackPane rightPane;
    private VBox leftPane;
    private AnchorPane rootPane;

    // Ventanas privadas y sus controladores
    private final Map<String, Stage> privateChats = new HashMap<>();
    private final Map<String, PrivadoController> privateChatsController = new HashMap<>();
    private final Map<String, Button> privadoButtons = new HashMap<>();

    // Ventanas de canales
    private final Map<String, CanalVentana> canalesAbiertos = new HashMap<>();
    private final Map<String, Button> canalButtons = new HashMap<>();
    private final List<Stage> floatingWindows = new ArrayList<>();

    private CanalController controller; // tipo correcto

    private static class CanalVentana {
        Stage stage;
        CanalController controller;
        AnchorPane rootPane;

        CanalVentana(Stage s, CanalController c, AnchorPane p) {
            stage = s;
            controller = c;
            rootPane = p;
        }
    }

    // ------------------ Setters ------------------
    public void setLeftPane(VBox leftPane) {
        this.leftPane = leftPane;
    }

    public void setRightPane(StackPane rightPane) {
        this.rightPane = rightPane;
    }

    public void setRootPane(AnchorPane rootPane) {
        this.rootPane = rootPane;
    }

    public void setBot(PircBotX bot) {
        this.bot = bot;
    }

    // ------------------ Inicialización ------------------
    @FXML
    public void initialize() {
        inputField.setOnAction(e -> sendCommand());
        inputField.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == KeyCode.TAB) {
                event.consume();
                handleTabCompletion(event.isShiftDown());
            }
        });

        if (userListView_canal != null) {
            userListView_canal.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2) {
                    String selectedUser = userListView_canal.getSelectionModel().getSelectedItem();
                    if (selectedUser != null && bot != null && !selectedUser.equals(bot.getNick())) {
                        abrirChatPrivado(selectedUser);
                    }
                }
            });
        }
    }

    public AnchorPane getRootPane() {
        return rootPane;
    }

    private void handleTabCompletion(boolean shiftDown) {
        // Implementar autocompletado si quieres
    }

    // ------------------ Enviar comandos ------------------
    private void sendCommand() {
        String text = inputField.getText().trim();
        if (text.isEmpty() || bot == null)
            return;

        try {
            if (text.startsWith("/"))
                handleCommand(text.substring(1).trim());
            else {
                if (canalActivo != null && canalesAbiertos.containsKey(canalActivo)) {
                    CanalVentana ventana = canalesAbiertos.get(canalActivo);
                    ventana.controller.sendMessageToChannel(text);
                } else {
                    if (bot != null)
                        bot.sendIRC().message("#mas_de_40", text);
                    Platform.runLater(() -> chatArea.appendText("<Yo> " + text + "\n"));
                }
            }
        } finally {
            inputField.clear();
        }
    }

    private void handleCommand(String cmd) {
        try {
            if (cmd.startsWith("join ")) {
                abrirCanal(cmd.substring(5).trim());
            } else if (cmd.startsWith("quit")) {
                cerrarTodo("Cerrando cliente");
            } else if (cmd.startsWith("msg ")) {
                String[] parts = cmd.split(" ", 3);
                if (parts.length >= 3 && bot != null)
                    bot.sendIRC().message(parts[1], parts[2]);
            } else if (cmd.startsWith("me ")) {
                if (bot != null && canalActivo != null) {
                    String mensaje = cmd.substring(3).trim();
                    bot.sendIRC().action(canalActivo, mensaje);
                    appendMessage("Yo", "* " + mensaje);
                }
            } else if (bot != null)
                bot.sendRaw().rawLine(cmd);
            else
                appendSystemMessage("⚠ No conectado aún.");
        } catch (Exception e) {
            appendSystemMessage("⚠ Error al ejecutar comando: " + e.getMessage());
        }
    }

    // ------------------ Mensajes ------------------
    public void appendMessage(String usuario, String mensaje) {
        Platform.runLater(() -> chatArea.appendText("<" + usuario + "> " + mensaje + "\n"));
    }

    public void appendSystemMessage(String mensaje) {
        Platform.runLater(() -> chatArea.appendText("[Servidor] " + mensaje + "\n"));
    }

    // ------------------ Chat privado ------------------
    public void abrirChatPrivado(String nick) {
        if (nick == null || nick.trim().isEmpty())
            return;
        String key = nick.toLowerCase();

        if (privateChats.containsKey(key)) {
            Platform.runLater(() -> privateChats.get(key).toFront());
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("JIRCHAT_PRIVADO.fxml"));
            BorderPane root = loader.load();
            PrivadoController controller = loader.getController();
            controller.setBot(bot);
            controller.setDestinatario(nick);
            controller.setMainController(this);
            controller.initializeChat();

            Stage stage = new Stage();
            stage.setTitle("Chat privado con " + nick);
            stage.setScene(new Scene(root));
            stage.setUserData(controller);
            privateChats.put(key, stage);
            privateChatsController.put(key, controller);

            // --- Crear botón en el panel izquierdo ---
            Button privadoBtn = new Button(nick);
            privadoBtn.setMaxWidth(Double.MAX_VALUE);
            privadoBtn.setOnAction(e -> {
                Stage s = privateChats.get(key);
                if (s != null)
                    s.toFront();
            });
            if (leftPane != null)
                leftPane.getChildren().add(privadoBtn);
            privadoButtons.put(key, privadoBtn);

            // --- Al cerrar la ventana, limpiar mapas y quitar botón ---
            stage.setOnCloseRequest(e -> {
                privateChats.remove(key);
                privateChatsController.remove(key);
                Button btn = privadoButtons.remove(key);
                if (btn != null && leftPane != null) {
                    Platform.runLater(() -> leftPane.getChildren().remove(btn));
                }
            });

            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
            appendSystemMessage("No se pudo abrir chat privado con " + nick);
        }
    }

    public void recibirMensajePrivado(String usuario, String mensaje) {
        Platform.runLater(() -> {
            String key = usuario.toLowerCase();
            PrivadoController ctrl = privateChatsController.get(key);
            if (ctrl == null) {
                abrirChatPrivado(usuario);
                ctrl = privateChatsController.get(key);
            }
            if (ctrl != null)
                ctrl.appendMessage(usuario, mensaje);
        });
    }

    // ------------------ Canales ------------------
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

        floatingWindows.add(canalStage);
        CanalVentana ventana = new CanalVentana(canalStage, canalController, canalPane);
        canalesAbiertos.put(canal, ventana);

        Button canalBtn = new Button(canal);
        canalBtn.setMaxWidth(Double.MAX_VALUE);
        canalBtn.setOnAction(e -> {
            canalStage.toFront();
            canalActivo = canal;
        });
        if (leftPane != null)
            leftPane.getChildren().add(canalBtn);
        canalButtons.put(canal, canalBtn);

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
        if (ventana != null) {
            Platform.runLater(() -> ventana.stage.close());
            Button btn = canalButtons.remove(canal);
            if (btn != null && leftPane != null)
                Platform.runLater(() -> leftPane.getChildren().remove(btn));
            floatingWindows.remove(ventana.stage);
        }
    }

    public void actualizarUsuariosCanal(String canal, List<String> usuarios) {
        CanalVentana ventana = canalesAbiertos.get(canal);
        if (ventana != null && ventana.controller != null) {
            ventana.controller.updateUsers(usuarios);
        }
    }

    public void connectToIRC() {
        new Thread(() -> {
            try {
                Configuration config = new Configuration.Builder().setName("akiles54321").setLogin("akiles54321")
                        .setRealName("JIRCHAT").addServer("irc.chatzona.org", 6697)
                        .setSocketFactory(SSLSocketFactory.getDefault()).setAutoNickChange(true).setAutoReconnect(true)
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
                            public void onPrivateMessage(PrivateMessageEvent event) {
                                String usuario = event.getUser().getNick();
                                String mensaje = event.getMessage();
                                recibirMensajePrivado(usuario, mensaje);
                            }

                            @Override
                            public void onMessage(MessageEvent event) {
                                String usuario = event.getUser().getNick();
                                String mensaje = event.getMessage();
                                String canal = event.getChannel().getName();

                                CanalVentana ventana = canalesAbiertos.get(canal);
                                if (ventana != null) ventana.controller.appendMessage(usuario, mensaje);
                            }

                            @Override
                            public void onAction(ActionEvent event) {
                                String usuario = event.getUser().getNick();
                                String mensaje = "* " + event.getAction();
                                String canal = event.getChannel().getName();

                                CanalVentana ventana = canalesAbiertos.get(canal);
                                if (ventana != null) ventana.controller.appendMessage(usuario, mensaje);
                            }

                            @Override
                            public void onUserList(UserListEvent event) {
                                List<String> usuariosConPrefijo = event.getUsers().stream()
                                        .map(u -> {
                                            String prefix = "";
                                            if(event.getChannel().getOps().contains(u)) prefix = "@";
                                            else if(event.getChannel().getVoices().contains(u)) prefix = "+";
                                            return prefix + u.getNick();
                                        })
                                        .collect(Collectors.toList());

                                actualizarUsuariosCanal(event.getChannel().getName(), usuariosConPrefijo);
                            }

                            @Override
                            public void onJoin(JoinEvent event) {
                                actualizarUsuariosCanal(event.getChannel().getName(), reconstruirListaConPrefijos(event.getChannel()));
                            }

                            @Override
                            public void onPart(PartEvent event) {
                                actualizarUsuariosCanal(event.getChannel().getName(), reconstruirListaConPrefijos(event.getChannel()));
                            }

                            @Override
                            public void onQuit(QuitEvent event) {
                                for (CanalVentana ventana : canalesAbiertos.values()) {
                                    actualizarUsuariosCanal(ventana.stage.getTitle(), reconstruirListaConPrefijos(ventana.controller.getCanalChannel()));
                                }
                            }

                            @Override
                            public void onKick(KickEvent event) {
                                actualizarUsuariosCanal(event.getChannel().getName(), reconstruirListaConPrefijos(event.getChannel()));
                            }

                            @Override
                            public void onMode(ModeEvent event) {
                                if(event.getChannel() != null) {
                                    String canal = event.getChannel().getName();
                                    CanalVentana ventana = canalesAbiertos.get(canal);
                                    if(ventana != null) {
                                        List<String> usuariosConPrefijo = reconstruirListaConPrefijos(event.getChannel());
                                        ventana.controller.updateUsers(usuariosConPrefijo);
                                    }
                                }
                            }

                            private List<String> reconstruirListaConPrefijos(org.pircbotx.Channel channel) {
                                return channel.getUsers().stream()
                                        .map(u -> {
                                            String prefix = "";
                                            if(channel.getOps().contains(u)) prefix = "@";
                                            else if(channel.getVoices().contains(u)) prefix = "+";
                                            return prefix + u.getNick();
                                        })
                                        .collect(Collectors.toList());
                            }

                        }).buildConfiguration();

                bot = new PircBotX(config);
                bot.startBot();
            } catch (Exception e) {
                appendSystemMessage("Error al conectar: " + e.getMessage());
            }
        }).start();
    }

    public void cerrarTodo(String mensaje) {
        for (String canal : new ArrayList<>(canalesAbiertos.keySet()))
            cerrarCanalDesdeVentana(canal);
        for (Stage s : new ArrayList<>(privateChats.values()))
            Platform.runLater(s::close);
        privateChats.clear();
        privateChatsController.clear();
        privadoButtons.clear();

        if (bot != null) {
            try {
                bot.sendIRC().quitServer(mensaje != null ? mensaje : "Cerrando cliente");
                bot.stopBotReconnect();
                bot.close();
            } catch (Exception ignored) {
            }
        }

        Platform.runLater(() -> {
            try {
                Stage primaryStage = (Stage) rootPane.getScene().getWindow();
                if (primaryStage != null)
                    primaryStage.close();
            } finally {
                Platform.exit();
                System.exit(0);
            }
        });
    }

    public void bindFloatingWindowsToRightPane(Stage primaryStage) {
        if (primaryStage == null)
            return;

        primaryStage.setOnCloseRequest(event -> {
            for (Stage s : floatingWindows)
                Platform.runLater(s::close);
            for (Stage s : privateChats.values())
                Platform.runLater(s::close);
        });
    }
}
