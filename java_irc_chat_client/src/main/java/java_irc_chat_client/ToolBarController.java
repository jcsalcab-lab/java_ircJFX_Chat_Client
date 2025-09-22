package java_irc_chat_client;

import javafx.event.ActionEvent;
import java.io.IOException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;




public class ToolBarController {

    private StackPane rightPane;
    private VBox leftPane;
    private ChatController chatController;
    private AnchorPane statusPane;  // Pane del Status
    private AnchorPane currentFrontPane;  // Pane en primer plano

    public void setRightPane(StackPane rightPane) { this.rightPane = rightPane; }
    public void setLeftPane(VBox leftPane) { this.leftPane = leftPane; }
    public ChatController getChatController() { return chatController; }

    @FXML
    private Button btnUserList;
    
    
    @FXML
    private Button btnConnect;

    
    @FXML
    private void onSetupButtonClicked() {
        try {
        	FXMLLoader loader = new FXMLLoader(getClass().getResource("/java_irc_chat_client/JIRCHAT_SETUP.fxml"));

        	Parent root = loader.load();
        	Stage stage = new Stage();
        	stage.setTitle("Configuración");
        	stage.setScene(new Scene(root));
        	stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    
    @FXML
    private void abrirLogs() {
        try {
        	FXMLLoader loader = new FXMLLoader(getClass().getResource("JIRCHAT_LOGS.fxml"));

            AnchorPane root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Logs");
            stage.setScene(new Scene(root));
            stage.setResizable(false); // no maximizar ni minimizar
            stage.initModality(Modality.NONE); // ventana independiente
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR,
                    "Error abriendo ventana de logs: " + e.getMessage(),
                    ButtonType.OK);
            alert.showAndWait();
        }
    }



    /**
     * Abre la ventana de usuarios conocidos y fuerza la recarga del XML cada vez.
     */
    @FXML
    private void abrirUserList() {
        try {
            // Crear un FXMLLoader nuevo cada vez
            FXMLLoader loader = new FXMLLoader(getClass().getResource("JIRCHAT_LISTAS_USUARIOS_CONOCIDOS.fxml"));
            Parent root = loader.load();

            // Obtener controlador recién creado
            UsuariosController controller = loader.getController();

            // Cargar los datos desde el XML siempre al abrir la ventana
            controller.cargarUsuariosDesdeXML();

            Stage stage = new Stage();
            stage.setTitle("Usuarios Conocidos");
            stage.setScene(new Scene(root));
            stage.initOwner(btnUserList.getScene().getWindow());
            stage.initModality(Modality.NONE);

            // Limpiar tabla al cerrar la ventana para evitar cache de datos
            stage.setOnCloseRequest(event -> controller.limpiarTabla());

            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    @FXML
    private void abrirVentanaConexion(ActionEvent event) {
        try {
            // Cargar FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/java_irc_chat_client/JIRCHAT_CONEXION.fxml"));
            Parent root = loader.load();

            // Obtener el controlador de la ventana de conexión
            ConexionController controller = loader.getController();

            // Cargar los datos directamente desde el fichero físico
            controller.cargarFormulario(); // ahora es público

            // Crear la nueva ventana
            Stage stage = new Stage();
            stage.setTitle("Conexión IRC");
            stage.setScene(new Scene(root));
            stage.setResizable(true);
            stage.initOwner(btnConnect.getScene().getWindow()); // ventana padre
            stage.initModality(Modality.NONE);

            // Registrar el guardado al cerrar la ventana
            stage.setOnCloseRequest(e -> {
                try {
                    controller.guardarFormulario(); // guarda en el fichero físico
                } catch (Exception ex) {
                    ex.printStackTrace();
                    Alert alert = new Alert(Alert.AlertType.ERROR,
                            "Error guardando formulario al cerrar: " + ex.getMessage(),
                            ButtonType.OK);
                    alert.showAndWait();
                }
            });

            // Mostrar la ventana
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR,
                    "Error abriendo la ventana de conexión: " + e.getMessage(),
                    ButtonType.OK);
            alert.showAndWait();
        }
    }





    /**
     * Abre la ventana de chat y vincula las floating windows al primaryStage.
     */
    @FXML
    public void abrirChat(Stage primaryStage) {
        if (chatController != null) return; // Ya abierto

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("JIRCHAT_CONNECT_STAGE.fxml"));
            AnchorPane chatPane = loader.load();
            chatController = loader.getController();

            // Vincular panes
            chatController.setLeftPane(leftPane);
            chatController.setRightPane(rightPane);

            // Añadir al rightPane
            rightPane.getChildren().add(chatPane);

            // Guardar statusPane y ponerlo en primer plano
            statusPane = (AnchorPane) chatController.getRootPane();

            currentFrontPane = statusPane;

            // Conectar al IRC
            chatController.connectToIRC();

            // Crear botón Status en el leftPane
            if (leftPane != null) {
                Button statusButton = new Button("Status");
                statusButton.setMaxWidth(Double.MAX_VALUE);
                statusButton.setOnAction(e -> showStatus());
                leftPane.getChildren().add(statusButton);
            }

            // Vincular ventanas flotantes al primaryStage
            if (primaryStage != null) {
                chatController.bindFloatingWindowsToRightPane(primaryStage);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Muestra el panel de estado en primer plano.
     */
    private void showStatus() {
        if (statusPane == null) return;

        if (currentFrontPane == null || currentFrontPane == statusPane) {
            statusPane.toFront();
            currentFrontPane = statusPane;
        }
    }
}



