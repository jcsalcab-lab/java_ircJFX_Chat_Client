package java_irc_chat_client;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.scene.paint.Color;
import org.pircbotx.PircBotX;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.ServerResponseEvent;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class CanalesListController {

    @FXML private TableView<Canal> canalesTable;
    @FXML private TableColumn<Canal, String> colCanal;
    @FXML private TableColumn<Canal, Integer> colUsuarios;
    @FXML private TableColumn<Canal, String> colDescripcion;

    @FXML private TextField txtBusqueda;
    @FXML private Button btnBuscar;

    private PircBotX bot;
    private ListenerAdapter listener;
    private final ObservableList<Canal> canales = FXCollections.observableArrayList();

    // ChatController para abrir ventana de chat
    private ChatController chatController;

    public CanalesListController() {
        // Constructor vacío, no se lanza IOException
    }

    // Inyección del ChatController
    public void setChatController(ChatController chatController) {
        this.chatController = chatController;
    }

    // Inyección del bot
    public void setBot(PircBotX bot) {
        if (this.bot != null && listener != null) {
            try { this.bot.getConfiguration().getListenerManager().removeListener(listener); } catch (Exception ignored) {}
        }
        this.bot = bot;
        if (this.bot != null) {
            cargaCanalesServidor();
        }
    }

    @FXML
    public void initialize() {
        // Configurar columnas
        colCanal.setCellValueFactory(data -> data.getValue().nombreProperty());
        colUsuarios.setCellValueFactory(data -> data.getValue().numUsuariosProperty().asObject());
        colDescripcion.setCellValueFactory(data -> data.getValue().descripcionProperty());

        // Alterna colores de filas y doble clic
        canalesTable.setRowFactory(tv -> {
            TableRow<Canal> row = new TableRow<>() {
                @Override
                protected void updateItem(Canal item, boolean empty) {
                    super.updateItem(item, empty);
                    setPrefHeight(24);
                    setMinHeight(24);
                    setMaxHeight(24);

                    if (empty || item == null) {
                        setStyle("");
                    } else {
                        String search = txtBusqueda.getText();
                        if (search != null && !search.isEmpty() &&
                                item.getNombre().toLowerCase().contains(search.toLowerCase())) {
                            setStyle("-fx-background-color: #39FF14;"); // verde fosforito
                        } else if (getIndex() % 2 == 0) {
                            setStyle("-fx-background-color: #FFF8DC;"); // vainilla
                        } else {
                            setStyle("-fx-background-color: #ADD8E6;"); // azul claro
                        }
                    }
                }
            };

            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && !row.isEmpty()) {
                    Canal canal = row.getItem();

                    // Debug: mostrar en consola el canal seleccionado
                    System.out.println("👉 Doble clic sobre el canal: " + (canal != null ? canal.getNombre() : "NULL"));

                    if (chatController != null && canal != null) {
                        Platform.runLater(() -> {
                            try {
                                System.out.println("✅ Abriendo canal desde chatController: " + canal.getNombre());
                                chatController.abrirCanal(canal.getNombre());
                            } catch (IOException e) {
                                e.printStackTrace();
                                // Opcional: mostrar alerta al usuario
                                Alert alert = new Alert(Alert.AlertType.ERROR);
                                alert.setTitle("Error al abrir canal");
                                alert.setHeaderText(null);
                                alert.setContentText("No se pudo abrir el canal: " + e.getMessage());
                                alert.showAndWait();
                            }
                        });
                    } else {
                        System.out.println("⚠ No se pudo abrir el canal: chatController es NULL o canal es NULL");
                    }
                }
            });



            return row;
        });

        canalesTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        // Columna descripción con colores IRC
        colDescripcion.setCellFactory(col -> new TableCell<>() {
            private final TextFlow textFlow = new TextFlow();
            {
                textFlow.setPrefHeight(24);
                textFlow.setMinHeight(24);
                textFlow.setMaxHeight(24);
                textFlow.setLineSpacing(0);
            }

            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setGraphic(null);
                } else {
                    textFlow.getChildren().clear();
                    textFlow.getChildren().addAll(parseIRCText(item));
                    textFlow.prefWidthProperty().bind(colDescripcion.widthProperty().subtract(10));
                    setGraphic(textFlow);
                }
            }
        });

        btnBuscar.setOnAction(e -> canalesTable.refresh());
    }

    private List<Text> parseIRCText(String ircText) {
        List<Text> texts = new ArrayList<>();
        if (ircText == null || ircText.isEmpty()) return texts;

        Color[] ircColors = {
                Color.WHITE, Color.BLACK, Color.BLUE, Color.GREEN, Color.RED,
                Color.BROWN, Color.PURPLE, Color.ORANGE, Color.YELLOW,
                Color.LIGHTGREEN, Color.CYAN, Color.LIGHTCYAN, Color.BLUEVIOLET,
                Color.PINK, Color.GREY, Color.LIGHTGREY
        };

        Color currentColor = Color.BLACK;
        int i = 0;
        while (i < ircText.length()) {
            char c = ircText.charAt(i);
            if (c == '\u0003') {
                i++;
                StringBuilder number = new StringBuilder();
                while (i < ircText.length() && Character.isDigit(ircText.charAt(i)) && number.length() < 2) {
                    number.append(ircText.charAt(i));
                    i++;
                }
                if (number.length() > 0) {
                    int code = Integer.parseInt(number.toString());
                    currentColor = code >= 0 && code < ircColors.length ? ircColors[code] : Color.BLACK;
                } else currentColor = Color.BLACK;
            } else if (c == '\u000f') {
                currentColor = Color.BLACK;
                i++;
            } else {
                StringBuilder sb = new StringBuilder();
                while (i < ircText.length() && ircText.charAt(i) != '\u0003' && ircText.charAt(i) != '\u000f') {
                    sb.append(ircText.charAt(i));
                    i++;
                }
                Text t = new Text(sb.toString());
                t.setFill(currentColor);
                texts.add(t);
            }
        }
        return texts;
    }

    public void cargaCanalesServidor() {
        if (bot == null) return;
        Platform.runLater(canales::clear);

        listener = new ListenerAdapter() {
            @Override
            public void onServerResponse(ServerResponseEvent event) {
                int code = event.getCode();

                if (code == 322) { // Canal
                    var parts = event.getParsedResponse();
                    if (parts.size() >= 3) {
                        String nombre = parts.get(1);
                        int numUsuarios = Integer.parseInt(parts.get(2));
                        String topic = parts.size() > 3 ? parts.get(3) : "";
                        Platform.runLater(() -> canales.add(new Canal(nombre, numUsuarios, "", topic)));
                    }
                } else if (code == 323) { // Fin de lista
                    Platform.runLater(() -> canales.sort(Comparator.comparing(Canal::getNombre, String.CASE_INSENSITIVE_ORDER)));
                    bot.getConfiguration().getListenerManager().removeListener(this);
                }
            }
        };

        canalesTable.setItems(canales);
        bot.getConfiguration().getListenerManager().addListener(listener);
        bot.sendRaw().rawLine("LIST");
    }

    public List<Canal> getCanalesSeleccionados() {
        return new ArrayList<>(canalesTable.getSelectionModel().getSelectedItems());
    }

    public void shutdown() {
        if (bot != null && listener != null) {
            try { bot.getConfiguration().getListenerManager().removeListener(listener); } catch (Exception ignored) {}
        }
    }
}

