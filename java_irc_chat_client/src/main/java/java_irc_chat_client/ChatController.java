package java_irc_chat_client;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
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
import org.pircbotx.User;
import org.pircbotx.hooks.events.UserListEvent;
import java.util.stream.Collectors;
import java.util.Objects;
import javax.net.ssl.SSLSocketFactory;
import java.io.IOException;
import java.util.*;

public class ChatController {

    @FXML private AnchorPane rootPane;
    @FXML private TextArea chatArea;
    @FXML private TextField inputField;
    @FXML private ListView<String> userList;

    private VBox leftPane;
    private StackPane rightPane;
    private PircBotX bot;
    private ChatController mainController;
    
    public void setMainController(ChatController mainController){ this.mainController = mainController; }
    
    private String canal;  // <-- Asegúrate de tener esto
    
    public void appendMessage(String usuario, String mensaje){
        chatArea.appendText("<" + usuario + "> " + mensaje + "\n");
    }

    public void updateUsers(List<String> users){
        userList.getItems().setAll(users);
    }

    public void setBot(PircBotX bot){ this.bot = bot; }
    public void setCanal(String canal){ this.canal = canal; }
    
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

    private final Map<String, CanalVentana> canalesAbiertos = new HashMap<>();
    private final Map<String, Button> canalButtons = new HashMap<>();
    private String canalActivo = null;

    // Buffers necesarios
    private final Map<String, Set<String>> usersBuffer = new HashMap<>();
    private final Map<String, List<String>> messagesBuffer = new HashMap<>();

    private final List<Stage> floatingWindows = new ArrayList<>();
    private AnchorPane currentFrontPane;

    public void setLeftPane(VBox leftPane) { this.leftPane = leftPane; }
    public void setRightPane(StackPane rightPane) { 
        this.rightPane = rightPane; 
        this.currentFrontPane = rootPane;
    }

    public AnchorPane getRootPane() { return rootPane; }

    @FXML
    public void initialize() {
        inputField.setOnAction(e -> sendCommand());
    }

    private void resizeAllFloatingWindows() {
        Platform.runLater(() -> {
            for (Stage stage : floatingWindows) {
                if (rightPane != null) {
                    stage.setWidth(rightPane.getWidth());
                    stage.setHeight(rightPane.getHeight());
                }
            }
        });
    }

    private void repositionAllFloatingWindows() {
        Platform.runLater(() -> {
            if (rightPane == null) return;
            javafx.geometry.Point2D pos = rightPane.localToScreen(0, 0);
            if (pos == null) return;
            for (Stage stage : floatingWindows) {
                stage.setX(pos.getX());
                stage.setY(pos.getY());
            }
        });
    }

    public void bindFloatingWindowsToRightPane(Stage primaryStage) {
        if (rightPane == null) return;

        rightPane.widthProperty().addListener((obs, oldV, newV) -> resizeAllFloatingWindows());
        rightPane.heightProperty().addListener((obs, oldV, newV) -> resizeAllFloatingWindows());
        primaryStage.xProperty().addListener((obs, oldV, newV) -> repositionAllFloatingWindows());
        primaryStage.yProperty().addListener((obs, oldV, newV) -> repositionAllFloatingWindows());
    }

    private void sendCommand() {
        String text = inputField.getText().trim();
        if (text.isEmpty()) return;
        boolean isCommand = text.startsWith("/");

        try {
            if (isCommand) {
                String cmd = text.substring(1).trim();
                if (cmd.startsWith("join ")) abrirCanal(cmd.substring(5).trim());
                else if (cmd.startsWith("part")) {
                    String[] parts = cmd.split(" ",3);
                    String canal = parts.length >=2 ? parts[1] : canalActivo;
                    String message = parts.length==3 ? parts[2] : "";
                    cerrarCanalDesdeVentana(canal);
                    if(!message.isEmpty()) bot.sendRaw().rawLine("PART "+canal+" :"+message);
                    else bot.sendRaw().rawLine("PART "+canal);
                }
                else if (cmd.startsWith("quit")) cerrarTodo("Cerrando cliente");
                else if (cmd.startsWith("nick ")) bot.sendIRC().changeNick(cmd.substring(5).trim());
                else bot.sendRaw().rawLine(cmd);
            } else {
                if (canalActivo != null && canalesAbiertos.containsKey(canalActivo)) {
                    CanalVentana v = canalesAbiertos.get(canalActivo);
                    bot.sendIRC().message(canalActivo, text);
                    v.controller.appendMessage("Yo", text);
                } else {
                    bot.sendIRC().message("#mas_de_40", text);
                    chatArea.appendText("<Yo> "+text+"\n");
                }
            }
        } catch(Exception e) {
            chatArea.appendText("⚠ Error al ejecutar comando: "+e.getMessage()+"\n");
        }
        inputField.clear();
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

    public void cerrarTodo(String mensaje){
        for(String canal : new ArrayList<>(canalesAbiertos.keySet())) cerrarCanalDesdeVentana(canal);
        if(bot != null){
            try{
                bot.sendIRC().quitServer(mensaje != null ? mensaje : "Cerrando cliente");
                bot.stopBotReconnect();
                bot.close();
            }catch(Exception ignored){}
        }
        Platform.runLater(() -> {
            try{
                Stage primaryStage = (Stage)rootPane.getScene().getWindow();
                if(primaryStage != null) primaryStage.close();
            }finally{
                Platform.exit();
                System.exit(0);
            }
        });
    }

    public void connectToIRC() {
        new Thread(() -> {
            try {
                Configuration config = new Configuration.Builder()
                        .setName("akiles5432")
                        .setLogin("akiles5432")
                        .setRealName("JIRCHAT")
                        .addServer("irc.chatzona.org", 6697)
                        .setSocketFactory(SSLSocketFactory.getDefault())
                        .setAutoNickChange(true)
                        .setAutoReconnect(true)
                        .addListener(new ListenerAdapter() {
                            private boolean identificado = false;

                            @Override
                            public void onConnect(ConnectEvent event) {
                                Platform.runLater(() -> chatArea.appendText("✅ Conectado a irc.chatzona.org\n"));
                                // Envía identificación
                                bot.sendIRC().message("NickServ", "IDENTIFY <tu_password>");
                            }
                            
                            /* ... dentro de new ListenerAdapter() { */
                            @Override
                            public void onUserList(UserListEvent event) {
                                // Nombre del canal
                                String canal = event.getChannel() != null ? event.getChannel().getName() : null;
                                if (canal == null) return;

                                // Obtener los usuarios (User) y convertir a lista de nicks ordenada
                                Set<User> userObjs = event.getUsers(); // Set<User>
                                List<String> nicks = userObjs.stream()
                                    .map(User::getNick)
                                    .filter(Objects::nonNull)
                                    .map(String::trim)
                                    .filter(s -> !s.isEmpty())
                                    .sorted(String.CASE_INSENSITIVE_ORDER)
                                    .collect(Collectors.toList());

                                // Guardar en el buffer y actualizar la ventana abierta si existe
                                usersBuffer.put(canal, new HashSet<>(nicks));
                                if (canalesAbiertos.containsKey(canal)) {
                                    CanalVentana v = canalesAbiertos.get(canal);
                                    Platform.runLater(() -> v.controller.updateUsers(nicks));
                                }

                                // para depuración (opcional)
                                Platform.runLater(() -> chatArea.appendText("NAMES ("+canal+"): "+nicks+"\n"));
                            }


                            @Override
                            public void onNotice(NoticeEvent event) {
                                String from = event.getUser() != null ? event.getUser().getNick() : "Servidor";
                                String mensaje = event.getMessage();
                                Platform.runLater(() -> chatArea.appendText("[" + from + "] " + mensaje + "\n"));

                                // Detectar identificación exitosa
                                if(!identificado && mensaje.toLowerCase().contains("identified")) {
                                    identificado = true;
                                    Platform.runLater(() -> chatArea.appendText("✅ Identificación exitosa. Ahora puedes unirte a canales.\n"));

                                    // Unir a todos los canales abiertos
                                    for(String canal : canalesAbiertos.keySet()){
                                        bot.sendIRC().joinChannel(canal);
                                    }
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
                                    Platform.runLater(() -> chatArea.appendText("<"+usuario+"> "+mensaje+"\n"));
                                }
                            }

                            @Override
                            public void onJoin(JoinEvent event){
                                String canal = event.getChannel().getName();
                                String usuario = event.getUser().getNick();
                                usersBuffer.putIfAbsent(canal, new HashSet<>());
                                usersBuffer.get(canal).add(usuario);

                                if(canalesAbiertos.containsKey(canal)){
                                    CanalVentana v = canalesAbiertos.get(canal);
                                    List<String> ulist = new ArrayList<>(usersBuffer.get(canal));
                                    Collections.sort(ulist, String.CASE_INSENSITIVE_ORDER);
                                    Platform.runLater(() -> v.controller.updateUsers(ulist));
                                }
                            }

                            @Override
                            public void onPart(PartEvent event){
                                String canal = event.getChannel().getName();
                                String usuario = event.getUser().getNick();
                                if(usersBuffer.containsKey(canal)){
                                    usersBuffer.get(canal).remove(usuario);
                                    if(canalesAbiertos.containsKey(canal)){
                                        CanalVentana v = canalesAbiertos.get(canal);
                                        List<String> ulist = new ArrayList<>(usersBuffer.get(canal));
                                        Collections.sort(ulist,String.CASE_INSENSITIVE_ORDER);
                                        Platform.runLater(() -> v.controller.updateUsers(ulist));
                                    }
                                }
                            }

                            @Override
                            public void onQuit(QuitEvent event){
                                String usuario = event.getUser().getNick();
                                for(String canal: usersBuffer.keySet()){
                                    if(usersBuffer.get(canal).remove(usuario) && canalesAbiertos.containsKey(canal)){
                                        CanalVentana v = canalesAbiertos.get(canal);
                                        List<String> ulist = new ArrayList<>(usersBuffer.get(canal));
                                        Collections.sort(ulist,String.CASE_INSENSITIVE_ORDER);
                                        Platform.runLater(() -> v.controller.updateUsers(ulist));
                                    }
                                }
                            }
                        }).buildConfiguration();

                bot = new PircBotX(config);
                bot.startBot();
            }catch(Exception e){
                Platform.runLater(() -> chatArea.appendText("Error al conectar: "+e.getMessage()+"\n"));
            }
        }).start();
    }

    private void abrirCanal(String canal) throws IOException {
        if(canalesAbiertos.containsKey(canal)){
            CanalVentana v = canalesAbiertos.get(canal);
            v.stage.toFront();
            canalActivo = canal;
            return;
        }

        FXMLLoader loader = new FXMLLoader(getClass().getResource("JIRCHAT_CANAL.fxml"));
        AnchorPane canalPane = loader.load();
        CanalController canalController = loader.getController(); // <-- Aquí obtienes el controller

        // Configurar el controller
        canalController.setBot(bot);
        canalController.setCanal(canal);
        canalController.setMainController(this);

        Stage canalStage = new Stage();
        canalStage.setTitle("Canal " + canal);
        canalStage.setScene(new Scene(canalPane));

        if(rightPane != null && rightPane.getScene() != null){
            javafx.geometry.Point2D pos = rightPane.localToScreen(0, 0);
            if(pos != null){
                canalStage.setX(pos.getX());
                canalStage.setY(pos.getY());
            }
            canalStage.setWidth(rightPane.getWidth());
            canalStage.setHeight(rightPane.getHeight());
        }

        floatingWindows.add(canalStage);
        CanalVentana ventana = new CanalVentana(canalStage, canalController, canalPane);
        canalesAbiertos.put(canal, ventana);

        Button canalBtn = new Button(canal);
        canalBtn.setMaxWidth(Double.MAX_VALUE);
        canalBtn.setOnAction(e -> { canalStage.toFront(); canalActivo = canal; });
        if(leftPane != null) leftPane.getChildren().add(canalBtn);
        canalButtons.put(canal, canalBtn);

        canalStage.setOnCloseRequest(ev -> cerrarCanalDesdeVentana(canal));

        canalActivo = canal;
        canalStage.show();

        // Unirse al canal
        bot.sendIRC().joinChannel(canal);
        bot.sendRaw().rawLineNow("NAMES " + canal);

        // Mostrar mensajes buffer si existen
        if(messagesBuffer.containsKey(canal)){
            List<String> buffer = messagesBuffer.get(canal);
            buffer.forEach(m -> canalController.appendMessage("Servidor", m));
            messagesBuffer.remove(canal);
        }

        // Mostrar usuarios buffer si existen
        if(usersBuffer.containsKey(canal)){
            List<String> ulist = new ArrayList<>(usersBuffer.get(canal));
            Collections.sort(ulist, String.CASE_INSENSITIVE_ORDER);
            canalController.updateUsers(ulist);
        }

        chatArea.appendText("➡ Uniéndote a " + canal + "\n");
    }


}
