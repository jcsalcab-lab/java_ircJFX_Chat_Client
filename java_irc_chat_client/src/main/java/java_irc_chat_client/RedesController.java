package java_irc_chat_client;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class RedesController {

    // --- Primer cuadro: Redes ---
    @FXML
    private ComboBox<String> comboRedes;

    @FXML
    private Button btnNueva;

    @FXML
    private Button btnBorrar;

    // --- Segundo cuadro: Configuración Red ---
    @FXML
    private TextField txtNombreRed;

    @FXML
    private TextField txtFicheroMenus;

    @FXML
    private Button btnBuscarFichero;

    @FXML
    private TextField txtApareceComo;

    // --- Tercer cuadro: Bots ---
    @FXML
    private TextField txtMascaraIpBots;

    @FXML
    private TextField txtNickserv;

    @FXML
    private TextField txtChanserv;

    @FXML
    private TextField txtMemoserv;

    @FXML
    private TextField txtPideId;

    // --- Checkboxes y ayuda ---
    @FXML
    private CheckBox chkAutoCargarMenus;

    @FXML
    private CheckBox chkMensajeBotsPrivado;

    @FXML
    private CheckBox chkSinPerfil;

    @FXML
    private Button btnAyuda;

    // Método de inicialización
    @FXML
    private void initialize() {
        // Aquí puedes inicializar la ComboBox o cualquier valor por defecto
        comboRedes.getItems().addAll("Red1", "Red2", "Red3");
    }

    // --- Métodos para los botones ---
    @FXML
    private void onNueva() {
        System.out.println("Botón Nueva presionado");
        // Lógica para crear una nueva red
    }

    @FXML
    private void onBorrar() {
        System.out.println("Botón Borrar presionado");
        // Lógica para borrar la red seleccionada
    }

    @FXML
    private void onBuscarFichero() {
        System.out.println("Botón Buscar Fichero presionado");
        // Lógica para abrir un FileChooser
    }

    @FXML
    private void onAyuda() {
        System.out.println("Botón Ayuda presionado");
        // Mostrar ayuda o diálogo
    }
    
    @FXML
    private void onGuarda(ActionEvent event) {
        // Aquí pones lo que debe hacer el botón Guarda
        System.out.println("Botón Guarda pulsado");
    }
}
