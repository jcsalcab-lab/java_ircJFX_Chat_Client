package java_irc_chat_client;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

public class MediaController {

    @FXML
    private ComboBox<String> eventoComboBox;

    @FXML
    private TextField sonidoTextField;

    @FXML
    private TextField audioTextField;

    @FXML
    private TextField videoTextField;

    @FXML
    private Button importarButton;

    @FXML
    private Button exportarButton;

    @FXML
    private Button seleccionarAudioButton;

    @FXML
    private Button seleccionarVideoButton;

    @FXML
    private Button reproducirSonidoButton;

    @FXML
    private void initialize() {
        // Inicialización de la ComboBox, por ejemplo
        eventoComboBox.getItems().addAll("Evento1", "Evento2", "Evento3");

        // Puedes agregar handlers de botón si quieres
        // seleccionarAudioButton.setOnAction(...);
    }
}
